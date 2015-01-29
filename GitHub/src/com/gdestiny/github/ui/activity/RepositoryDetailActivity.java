package com.gdestiny.github.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Repository;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.gdestiny.github.R;
import com.gdestiny.github.ui.fragment.BaseLoadFragment;
import com.gdestiny.github.ui.fragment.FollowerFragment;
import com.gdestiny.github.ui.fragment.RepositoryCodeFragment;
import com.gdestiny.github.ui.fragment.RepositoryCommitFragment;
import com.gdestiny.github.ui.view.IndicatorView;
import com.gdestiny.github.utils.GLog;

public class RepositoryDetailActivity extends BaseFragmentActivity {

	public static final String EXTRA_REPOSITORY = "repository";

	private Repository repository;
	private ViewPager viewpager;
	private RepositoryPageAdapter adapter;

	private IndicatorView indicatorView;

	private List<BaseLoadFragment<?, ?>> fragments = new ArrayList<BaseLoadFragment<?, ?>>();

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_repository_detail);
		getSwipeBackLayout().setEdgeSize(10);
	}

	@Override
	protected void initView() {
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		viewpager.setOffscreenPageLimit(3);

		indicatorView = (IndicatorView) findViewById(R.id.indicator);

		indicatorView.add("code", R.drawable.common_code_white)
				.add("news", R.drawable.tab_news_white)
				.add("commit", R.drawable.common_commit_white)
				.add("issues", R.drawable.circle_issue_white);

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
						}
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

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		repository = (Repository) getIntent().getSerializableExtra(
				EXTRA_REPOSITORY);
		titlebar.setLeftLayout(repository.getOwner().getAvatarUrl(),
				repository.getName(), repository.getOwner().getLogin());

		fragments.add(new RepositoryCodeFragment());
		fragments.add(new FollowerFragment());
		fragments.add(new RepositoryCommitFragment());
		fragments.add(new FollowerFragment());

		adapter = new RepositoryPageAdapter(getSupportFragmentManager());
		viewpager.setAdapter(adapter);
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
