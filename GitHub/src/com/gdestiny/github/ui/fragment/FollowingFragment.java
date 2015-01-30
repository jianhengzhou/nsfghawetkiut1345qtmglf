package com.gdestiny.github.ui.fragment;

import org.eclipse.egit.github.core.client.GitHubClient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gdestiny.github.R;
import com.gdestiny.github.async.SimpleUpdateTask;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.TestUtils;
import com.gdestiny.github.utils.ToastUtils;

public class FollowingFragment extends BaseLoadFragment<GitHubClient, String> {

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_following,
				R.id.pull_refresh_layout);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		// initStatusPopup(((BaseFragmentActivity) context).getTitlebar());
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		// execute(GitHubApplication.getClient());
	}

	@Override
	protected void initStatusPopup(TitleBar title) {
		// TODO Auto-generated method stub

	}

	@Override
	public String onBackground(GitHubClient params) throws Exception {
		// TODO Auto-generated method stub
		return super.onBackground(params);
	}

	@Override
	public void onSuccess(String result) {
		// TODO Auto-generated method stub
		super.onSuccess(result);
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
				TestUtils.interrupt(9000);
			}
		}).execute();
	}

}
