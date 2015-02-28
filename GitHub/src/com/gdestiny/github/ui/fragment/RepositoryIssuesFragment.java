package com.gdestiny.github.ui.fragment;

import java.util.LinkedHashMap;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.IssueAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.bean.IssueFilter;
import com.gdestiny.github.ui.activity.IssueDetailActivity;
import com.gdestiny.github.ui.activity.IssueFilterActivity;
import com.gdestiny.github.ui.activity.NewIssueActivity;
import com.gdestiny.github.ui.activity.RepositoryDetailActivity;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.view.ListPopupView;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.IntentUtils;

public class RepositoryIssuesFragment extends
		BaseLoadPageFragment<Issue, GitHubClient> {

	private final int RESULT_FILT = 1;
	private Repository repository;
	private IssueFilter issueFilter = new IssueFilter();
	private IssueAdapter issueAdapter;

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_repository_issues,
				R.id.pull_refresh_layout, R.id.list);
	}

	@Override
	protected void initView() {
		ListPopupView listPopup = (ListPopupView) findViewById(R.id.filter_popup);
		listPopup.bind(getMoreList());
		listPopup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				openFilter();
			}
		});
	}

	private void openFilter() {
		IntentUtils.create(context, IssueFilterActivity.class)
				.putExtra(Constants.Extra.REPOSITORY, repository)
				.putExtra(Constants.Extra.ISSUE_FILTER, issueFilter)
				.startForResult(RepositoryIssuesFragment.this, RESULT_FILT);
	}

	@Override
	protected void initData() {
		repository = (Repository) context.getIntent().getSerializableExtra(
				Constants.Extra.REPOSITORY);
		execute(GitHubApplication.getClient());
		// 防止与其他页面重叠
		getPullToRefreshLayout().getHeaderTransformer()
				.setProgressbarVisibility(View.GONE);
	}

	@Override
	public void initStatusPopup(final TitleBar title) {
		if (itemmap == null) {
			itemmap = new LinkedHashMap<Integer, Integer>();
			itemmap.put(R.string.new_issue, R.drawable.common_add);
			itemmap.put(R.string.filter, R.drawable.common_filter);

			itemmap.put(R.string.star, R.drawable.common_star_grey);
			itemmap.put(R.string.fork, R.drawable.common_branch_grey);
			itemmap.put(R.string.contributors,
					R.drawable.common_own_people_grey);
			itemmap.put(R.string.share, R.drawable.common_share_grey);
			itemmap.put(R.string.refresh, R.drawable.common_status_refresh);
		}
		if (menuListener == null) {
			menuListener = new StatusPopUpWindow.StatusPopUpWindowItemClickListener() {

				@Override
				public void onitemclick(int titleId) {
					GLog.sysout(context.getResources().getString(titleId) + "");
					boolean dismiss = true;
					switch (titleId) {
					case R.string.refresh:
						if (isLoading()) {
							GLog.sysout("update is not complete");
							return;
						}
						onRefreshStarted(null);
						break;
					case R.string.filter:
						openFilter();
						break;
					case R.string.new_issue:
						IntentUtils.create(context, NewIssueActivity.class)
								.start();
						break;
					default:
						((RepositoryDetailActivity) context).onMenu(titleId);
						break;
					}
					if (dismiss)
						title.dissmissStatus();
				}
			};
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_FILT) {
			System.out.println("RESULT_FILT");
			if (resultCode == Activity.RESULT_OK) {
				System.out.println("RESULT_OK");
				IssueFilter filter = (IssueFilter) data
						.getSerializableExtra(Constants.Extra.ISSUE_FILTER);

				if (filter.equals(issueFilter))
					return;
				issueFilter = filter;
				issueAdapter.setOpen(issueFilter.getState().equals(
						IssueService.STATE_OPEN));
				onRefreshStarted(null);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		IntentUtils.create(context, IssueDetailActivity.class)
				.putExtra(Constants.Extra.ISSUE, getDatas().get(position))
				.putExtra(Constants.Extra.REPOSITORY, repository).start();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void newListAdapter() {
		issueAdapter = new IssueAdapter(context);
		issueAdapter.setDatas(getDatas());
		setBaseAdapter(issueAdapter);
	}

	@Override
	public void newPageData(GitHubClient params) {
		IssueService service = new IssueService(params);
		setDataPage(service.pageIssues(repository, issueFilter == null ? null
				: issueFilter.toHashMap(), Constants.DEFAULT_PAGE_SIZE));
	}

	@Override
	public void onRefreshStarted(View view) {
		super.onRefreshStarted(view);
		execute(GitHubApplication.getClient());
	}

}
