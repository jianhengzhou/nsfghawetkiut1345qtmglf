package com.gdestiny.github.ui.fragment;

import static org.eclipse.egit.github.core.service.IssueService.DIRECTION_DESCENDING;
import static org.eclipse.egit.github.core.service.IssueService.FIELD_DIRECTION;
import static org.eclipse.egit.github.core.service.IssueService.FIELD_FILTER;
import static org.eclipse.egit.github.core.service.IssueService.FIELD_SORT;
import static org.eclipse.egit.github.core.service.IssueService.SORT_UPDATED;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.RepositoryIssue;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.IssueDashboardAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.activity.IssueDetailActivity;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.IntentUtils;

public class IssueDashboardIssueFragment extends
		BaseLoadPageFragment<RepositoryIssue, GitHubClient> {

	private String filterType;
	private Map<String, String> filterData;
	private IssueDashboardAdapter issueAdapter;

	public IssueDashboardIssueFragment(String filterType) {
		this.filterType = filterType;
	}

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_issue_dashboard_issue,
				R.id.pull_refresh_layout, R.id.list);
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {
		filterData = new HashMap<String, String>();
		filterData.put(FIELD_FILTER, filterType);
		filterData.put(FIELD_SORT, SORT_UPDATED);
		filterData.put(FIELD_DIRECTION, DIRECTION_DESCENDING);

		execute(GitHubApplication.getClient());
		if (!filterType.equals(IssueService.FILTER_CREATED)) {
			getPullToRefreshLayout().getHeaderTransformer()
					.setProgressbarVisibility(View.GONE);
		}
	}

	@Override
	public void newListAdapter() {
		issueAdapter = new IssueDashboardAdapter(context);
		issueAdapter.setDatas(getDatas());
		setBaseAdapter(issueAdapter);
	}

	@Override
	public void newPageData(GitHubClient params) {
		IssueService service = new IssueService(params);
		setDataPage(service.pageIssues(filterData, Constants.DEFAULT_PAGE_SIZE));
	}

	@Override
	public void initStatusPopup(TitleBar title) {

	}

	@Override
	public void onResultOk(int requestCode, Intent data) {
		super.onResultOk(requestCode, data);
		if (requestCode == Constants.Request.ISSUE_DETAIL) {
			GLog.sysout("ISSUE_DETAI");
			Issue issue = (Issue) data
					.getSerializableExtra(Constants.Extra.ISSUE);
			int position = data.getIntExtra(Constants.Extra.POSITION, -1);
			if (issue != null && position >= 0) {
				// IssueUtils.assignMain(issueAdapter.getDatas().get(position),
				// issue);
				RepositoryIssue repoIssue = issueAdapter.getDatas().get(
						position);
				repoIssue.setComments(issue.getComments());
				repoIssue.setTitle(issue.getTitle());
				repoIssue.setAssignee(issue.getAssignee());
				repoIssue.setLabels(issue.getLabels());
				repoIssue.setMilestone(issue.getMilestone());
				repoIssue.setBodyHtml(issue.getBodyHtml());

				issueAdapter.getDatas().remove(position);
				issueAdapter.getDatas().add(position, repoIssue);
				issueAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		IntentUtils
				.create(context, IssueDetailActivity.class)
				.putExtra(Constants.Extra.ISSUE, getDatas().get(position))
				.putExtra(Constants.Extra.POSITION, position)
				.putExtra(Constants.Extra.REPOSITORY,
						getDatas().get(position).getRepository())
				.startForResult(this, Constants.Request.ISSUE_DETAIL);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		return false;
	}

	@Override
	public void onRefreshStarted(View view) {
		super.onRefreshStarted(view);
		execute(GitHubApplication.getClient());
	}

}
