package com.gdestiny.github.async;

import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;

import android.content.Context;

import com.gdestiny.github.R;
import com.gdestiny.github.async.abstracts.DialogTask;
import com.gdestiny.github.utils.ToastUtils;

public class CommitCommentTask extends DialogTask<GitHubClient, CommitComment> {

	private Repository repository;
	private String sha;

	private CommitComment comment;

	public CommitCommentTask(Context context, Repository repository,
			String sha, CommitComment comment) {
		super(context);
		this.repository = repository;
		this.sha = sha;
		this.comment = comment;
		this.setLoadingMessage(R.string.committing_comment);
	}

	@Override
	public CommitComment onBackground(GitHubClient params) throws Exception {
		CommitService service = new CommitService(params);

		if (sha == null) {
			if (repository == null) {
				throw new IllegalArgumentException(
						"the repository  can not be null");
			}
			return service.editComment(repository, comment);
		} else {
			if (repository == null || sha == null) {
				throw new IllegalArgumentException(
						"the repository and sha can not be null");
			}
			return service.addComment(repository, sha, comment);
		}
	}

	@Override
	public void onSuccess(CommitComment comment) {
		ToastUtils.show(context, R.string.commen_succeed);
	}

}
