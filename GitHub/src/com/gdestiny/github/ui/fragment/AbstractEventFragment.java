package com.gdestiny.github.ui.fragment;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.event.Event;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.EventAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.GLog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbstractEventFragment extends
		BaseLoadPageFragment<Event, GitHubClient> {

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_news, R.id.pull_refresh_layout,
				R.id.list);
	}

	@Override
	public void newListAdapter() {
		EventAdapter eventAdapter = new EventAdapter(context);
		eventAdapter.setDatas(getDatas());
		setBaseAdapter(eventAdapter);
	}

	@Override
	public void onRefreshStarted(View view) {
		super.onRefreshStarted(view);
		execute(GitHubApplication.getClient());
	}

	@Override
	protected void initData() {
		execute(GitHubApplication.getClient());
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initStatusPopup(TitleBar title) {
		title.showRightBtn();
		title.getRightBtn().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isLoading())
					onRefreshStarted(v);
				else
					GLog.sysout("no need to refresh");
			}
		});
	}
}
