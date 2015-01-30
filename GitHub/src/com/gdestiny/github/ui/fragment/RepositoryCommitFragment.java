package com.gdestiny.github.ui.fragment;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.CommitAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.activity.RepositoryDetailActivity;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;

public class RepositoryCommitFragment extends
		BaseLoadPageFragment<RepositoryCommit, GitHubClient> {

	private Repository repository;

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_repository_commit,
				R.id.pull_refresh_layout, R.id.list);
	}

	@Override
	protected void initView() {
	}

	@Override
	protected void initData() {
		repository = (Repository) context.getIntent().getSerializableExtra(
				RepositoryDetailActivity.EXTRA_REPOSITORY);
		execute(GitHubApplication.getClient());
		// 防止与其他页面重叠
		getPullToRefreshLayout().getHeaderTransformer()
				.setProgressbarVisibility(View.GONE);
	}

	@Override
	protected void initStatusPopup(TitleBar title) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefreshStarted(View view) {
		super.onRefreshStarted(view);
		execute(GitHubApplication.getClient());
	}

	@Override
	public void newListAdapter() {
		CommitAdapter commitAdapter = new CommitAdapter(context);
		commitAdapter.setDatas(getDatas());
		setBaseAdapter(commitAdapter);
	}

	@Override
	public void newPageData(GitHubClient params) {
		CommitService service = new CommitService(params);
		setDataPage(service
				.pageCommits(repository, Constants.DEFAULT_PAGE_SIZE));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

}
