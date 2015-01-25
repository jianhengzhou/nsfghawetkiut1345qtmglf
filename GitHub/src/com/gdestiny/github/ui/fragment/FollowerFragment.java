package com.gdestiny.github.ui.fragment;

import java.util.LinkedHashMap;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gdestiny.github.R;
import com.gdestiny.github.async.SimpleUpdateTask;
import com.gdestiny.github.ui.activity.BaseFragmentActivity;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.TestUtils;
import com.gdestiny.github.utils.ToastUtils;

public class FollowerFragment extends BaseLoadFragment {

	// menu
	private LinkedHashMap<Integer, Integer> itemmap;
	private StatusPopUpWindow.StatusPopUpWindowItemClickListener menuListener;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.currentView = inflater.inflate(R.layout.frag_follower, null);
		return this.currentView;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		this.pullToRefreshLayout = (PullToRefreshLayout) this.currentView
				.findViewById(R.id.pull_refresh_layout);
		ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable()
				.listener(this).setup(pullToRefreshLayout);
		initStatusPopup(((BaseFragmentActivity) context).getTitlebar());
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
	}

	private void initStatusPopup(final TitleBar title) {
		if (itemmap == null) {
			itemmap = new LinkedHashMap<Integer, Integer>();
			itemmap.put(R.string.app_name, R.drawable.common_status_sort);
			itemmap.put(R.string.followers, R.drawable.common_status_refresh);
		}
		if (menuListener == null) {
			menuListener = new StatusPopUpWindow.StatusPopUpWindowItemClickListener() {

				@Override
				public void onitemclick(int titleId) {
					// TODO Auto-generated method stub
					ToastUtils.show(context,
							context.getResources().getString(titleId));
				}
			};
		}
		title.setStatusItem(context, itemmap, menuListener);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden)
			initStatusPopup(((BaseFragmentActivity) context).getTitlebar());
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		ToastUtils.show(context, "FollowerFragment onRefreshStarted");
		new SimpleUpdateTask(new SimpleUpdateTask.UpdateListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				ToastUtils.show(context, "FollowerFragment onSuccess");
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
