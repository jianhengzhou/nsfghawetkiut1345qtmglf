package com.gdestiny.github.async;

import org.eclipse.egit.github.core.Comment;

import android.content.Context;
import android.view.View;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.ui.dialog.MaterialDialog;
import com.gdestiny.github.utils.ToastUtils;

public class DeleteGistCommentTask extends DialogTask<Void, Boolean> {

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
	public Boolean onBackground(Void params) throws Exception {
		GitHubConsole.getInstance().deleteGistComment(gistId, comment);
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
