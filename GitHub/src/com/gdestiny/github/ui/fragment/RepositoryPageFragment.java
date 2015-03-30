package com.gdestiny.github.ui.fragment;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.RepositoryAdapter;
import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.ui.activity.RepositoryDetailActivity;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.IntentUtils;

public class RepositoryPageFragment extends
		BaseLoadPageFragment<Repository, Void> {

	private User user;

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_repository_page,
				R.id.pull_refresh_layout, R.id.list);
	}

	@Override
	public void initStatusPopup(TitleBar title) {
	}

	@Override
	public void newListAdapter() {
		RepositoryAdapter repositoryAdapter = new RepositoryAdapter(context);
		repositoryAdapter.setData(getDatas());
		setBaseAdapter(repositoryAdapter);
	}

	@Override
	public void newPageData(Void params) {
		setDataPage(GitHubConsole.getInstance().pageRepositories(
				user.getLogin()));
	}

	@Override
	public void onRefreshStarted(View view) {
		super.onRefreshStarted(view);
		execute();
	}

	@Override
	protected void initData() {
		user = (User) context.getIntent().getSerializableExtra(
				Constants.Extra.USER);
		execute();
	}

	@Override
	protected void initView() {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		IntentUtils.start(context, RepositoryDetailActivity.class,
				Constants.Extra.REPOSITORY, getDatas().get(position));
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		return false;
	}
}
