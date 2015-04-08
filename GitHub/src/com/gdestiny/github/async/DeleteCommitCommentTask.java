package com.gdestiny.github.async;

import org.eclipse.egit.github.core.Repository;

import android.content.Context;
import android.view.View;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.ui.dialog.MaterialDialog;
import com.gdestiny.github.utils.ToastUtils;

public class DeleteCommitCommentTask extends DialogTask<Void, Boolean> {

	private Repository repository;
	private long commentId;

	public DeleteCommitCommentTask(Context context, Repository repository,
			long commentId) {
		super(context);
		this.repository = repository;
		this.commentId = commentId;
		// this.setTitle(repository.getName());

		this.setLoadingMessage(R.string.deleting);
	}

	@Override
	public void onPrev() {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean onBackground(Void params) throws Exception {
		GitHubConsole.getInstance().deleteCommitComment(repository, commentId);
		return true;
	}

	@Override
	public void onSuccess(Boolean result) {
		ToastUtils.show(context, R.string.delete_succeed);
	}

	@Override
	public void execute(final Void params) {
		final MaterialDialog materialDialog = new MaterialDialog(context);
		materialDialog.setTitle(R.string.warning)
				.setMessage(R.string.warning_delete)
				.setPositiveButton("ok", new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						materialDialog.dismiss();
						DeleteCommitCommentTask.super.execute(params);
					}
				}).setNegativeButton("cancle", new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						materialDialog.dismiss();
					}
				}).show();
	}
}
