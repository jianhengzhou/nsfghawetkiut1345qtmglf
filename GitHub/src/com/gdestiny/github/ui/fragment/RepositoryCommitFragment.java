package com.gdestiny.github.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.service.CommitService;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.CommitAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.activity.RepositoryDetailActivity;
import com.gdestiny.github.ui.view.MoreListView;
import com.gdestiny.github.ui.view.MoreListView.OnAutoLoadListener;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.IteratorUtils;

public class RepositoryCommitFragment extends
		BaseLoadFragment<GitHubClient, List<RepositoryCommit>> {

	private Repository repository;
	private MoreListView commitList;
	private CommitAdapter commitAdapter;

	private List<RepositoryCommit> datas = new ArrayList<RepositoryCommit>();

	private PageIterator<RepositoryCommit> commitPage;

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_repository_commit,
				R.id.pull_refresh_layout);
	}

	@Override
	protected void initView() {
		commitList = (MoreListView) findViewById(R.id.list);
		commitAdapter = new CommitAdapter(context);
		commitAdapter.setDatas(datas);
		commitList.setAdapter(commitAdapter);

		commitList.setOnFooterClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				execute(null);
			}
		});
		commitList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
			}
		});
		commitList.setOnAutoLoadListener(new OnAutoLoadListener() {

			@Override
			public void onLoad() {
				execute(null);
			}
		});
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
	public List<RepositoryCommit> onBackground(GitHubClient params)
			throws Exception {
		if (commitPage == null) {
			CommitService service = new CommitService(params);
			commitPage = service.pageCommits(repository,
					Constants.DEFAULT_PAGE_SIZE);
		}
		return IteratorUtils.iteratorNextPage(commitPage);
	}

	@Override
	public void onSuccess(List<RepositoryCommit> result) {
		super.onSuccess(result);
		datas.addAll(result);
		commitAdapter.notifyDataSetChanged();
		commitList.requestLoadingFinish();
		commitList.requestNoMore(result.size() < Constants.DEFAULT_PAGE_SIZE
				|| !commitPage.hasNext());
	}

	@Override
	public void onException(Exception ex) {
		super.onException(ex);
		commitList.requestLoadingFinish();
		commitList.requestNoMore(true);
	}

	@Override
	public void onRefreshStarted(View view) {
		if (datas == null)
			datas = new ArrayList<RepositoryCommit>();
		datas.clear();
		commitPage = null;// 赋空值更新
		execute(GitHubApplication.getClient());
	}

}
