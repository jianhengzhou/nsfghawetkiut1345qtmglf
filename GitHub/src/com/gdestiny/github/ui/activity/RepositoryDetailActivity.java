package com.gdestiny.github.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Repository;

import com.gdestiny.github.R;
import com.gdestiny.github.ui.fragment.BaseLoadFragment;
import com.gdestiny.github.ui.fragment.FollowerFragment;
import com.gdestiny.github.ui.fragment.FollowingFragment;
import com.gdestiny.github.ui.fragment.RepositoryCodeFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class RepositoryDetailActivity extends BaseFragmentActivity {

	public static final String EXTRA_REPOSITORY = "repository";

	private Repository repository;
	private ViewPager viewpager;
	private RepositoryPageAdapter adapter;

	private List<BaseLoadFragment> fragments = new ArrayList<BaseLoadFragment>();

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_repository_detail);
	}

	@Override
	protected void initView() {
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		viewpager.setOffscreenPageLimit(3);
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
		fragments.add(new FollowingFragment());
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
