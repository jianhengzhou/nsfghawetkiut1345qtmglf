package com.gdestiny.github.ui.fragment;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.RepositoryAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.activity.RepositoryDetailActivity;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.IntentUtils;

public class RepositoryPageFragment extends
		BaseLoadPageFragment<Repository, GitHubClient> {

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
	public void newPageData(GitHubClient params) {
		// TODO Auto-generated method stub
		RepositoryService service = new RepositoryService(params);
		setDataPage(service.pageRepositories(user.getLogin(),
				Constants.DEFAULT_PAGE_SIZE));
	}

	@Override
	public void onRefreshStarted(View view) {
		super.onRefreshStarted(view);
		execute(GitHubApplication.getClient());
	}

	@Override
	protected void initData() {
		user = (User) context.getIntent().getSerializableExtra(
				Constants.Extra.USER);
		execute(GitHubApplication.getClient());
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
