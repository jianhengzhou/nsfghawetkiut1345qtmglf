package com.gdestiny.github.ui.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.WatcherService;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.gdestiny.github.R;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.async.BaseAsyncTask;
import com.gdestiny.github.async.StarTask;
import com.gdestiny.github.ui.dialog.StatusPopWindowItem;
import com.gdestiny.github.ui.fragment.BaseLoadFragment;
import com.gdestiny.github.ui.fragment.RepositoryCodeFragment;
import com.gdestiny.github.ui.fragment.RepositoryCommitFragment;
import com.gdestiny.github.ui.fragment.RepositoryEventFragment;
import com.gdestiny.github.ui.fragment.RepositoryIssuesFragment;
import com.gdestiny.github.ui.view.IndicatorView;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.ToastUtils;

public class RepositoryDetailActivity extends BaseFragmentActivity {

	public static final String EXTRA_REPOSITORY = "repository";

	private Repository repository;
	private ViewPager viewpager;
	private RepositoryPageAdapter adapter;

	private IndicatorView indicatorView;

	private List<BaseLoadFragment<?, ?>> fragments = new ArrayList<BaseLoadFragment<?, ?>>();

	private boolean isStarred;

	public boolean isStarred() {
		return isStarred;
	}

	public void setStarred(boolean isStared) {
		this.isStarred = isStared;
	}

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_repository_detail);
	}

	@Override
	protected void initView() {
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		viewpager.setOffscreenPageLimit(3);

		indicatorView = (IndicatorView) findViewById(R.id.indicator);

		indicatorView.add(R.string.code, R.drawable.common_code_white)
				.add(R.string.events_l, R.drawable.tab_news_white)
				.add(R.string.commit, R.drawable.common_commit_white)
				.add(R.string.issues, R.drawable.circle_issue_white);

		indicatorView.bind(viewpager);
		viewpager.setOnPageChangeListener(indicatorView);
		indicatorView
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						// TODO Auto-generated method stub
						if (position != indicatorView.getCurrentPosition()) {
							GLog.sysout("hide:"
									+ indicatorView.getCurrentPosition()
									+ ",show:" + position);
							hideHeaderView(fragments.get(indicatorView
									.getCurrentPosition()));
							showRefreshHeader(fragments.get(position));
							fragments.get(position).refreshPopup();
						}
						refreshStarPopup(position);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub

					}
				});

	}

	public void onMenu(int id) {
		switch (id) {
		case R.string.contributors:
			IntentUtils.create(context, ContributorsActivity.class)
					.putExtra(EXTRA_REPOSITORY, repository).start();
			break;
		case R.string.star:
			new StarTask(context, isStarred, repository)
					.execute(GitHubApplication.getClient());
			break;
		default:
			ToastUtils.show(context, getResources().getString(id));
			break;
		}
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		repository = (Repository) getIntent().getSerializableExtra(
				EXTRA_REPOSITORY);
		titlebar.setLeftLayout(repository.getOwner().getAvatarUrl(),
				repository.getName(), repository.getOwner().getLogin());
		fragments.add(new RepositoryCodeFragment());
		fragments.add(new RepositoryEventFragment());
		fragments.add(new RepositoryCommitFragment());
		fragments.add(new RepositoryIssuesFragment());

		adapter = new RepositoryPageAdapter(getSupportFragmentManager());
		viewpager.setAdapter(adapter);
		initStar();
	}

	private void refreshStarPopup(int position) {
		StatusPopWindowItem item = null;
		if (position == 3)
			item = getTitlebar().getStatusPopup().getAction(2);
		else
			item = getTitlebar().getStatusPopup().getAction(0);
		if (isStarred)
			item.mTitle = getResources().getString(R.string.unstar);
		else
			item.mTitle = getResources().getString(R.string.star);
	}

	private void initStar() {
		new BaseAsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO Auto-generated method stub
				WatcherService service = new WatcherService(
						GitHubApplication.getClient());
				try {
					return service.isWatching(repository);
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}

			@Override
			protected void onPostExecute(Boolean result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				isStarred = result;
				refreshStarPopup(indicatorView.getCurrentPosition());
				GLog.sysout("isStarred = " + isStarred);
			}

		}.execute();
	}

	private class RepositoryPageAdapter extends FragmentStatePagerAdapter {

		public RepositoryPageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			if (fragments == null)
				return 1;
			return fragments.size();
		}

		@Override
		public int getItemPosition(Object object) {
			return FragmentStatePagerAdapter.POSITION_NONE;
		}
	}

	@Override
	public void onBackPressed() {
		if (viewpager.getCurrentItem() == 0) {
			if (((RepositoryCodeFragment) fragments.get(0)).onBackPressed())
				super.onBackPressed();
		} else
			super.onBackPressed();
	}

	@Override
	protected void onleftLayout() {
		finish();
	}

}