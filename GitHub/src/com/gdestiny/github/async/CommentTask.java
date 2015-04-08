package com.gdestiny.github.async;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;

import android.content.Context;
import android.text.TextUtils;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.utils.ToastUtils;

public class CommentTask extends DialogTask<Void, Comment> {

	private Repository repository;
	private Issue issue;

	private String commentContent;
	private Comment comment;

	public CommentTask(Context context, Repository repository, Issue issue,
			String comment) {
		super(context);
		this.repository = repository;
		this.issue = issue;
		this.commentContent = comment;
		// this.setTitle(repository.getName());
		this.setLoadingMessage(R.string.committing_comment);
	}

	public CommentTask(Context context, Repository repository, Comment comment) {
		super(context);
		this.repository = repository;
		this.comment = comment;
		// this.setTitle(repository.getName());
		this.setLoadingMessage(R.string.committing_comment);
	}

	@Override
	public Comment onBackground(Void params) throws Exception {

		if (comment != null) {
			if (repository == null) {
				throw new IllegalArgumentException(
						"the repository  can not be null");
			}
			return GitHubConsole.getInstance().editIssueComment(repository,
					comment);
		} else {
			if (TextUtils.isEmpty(commentContent.trim())) {
				throw new IllegalAccessException("the content is empty");
			}
			if (repository == null || issue == null) {
				throw new IllegalArgumentException(
						"the repository and issue can not be null");
			}
			return GitHubConsole.getInstance().createIssueComment(repository,
					issue.getNumber(), commentContent);
		}
	}

	@Override
	public void onSuccess(Comment comment) {
		ToastUtils.show(context, R.string.commen_succeed);
	}

}
