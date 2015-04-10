package com.gdestiny.github.ui.fragment;

import org.eclipse.egit.github.core.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.fragment.BaseLoadPageFragment;
import com.gdestiny.github.adapter.UserAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.activity.UserNavigationActivity;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.IntentUtils;

public abstract class AbstractFollowFragment extends
		BaseLoadPageFragment<User, Void> {

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_follows,
				R.id.pull_refresh_layout, R.id.list);
	}

	@Override
	public void initStatusPopup(TitleBar title) {
		title.showRightBtn();
		title.getRightBtn().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isLoading())
					onRefresh();
				else
					GLog.sysout("no need to refresh");
			}
		});
	}

	@Override
	public void newListAdapter() {
		UserAdapter userAdapter = new UserAdapter(context);
		userAdapter.setDatas(getDatas());
		setBaseAdapter(userAdapter);
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
		User user = getDatas().get(position);
		if (user.getLogin().equals(GitHubApplication.getUser().getLogin()))
			return;
		IntentUtils.create(context, UserNavigationActivity.class)
				.putExtra(Constants.Extra.USER, user).start();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		return false;
	}
}
