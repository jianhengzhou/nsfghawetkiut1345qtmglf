package com.gdestiny.github.ui.dialog;

import java.util.List;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.RepositoryBranch;
import org.eclipse.egit.github.core.client.GitHubClient;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.GitHubTask;
import com.gdestiny.github.abstracts.async.GitHubTask.TaskListener;
import com.gdestiny.github.async.GitHubConsole;

public class BranchDialog {

	private Context context;
	private MaterialDialog dialog;
	private IRepositoryIdProvider repository;
	private List<RepositoryBranch> branchList;
	private OnItemClickListener onItemClickListener;

	public BranchDialog(Context context, IRepositoryIdProvider repository) {
		this.context = context;
		this.repository = repository;
	}

	public BranchDialog show() {
		new GitHubTask<List<RepositoryBranch>>(
				new TaskListener<List<RepositoryBranch>>() {

					@Override
					public void onPrev() {
						dialog = new MaterialDialog(context).inProgress(context
								.getResources().getString(
										R.string.loading_branch));
						dialog.show();
					}

					@Override
					public List<RepositoryBranch> onExcute(GitHubClient client) {
						return GitHubConsole.getInstance()
								.getBranch(repository);
					}

					@Override
					public void onSuccess(List<RepositoryBranch> result) {
						branchList = result;
						dialog.dismiss();
						MaterialDialog branchDialog = new MaterialDialog(
								context)
								.setTitle("Branch")
								.setCanceledOnTouchOutside(true)
								.setOnItemClickListener(
										new OnItemClickListener() {

											@Override
											public void onItemClick(
													AdapterView<?> parent,
													View view, int position,
													long id) {
												if (onItemClickListener != null)
													onItemClickListener
															.onItemClick(
																	parent,
																	view,
																	position,
																	id);
											}
										});
						;
						for (RepositoryBranch branch : result) {
							branchDialog.addItem(R.drawable.common_branch_grey,
									branch.getName(), false);
						}
						branchDialog.show();
					}

					@Override
					public void onError() {
						// TODO Auto-generated method stub

					}
				}).execute();
		return this;
	}

	public List<RepositoryBranch> getBranchList() {
		return branchList;
	}

	public void setBranchList(List<RepositoryBranch> branchList) {
		this.branchList = branchList;
	}

	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public BranchDialog setOnItemClickListener(
			OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
		return this;
	}

}
