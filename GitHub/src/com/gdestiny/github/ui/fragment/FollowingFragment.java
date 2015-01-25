package com.gdestiny.github.ui.fragment;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gdestiny.github.R;
import com.gdestiny.github.async.SimpleUpdateTask;
import com.gdestiny.github.utils.TestUtils;
import com.gdestiny.github.utils.ToastUtils;

public class FollowingFragment extends BaseLoadFragment {

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.currentView = inflater.inflate(R.layout.frag_following, null);
		return this.currentView;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		this.pullToRefreshLayout = (PullToRefreshLayout) this.currentView
				.findViewById(R.id.pull_refresh_layout);
		ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable()
				.listener(this).setup(pullToRefreshLayout);
		// initStatusPopup(((BaseFragmentActivity) context).getTitlebar());
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		ToastUtils.show(context, "FollowingFragment onRefreshStarted");
		new SimpleUpdateTask(new SimpleUpdateTask.UpdateListener() {

			@Override
			public void onSuccess() {
				ToastUtils.show(context, "FollowingFragment onSuccess");
				dismissProgress();
			}

			@Override
			public void onPrev() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onExcute() {
				// TODO Auto-generated method stub
				TestUtils.interrupt(9000);
			}
		}).execute();
	}

}
