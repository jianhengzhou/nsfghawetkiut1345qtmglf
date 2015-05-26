package com.gdestiny.github.abstracts.async;

import android.content.Context;

import com.gdestiny.github.ui.dialog.ConfirmDialog;

public abstract class ConfirmDialogTask<Params, Result> extends
		DialogTask<Params, Result> {

	private String message;

	public ConfirmDialogTask(Context context, String message) {
		super(context);
		this.message = message;
	}

	@Override
	public void execute() {
		execute(null);
	}

	@Override
	public void execute(final Params params) {
		new ConfirmDialog(context, message) {

			@Override
			public void onOk() {
				ConfirmDialogTask.super.execute(params);
			}

			@Override
			public void onCancle() {

			}
		}.show();
	}
}
