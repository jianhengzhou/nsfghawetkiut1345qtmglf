package com.gdestiny.github.ui.fragment;

import java.io.IOException;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.Reference;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.Tree;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.DataService;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.CodeTreeAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.async.GitHubTask;
import com.gdestiny.github.bean.CodeTree;
import com.gdestiny.github.ui.activity.RepositoryDetailActivity;
import com.gdestiny.github.ui.view.PathView;
import com.gdestiny.github.ui.view.PathView.PathClickListener;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.ToastUtils;

public class RepositoryCodeFragment extends BaseLoadFragment {

	public static final String CODE = "codetree";

	private Repository repository;
	private ListView codeList;
	private CodeTreeAdapter codeAdapter;

	private CodeTree currCodeTree;
	private PathView pathView;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.currentView = inflater
				.inflate(R.layout.frag_repository_code, null);
		return this.currentView;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		this.pullToRefreshLayout = (PullToRefreshLayout) this.currentView
				.findViewById(R.id.pull_refresh_layout);
		ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable()
				.listener(this).setup(pullToRefreshLayout);
		codeList = (ListView) this.currentView.findViewById(R.id.list);
		codeAdapter = new CodeTreeAdapter(context);
		codeList.setAdapter(codeAdapter);
		codeList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position < currCodeTree.getTreeCount()) {
					currCodeTree = currCodeTree.subTree.get(position);
					codeAdapter.setCodeTree(currCodeTree);
					codeList.setSelection(0);
					pathView.add(currCodeTree.currEntry.getPath());
				}
			}
		});

		pathView = (PathView) this.currentView.findViewById(R.id.pathview);
		pathView.setPathListener(new PathClickListener() {

			@Override
			public void onPathClick(String path) {
				setCodeData(path);
			}

			@Override
			public void onRootClick() {
				setCodeData(CodeTree.ROOT);
			}
		});
	}

	@Override
	protected void initData() {
		repository = (Repository) context.getIntent().getSerializableExtra(
				RepositoryDetailActivity.data);
		getDetail();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putSerializable(CODE, currCodeTree);
	}

	private void getDetail() {
		new GitHubTask<Tree>(new GitHubTask.TaskListener<Tree>() {

			@Override
			public void onPrev() {
				showProgress();
			}

			@Override
			public Tree onExcute(GitHubClient client) {
				// TODO Auto-generated method stub
				DataService dataService = new DataService(client);
				Tree tree = null;
				try {
					GLog.sysout(repository.getMasterBranch());
					Reference ref = dataService.getReference(repository,
							"heads/" + repository.getMasterBranch());
					Commit commit = dataService.getCommit(repository, ref
							.getObject().getSha());
					tree = dataService.getTree(repository, commit.getTree()
							.getSha(), true);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return tree;
			}

			@Override
			public void onSuccess(Tree result) {
				dismissProgress();
				ToastUtils.show(context, "onSuccess");
				currCodeTree = CodeTree.toCodeTree(result);
				codeAdapter.setCodeTree(currCodeTree);
			}

			@Override
			public void onError() {
				dismissProgress();
				ToastUtils.show(context, "onError");
			}
		}).execute(GitHubApplication.getClient());
	}

	public boolean onBackPressed() {
		if (currCodeTree.name.equals(CodeTree.ROOT))
			return true;
		setCodeData(currCodeTree.parent);
		return false;
	}

	private void setCodeData(String path) {
		currCodeTree = CodeTree.allFolder.get(path);
		codeAdapter.setCodeTree(currCodeTree);
		codeList.setSelection(0);
		// ¸üÐÂpathview
		if (currCodeTree.currEntry == null)
			pathView.resetPath(CodeTree.ROOT);
		else
			pathView.resetPath(currCodeTree.currEntry.getPath());
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub

	}

}
