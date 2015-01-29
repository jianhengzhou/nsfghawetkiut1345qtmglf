package com.gdestiny.github.ui.fragment;

import java.util.LinkedHashMap;

import org.eclipse.egit.github.core.client.GitHubClient;

import android.os.Bundle;
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

public class FollowerFragment extends BaseLoadFragment<GitHubClient, String> {

	// menu
	private LinkedHashMap<Integer, Integer> itemmap;
	private StatusPopUpWindow.StatusPopUpWindowItemClickListener menuListener;

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_follower,
				R.id.pull_refresh_layout);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		initStatusPopup(((BaseFragmentActivity) context).getTitlebar());
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		// execute(GitHubApplication.getClient());
	}

	@Override
	public String onBackground(GitHubClient params) throws Exception {
		// TODO Auto-generated method stub
		TestUtils.interrupt(5000);
		return super.onBackground(params);
	}

	@Override
	public void onSuccess(String result) {
		// TODO Auto-generated method stub
		super.onSuccess(result);
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
				noData(true);
			}

			@Override
			public void onPrev() {
				// TODO Auto-generated method stub
				showProgress();
			}

			@Override
			public void onExcute() {
				// TODO Auto-generated method stub
				TestUtils.interrupt(5000);
			}
		}).execute();
	}

}
