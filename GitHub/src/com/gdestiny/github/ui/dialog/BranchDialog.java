package com.gdestiny.github.ui.dialog;

import java.io.IOException;
import java.util.List;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.RepositoryBranch;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

import android.content.Context;

import com.gdestiny.github.R;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.async.GitHubTask;
import com.gdestiny.github.async.GitHubTask.TaskListener;

public class BranchDialog {

	private Context context;
	private MaterialDialog dialog;
	private IRepositoryIdProvider repository;

	public BranchDialog(Context context, IRepositoryIdProvider repository) {
		this.context = context;
		this.repository = repository;
		init();
	}

	private void init() {
		dialog = new MaterialDialog(context);

		dialog.inProgress(context.getResources().getString(
				R.string.loading_branch));
	}

	public BranchDialog show() {
		new GitHubTask<List<RepositoryBranch>>(
				new TaskListener<List<RepositoryBranch>>() {

					@Override
					public void onPrev() {
						if (dialog != null)
							dialog.show();
					}

					@Override
					public List<RepositoryBranch> onExcute(GitHubClient client) {
						// TODO Auto-generated method stub
						RepositoryService service = new RepositoryService(
								GitHubApplication.getClient());
						List<RepositoryBranch> branch = null;
						try {
							branch = service.getBranches(repository);
						} catch (IOException e) {
							e.printStackTrace();
							return null;
						}
						return branch;
					}

					@Override
					public void onSuccess(List<RepositoryBranch> result) {
						dialog.dismiss();
						MaterialDialog branchDialog = new MaterialDialog(
								context).setTitle("Branch");
						for (RepositoryBranch branch : result) {
							branchDialog.addItem(
									R.drawable.common_repository_item,
									branch.getName());
						}
						branchDialog.show();
					}

					@Override
					public void onError() {
						// TODO Auto-generated method stub

					}
				}).execute(GitHubApplication.getClient());
		return this;
	}
}
