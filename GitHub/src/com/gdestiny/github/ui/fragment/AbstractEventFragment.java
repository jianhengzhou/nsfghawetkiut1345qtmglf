package com.gdestiny.github.ui.fragment;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.event.Event;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.EventAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.dialog.MaterialDialog;
import com.gdestiny.github.utils.EventUtils;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.ToastUtils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public abstract class AbstractEventFragment extends
		BaseLoadPageFragment<Event, GitHubClient> {

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_events,
				R.id.pull_refresh_layout, R.id.list);
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
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		Event event = getDatas().get(position);
		MaterialDialog mMaterialDialog = new MaterialDialog(context);
		mMaterialDialog
				.setTitle("Go To")
				// .inProgress("loading")
				.addItem(EventUtils.getAuthorAvatarUrl(event),
						EventUtils.getAuthor(event))
				.addItem(R.drawable.common_repository_item,
						event.getRepo().getName(), false)
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						GLog.sysout(position + "");
						ToastUtils.show(context, position + "");
					}
				}).setCanceledOnTouchOutside(true);
		mMaterialDialog.show();
		return true;
	}
}
