package com.gdestiny.github.async;

import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.Repository;

import android.content.Context;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.utils.ToastUtils;

public class CommitCommentTask extends DialogTask<Void, CommitComment> {

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
	public CommitComment onBackground(Void params) throws Exception {

		if (sha == null) {
			if (repository == null) {
				throw new IllegalArgumentException(
						"the repository  can not be null");
			}
			return GitHubConsole.getInstance().editCommitComment(repository,
					comment);
		} else {
			if (repository == null || sha == null) {
				throw new IllegalArgumentException(
						"the repository and sha can not be null");
			}
			return GitHubConsole.getInstance().createCommitComment(repository,
					sha, comment);
		}
	}

	@Override
	public void onSuccess(CommitComment comment) {
		ToastUtils.show(context, R.string.commen_succeed);
	}

}
