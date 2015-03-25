package com.gdestiny.github.async;

import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_COMMENTS;
import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_GISTS;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.GistService;

import android.content.Context;
import android.view.View;

import com.gdestiny.github.R;
import com.gdestiny.github.async.abstracts.DialogTask;
import com.gdestiny.github.ui.dialog.MaterialDialog;
import com.gdestiny.github.utils.ToastUtils;

public class DeleteGistCommentTask extends DialogTask<GitHubClient, Boolean> {

	private Comment comment;
	private String gistId;

	public DeleteGistCommentTask(Context context, String gistId, Comment comment) {
		super(context);
		this.comment = comment;
		this.gistId = gistId;
		this.setLoadingMessage(R.string.deleting);
	}

	@Override
	public void onPrev() {

	}

	@Override
	public Boolean onBackground(GitHubClient params) throws Exception {
		GistService service = new GistService(params);
		// ืํมห
		// service.deleteComment(comment.getId());
		StringBuilder uri = new StringBuilder(SEGMENT_GISTS);
		uri.append('/').append(gistId);
		uri.append(SEGMENT_COMMENTS);
		uri.append('/').append(comment.getId());
		service.getClient().delete(uri.toString());
		return true;
	}

	@Override
	public void onSuccess(Boolean result) {
		ToastUtils.show(context, R.string.delete_succeed);
	}

	@Override
	public void execute(final GitHubClient params) {
		final MaterialDialog materialDialog = new MaterialDialog(context);
		materialDialog.setTitle(R.string.warning)
				.setMessage(R.string.warning_delete)
				.setPositiveButton("ok", new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						materialDialog.dismiss();
						DeleteGistCommentTask.super.execute(params);
					}
				}).setNegativeButton("cancle", new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						materialDialog.dismiss();
					}
				}).show();
	}

}
