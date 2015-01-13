package com.gdestiny.github.ui.fragment;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseLoadFragment extends BaseFragment implements OnRefreshListener {

	protected View currentView;
	protected PullToRefreshLayout pullToRefreshLayout;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void showProgress() {
		if (pullToRefreshLayout != null && !pullToRefreshLayout.isRefreshing()) {
			pullToRefreshLayout.setRefreshing(true);
		}
	}

	public void dismissProgress() {
		if (pullToRefreshLayout != null && pullToRefreshLayout.isRefreshing()) {
			pullToRefreshLayout.setRefreshing(false);
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub

	}
}
