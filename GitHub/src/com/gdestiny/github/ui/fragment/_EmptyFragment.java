package com.gdestiny.github.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.fragment.BaseLoadFragment;
import com.gdestiny.github.ui.view.TitleBar;

public class _EmptyFragment extends BaseLoadFragment<Void, Void> {

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(inflater, R.layout.frag_todo, R.id.pull_refresh_layout);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initStatusPopup(TitleBar title) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefresh() {
		dismissProgress();
	}

}
