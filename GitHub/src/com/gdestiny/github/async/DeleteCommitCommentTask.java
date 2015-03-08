package com.gdestiny.github.async;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;

import android.content.Context;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.ToastUtils;

public class DeleteCommitCommentTask extends DialogTask<GitHubClient, Boolean> {

	private Repository repository;
	private long commentId;

	public DeleteCommitCommentTask(Context context, Repository repository,
			long commentId) {
		super(context);
		this.repository = repository;
		this.commentId = commentId;
		this.setTitle(repository.getName());

		this.setLoadingMessage(R.string.deleting);
	}

	@Override
	public void onPrev() {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean onBackground(GitHubClient params) throws Exception {
		CommitService service = new CommitService(params);
		service.deleteComment(repository, commentId);
		return true;
	}

	@Override
	public void onSuccess(Boolean result) {
		ToastUtils.show(context, R.string.delete_succeed);
	}

}
