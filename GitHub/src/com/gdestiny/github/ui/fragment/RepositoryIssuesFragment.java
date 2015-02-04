package com.gdestiny.github.ui.fragment;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.IssueAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.activity.IssueDetailActivity;
import com.gdestiny.github.ui.activity.RepositoryDetailActivity;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.IntentUtils;

public class RepositoryIssuesFragment extends
		BaseLoadPageFragment<Issue, GitHubClient> {

	private Repository repository;

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_repository_issues,
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		IntentUtils
				.create(context, IssueDetailActivity.class)
				.putExtra(IssueDetailActivity.EXTRA_ISSUE,
						getDatas().get(position))
				.putExtra(IssueDetailActivity.EXTRA_IREPOSITORY, repository)
				.start();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void newListAdapter() {
		IssueAdapter issueAdapter = new IssueAdapter(context);
		issueAdapter.setDatas(getDatas());
		setBaseAdapter(issueAdapter);
	}

	@Override
	public void newPageData(GitHubClient params) {
		IssueService service = new IssueService(params);
		setDataPage(service.pageIssues(repository, null,
				Constants.DEFAULT_PAGE_SIZE));
	}

	@Override
	public void onRefreshStarted(View view) {
		super.onRefreshStarted(view);
		execute(GitHubApplication.getClient());
	}

}
