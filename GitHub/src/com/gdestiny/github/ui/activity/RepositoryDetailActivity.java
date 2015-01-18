package com.gdestiny.github.ui.activity;

import java.io.IOException;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.Reference;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.Tree;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.DataService;

import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.async.GitHubTask;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.TestUtils;
import com.gdestiny.github.utils.ToastUtils;

import android.os.Bundle;

public class RepositoryDetailActivity extends BaseFragmentActivity {

	public static final String data = "repository";

	private Repository repository;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		repository = (Repository) getIntent().getSerializableExtra(data);
		titlebar.setLeftLayout(repository.getOwner().getAvatarUrl(),
				repository.getName());
		getDetail();
	}

	private void getDetail() {
		new GitHubTask<Tree>(new GitHubTask.TaskListener<Tree>() {

			@Override
			public void onPrev() {
				// TODO Auto-generated method stub

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
				// TODO Auto-generated method stub
				ToastUtils.show(context, "onSuccess");
				IntentUtils.startTest(context, TestUtils.printTree(result));
			}

			@Override
			public void onError() {
				// TODO Auto-generated method stub
				ToastUtils.show(context, "onError");
			}
		}).execute(GitHubApplication.getClient());
	}

	@Override
	protected void onleftLayout() {
		// TODO Auto-generated method stub
		finish();
	}

}
