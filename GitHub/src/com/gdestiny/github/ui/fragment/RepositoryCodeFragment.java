package com.gdestiny.github.ui.fragment;

import java.io.IOException;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.Reference;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.Tree;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.DataService;

import android.os.Bundle;
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
import com.gdestiny.github.ui.activity.CodeFileActivity;
import com.gdestiny.github.ui.activity.RepositoryDetailActivity;
import com.gdestiny.github.ui.view.PathView;
import com.gdestiny.github.ui.view.PathView.PathClickListener;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.ToastUtils;

public class RepositoryCodeFragment extends BaseLoadFragment {

	public static final String EXTRA_CODE = "codetree";

	private Repository repository;

	private ListView codeList;
	private CodeTreeAdapter codeAdapter;

	private CodeTree currCodeTree;
	private PathView pathView;

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_repository_code,
				R.id.pull_refresh_layout);
		if (savedInstanceState != null) {
			currCodeTree = (CodeTree) savedInstanceState
					.getSerializable(EXTRA_CODE);

		}
	}

	@Override
	protected void initView() {

		codeList = (ListView) findViewById(R.id.list);
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
				} else {
					IntentUtils
							.create(context, CodeFileActivity.class)
							.putExtra(
									CodeFileActivity.EXTRA_CODE_ENTRY,
									currCodeTree.subEntry.get(position
											- currCodeTree.getTreeCount()))
							.putExtra(CodeFileActivity.EXTRA_CODE_REPOSITORY,
									repository).start();
				}
			}
		});

		pathView = (PathView) findViewById(R.id.pathview);
		pathView.setPathListener(new PathClickListener() {

			@Override
			public void onPathClick(String path) {
				setCodeData(path);
			}
		});
	}

	@Override
	protected void initData() {
		repository = (Repository) context.getIntent().getSerializableExtra(
				RepositoryDetailActivity.EXTRA_REPOSITORY);
		if (currCodeTree == null)
			getCodeDetail();
		else {
			codeAdapter.setCodeTree(currCodeTree);
			if (!currCodeTree.name.equals(CodeTree.ROOT))
				pathView.resetView(currCodeTree.currEntry.getPath());
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(EXTRA_CODE, currCodeTree);
	}

	private void getCodeDetail() {
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
				currCodeTree = CodeTree.toCodeTree(result);
				codeAdapter.setCodeTree(currCodeTree);
			}

			@Override
			public void onError() {
				dismissProgress();
				ToastUtils.show(context,
						getResources().getString(R.string.network_error));
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
		getCodeDetail();
	}

}
