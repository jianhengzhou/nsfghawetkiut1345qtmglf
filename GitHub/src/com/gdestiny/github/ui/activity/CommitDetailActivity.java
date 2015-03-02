package com.gdestiny.github.ui.activity;

import java.util.List;

import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.CommitExpandAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.bean.CommitTree;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.ToastUtils;

public class CommitDetailActivity extends
		BaseLoadFragmentActivity<GitHubClient, CommitTree> {

	private Repository repository;
	private RepositoryCommit commit;

	private ExpandableListView commitExpandList;
	private CommitExpandAdapter commitAdapter;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_commit_detail, R.id.pull_refresh_layout);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		commitExpandList = (ExpandableListView) findViewById(R.id.list);

		commitAdapter = new CommitExpandAdapter(context);
		commitExpandList.setAdapter(commitAdapter);
		commitExpandList.addHeaderView(LayoutInflater.from(context).inflate(
				R.layout.layout_issue_detail, null));
		commitExpandList.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				ToastUtils.show(context, groupPosition + ":" + childPosition);
				return true;
			}
		});
		commitExpandList
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						int groupPosition = (Integer) view
								.getTag(R.id.tag_group); // 参数值是在setTag时使用的对应资源id号
						int childPosition = (Integer) view
								.getTag(R.id.tag_child);
						ToastUtils.show(context, groupPosition + ":"
								+ childPosition);
						return true;
					}
				});
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		repository = (Repository) getIntent().getSerializableExtra(
				Constants.Extra.REPOSITORY);
		commit = (RepositoryCommit) getIntent().getSerializableExtra(
				Constants.Extra.REPOSITORY_COMMIT);
		execute(GitHubApplication.getClient());
	}

	@Override
	public CommitTree onBackground(GitHubClient params) throws Exception {
		// TODO Auto-generated method stub
		CommitService service = new CommitService(params);
		commit = service.getCommit(repository, commit.getSha());
		List<CommitComment> comments = service.getComments(repository,
				commit.getSha());
		for (@SuppressWarnings("unused")
		CommitComment c : comments) {
			// System.out.println(c.getLine());
		}
		CommitTree commitTree = new CommitTree(commit);
		return commitTree;
	}

	@Override
	public void onSuccess(CommitTree result) {
		// TODO Auto-generated method stub
		super.onSuccess(result);
		for (CommitFile cf : result.getCommitFiles()) {
			System.out.println("-----------------------");
			System.out.println("getAdditions:" + cf.getAdditions());
			System.out.println("getBlobUrl:" + cf.getBlobUrl());
			System.out.println("getChanges:" + cf.getChanges());
			System.out.println("getDeletions:" + cf.getDeletions());
			System.out.println("getFilename:" + cf.getFilename());
			System.out.println("getPatch:" + cf.getPatch());
			System.out.println("getRawUrl:" + cf.getRawUrl());
			System.out.println("getSha:" + cf.getSha());
			System.out.println("getStatus:" + cf.getStatus());
		}
		commitAdapter.setCommitTree(result);
	}

	@Override
	protected void onleftLayout() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub

	}

}
