package com.gdestiny.github.adapter;

import java.util.List;

import com.gdestiny.github.abstracts.fragment.BaseLoadFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class SimplePageAdapter extends FragmentStatePagerAdapter {
	private List<BaseLoadFragment<?, ?>> fragments;

	public SimplePageAdapter(FragmentManager fm) {
		super(fm);
	}

	public SimplePageAdapter(FragmentManager fm,
			List<BaseLoadFragment<?, ?>> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	public List<BaseLoadFragment<?, ?>> getFragments() {
		return fragments;
	}

	public void setFragments(List<BaseLoadFragment<?, ?>> fragments) {
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		if (fragments == null)
			return 0;
		return fragments.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return FragmentStatePagerAdapter.POSITION_NONE;
	}
}