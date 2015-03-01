package com.gdestiny.github.async;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;

import android.content.Context;
import android.text.TextUtils;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.ToastUtils;

public class CommentTask extends DialogTask<GitHubClient, Comment> {

	private Repository repository;
	private Issue issue;

	private String comment;

	public CommentTask(Context context, Repository repository, Issue issue,
			String comment) {
		super(context);
		this.repository = repository;
		this.issue = issue;
		this.comment = comment;
		// this.setTitle(repository.getName());
		this.setLoadingMessage(R.string.committing_comment);
	}

	@Override
	public Comment onBackground(GitHubClient params) throws Exception {
		IssueService service = new IssueService(params);

		if (TextUtils.isEmpty(comment.trim())) {
			throw new IllegalAccessException("the content is empty");
		}
		if (repository == null || issue == null) {
			throw new IllegalArgumentException(
					"the repository and issue can not be null");
		}
		return service.createComment(repository, issue.getNumber(), comment);
	}

	@Override
	public void onSuccess(Comment Comment) {
		ToastUtils.show(context, R.string.commen_succeed);
	}

}
