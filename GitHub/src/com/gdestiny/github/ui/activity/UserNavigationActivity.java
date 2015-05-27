package com.gdestiny.github.ui.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.UserService;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseFragmentActivity;
import com.gdestiny.github.abstracts.async.BaseAsyncTask;
import com.gdestiny.github.abstracts.fragment.BaseLoadFragment;
import com.gdestiny.github.adapter.SimplePageAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.async.FollowUserTask;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.dialog.StatusPopWindowItem;
import com.gdestiny.github.ui.fragment.EventsUserFragment;
import com.gdestiny.github.ui.fragment.FollowerFragment;
import com.gdestiny.github.ui.fragment.FollowingFragment;
import com.gdestiny.github.ui.fragment.RepositoryPageFragment;
import com.gdestiny.github.ui.view.IndicatorView;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.ToastUtils;

public class UserNavigationActivity extends BaseFragmentActivity {

	private ViewPager viewpager;
	private IndicatorView indicatorView;
	private List<BaseLoadFragment<?, ?>> fragments = new ArrayList<BaseLoadFragment<?, ?>>();

	private SimplePageAdapter adapter;

	private User user;
	private boolean isFollowing;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.act_user_navigation);
	}

	@Override
	protected void initView() {
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		viewpager.setOffscreenPageLimit(3);

		indicatorView = (IndicatorView) findViewById(R.id.indicator);

		indicatorView.bind(viewpager);
		viewpager.setOnPageChangeListener(indicatorView);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		user = (User) getIntent().getSerializableExtra(Constants.Extra.USER);
		if (user == null) {
			ToastUtils.show(context, "error");
			return;
		}
		getTitlebar().setLeftLayout(user.getAvatarUrl(), user.getLogin(), null);
		initWatch();

		indicatorView
				.add(R.string.tab_repository,
						R.drawable.common_repository_normal)
				.add(R.string.events_l, R.drawable.common_news_normal)
				.add(R.string.tab_follower, R.drawable.common_follower_normal)
				.add(R.string.tab_following, R.drawable.common_following_normal);

		fragments.add(new RepositoryPageFragment());
		fragments.add(new EventsUserFragment(user.getLogin()));
		fragments.add(new FollowerFragment(user.getLogin()));
		fragments.add(new FollowingFragment(user.getLogin()));

		adapter = new SimplePageAdapter(getSupportFragmentManager(), fragments);
		viewpager.setAdapter(adapter);
	}

	@Override
	protected void initActionBar(TitleBar titleBar) {
		super.initActionBar(titleBar);
		LinkedHashMap<Integer, Integer> itemmap = new LinkedHashMap<Integer, Integer>();
		itemmap.put(R.string.follow, R.drawable.common_follow_grey);
		itemmap.put(R.string.refresh, R.drawable.common_status_refresh);

		StatusPopUpWindow.StatusPopUpWindowItemClickListener menuListener = new StatusPopUpWindow.StatusPopUpWindowItemClickListener() {

			@Override
			public void onitemclick(int titleId) {
				GLog.sysout(context.getResources().getString(titleId) + "");
				switch (titleId) {
				case R.string.refresh:
					if (!fragments.get(indicatorView.getCurrentPosition())
							.isLoading()) {
						fragments.get(indicatorView.getCurrentPosition())
								.onRefresh();
					}
					break;
				case R.string.follow:
					new FollowUserTask(context, isFollowing, user.getLogin()) {

						@Override
						public void onSuccess(Boolean result) {
							super.onSuccess(result);
							isFollowing = !isFollowing;
							refreshFollow(isFollowing);
						}
					}.execute();
					break;
				}
				titlebar.dissmissStatus();
			}
		};
		titlebar.setStatusItem(context, itemmap, menuListener);
	}

	private void initWatch() {
		new BaseAsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				UserService service = new UserService(
						GitHubApplication.getClient());
				try {
					return service.isFollowing(user.getLogin());
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				isFollowing = result;
				refreshFollow(result);
			}

		}.execute();
	}

	private void refreshFollow(boolean isFollowing) {
		StatusPopWindowItem item = getTitlebar().getStatusPopup().getAction(0);
		if (isFollowing)
			item.mTitle = getResources().getString(R.string.unfollow);
		else
			item.mTitle = getResources().getString(R.string.follow);
	}

	@Override
	protected void onleftLayout() {
		finish();
	}
}
