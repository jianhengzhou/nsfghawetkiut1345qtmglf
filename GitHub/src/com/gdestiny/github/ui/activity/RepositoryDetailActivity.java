package com.gdestiny.github.ui.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Repository;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseFragmentActivity;
import com.gdestiny.github.abstracts.async.BaseAsyncTask;
import com.gdestiny.github.abstracts.fragment.BaseLoadFragment;
import com.gdestiny.github.adapter.SimplePageAdapter;
import com.gdestiny.github.async.ForkRepositoryTask;
import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.async.StarRepositoryTask;
import com.gdestiny.github.async.refresh.RefreshRepositoryTask;
import com.gdestiny.github.ui.dialog.StatusPopWindowItem;
import com.gdestiny.github.ui.fragment.RepositoryCodeFragment;
import com.gdestiny.github.ui.fragment.RepositoryCommitFragment;
import com.gdestiny.github.ui.fragment.RepositoryEventFragment;
import com.gdestiny.github.ui.fragment.RepositoryIssuesFragment;
import com.gdestiny.github.ui.view.IndicatorView;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.ToastUtils;
import com.gdestiny.github.utils.client.RepositoryUtils;

public class RepositoryDetailActivity extends BaseFragmentActivity {

	private Repository repository;
	private ViewPager viewpager;
	private SimplePageAdapter adapter;

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
					.putExtra(Constants.Extra.REPOSITORY, repository).start();
			break;
		case R.string.star:
			new StarRepositoryTask(context, isStarred, repository) {

				@Override
				public void onSuccess(Boolean result) {
					super.onSuccess(result);
					isStarred = !isStarred;
					refreshStarPopup(indicatorView.getCurrentPosition());
				}
			}.execute();
			break;
		case R.string.fork:
			new ForkRepositoryTask(context, repository).execute();
			break;
		case R.string.share:
			AndroidUtils.share(context, repository.getName(),
					repository.getHtmlUrl());
			break;
		default:
			ToastUtils.show(context, "TODO:" + getResources().getString(id));
			break;
		}
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		repository = (Repository) getIntent().getSerializableExtra(
				Constants.Extra.REPOSITORY);
		if (!android.text.TextUtils.isEmpty(repository.getOwner()
				.getAvatarUrl()) && RepositoryUtils.isComplete(repository))
		// if (repository.getOwner() != null)
		{
			init();
		} else {
			new RefreshRepositoryTask(context, repository) {

				@Override
				public void onSuccess(Repository result) {
					repository = result;
					init();
				}
			}.execute();
		}
	}

	private void init() {
		titlebar.setLeftLayout(repository.getOwner().getAvatarUrl(),
				repository.getName(), repository.getOwner().getLogin());
		fragments.add(new RepositoryCodeFragment());
		fragments.add(new RepositoryCommitFragment());
		fragments.add(new RepositoryEventFragment());

		indicatorView.add(R.string.code, R.drawable.common_code_white)
				.add(R.string.commit, R.drawable.common_commit_white)
				.add(R.string.events_l, R.drawable.tab_news_white);
		if (repository.isHasIssues()) {
			fragments.add(new RepositoryIssuesFragment());
			indicatorView.add(R.string.issues, R.drawable.circle_issue_white);
		}

		adapter = new SimplePageAdapter(getSupportFragmentManager(), fragments);
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
				try {
					return GitHubConsole.getInstance().isWatching(repository);
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