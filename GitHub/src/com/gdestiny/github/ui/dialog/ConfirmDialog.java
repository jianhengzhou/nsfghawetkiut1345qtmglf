package com.gdestiny.github.ui.dialog;

import com.gdestiny.github.R;

import android.content.Context;
import android.view.View;

public abstract class ConfirmDialog extends MaterialDialog {

	public ConfirmDialog(Context context, String message) {
		super(context);
		setMessage(message);
		init();
	}

	public ConfirmDialog(Context context, int resId) {
		super(context);
		setMessage(resId);
		init();
	}

	public ConfirmDialog setTitle(String title) {
		setTitle(title);
		return this;
	}

	private void init() {
		setCanceledOnTouchOutside(true).setTitle(R.string.warning)
				.setPositiveButton("ok", new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();
						onOk();
					}
				}).setNegativeButton("cancle", new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();
						onCancle();
					}
				});
	}

	public abstract void onOk();

	public abstract void onCancle();
}
