package com.gdestiny.github.ui.fragment;

import org.eclipse.egit.github.core.event.Event;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.EventAdapter;
import com.gdestiny.github.ui.activity.RepositoryDetailActivity;
import com.gdestiny.github.ui.activity.UserNavigationActivity;
import com.gdestiny.github.ui.dialog.MaterialDialog;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.EventUtils;
import com.gdestiny.github.utils.IntentUtils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public abstract class AbstractEventFragment extends
		BaseLoadPageFragment<Event, Void> {

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
	public void onRefresh() {
		super.onRefresh();
		execute();
	}

	@Override
	protected void initData() {
		execute();
	}

	@Override
	protected void initView() {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newPageData(Void params) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initStatusPopup(TitleBar title) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		final Event event = getDatas().get(position);
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
						switch (position) {
						case 0:
							IntentUtils
									.create(context,
											UserNavigationActivity.class)
									.putExtra(Constants.Extra.USER,
											EventUtils.getEventUser(event))
									.start();
							break;
						case 1:
							IntentUtils
									.create(context,
											RepositoryDetailActivity.class)
									.putExtra(Constants.Extra.REPOSITORY,
											EventUtils.getRepository(event))
									.start();
							break;
						}
					}
				}).setCanceledOnTouchOutside(true);
		mMaterialDialog.show();
		return true;
	}
}
