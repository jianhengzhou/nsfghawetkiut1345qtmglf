package com.gdestiny.github.async;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;

import android.content.Context;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.ToastUtils;

public class DeleteTask extends DialogTask<GitHubClient, Boolean> {

	private Repository repository;
	private Comment comment;

	public DeleteTask(Context context, Repository repository, Comment comment) {
		super(context);
		this.repository = repository;
		this.comment = comment;
		this.setTitle(repository.getName());

		this.setLoadingMessage(R.string.deleting);
	}

	@Override
	public void onPrev() {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean onBackground(GitHubClient params) throws Exception {
		IssueService service = new IssueService(params);
		service.deleteComment(repository, comment.getId());
		return true;
	}

	@Override
	public void onSuccess(Boolean result) {
		ToastUtils.show(context, R.string.delete_succeed);
	}

}
