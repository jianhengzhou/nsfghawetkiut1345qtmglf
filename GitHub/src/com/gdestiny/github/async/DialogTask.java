package com.gdestiny.github.async;

import android.content.Context;

import com.gdestiny.github.R;
import com.gdestiny.github.ui.dialog.MaterialDialog;
import com.gdestiny.github.utils.ToastUtils;

public abstract class DialogTask<Params, Result> implements
		LoadingTask<Params, Result> {

	protected Context context;
	private MaterialDialog dialog;

	public DialogTask(Context context) {
		this.context = context;
		dialog = new MaterialDialog(context).inProgress();
	}

	public DialogTask<Params, Result> setTitle(int resId) {
		dialog.setTitle(resId);
		return this;
	}

	public DialogTask<Params, Result> setTitle(CharSequence title) {
		dialog.setTitle(title);
		return this;
	}

	public DialogTask<Params, Result> setLoadingMessage(int resId) {
		dialog.setLoadingText(resId);
		return this;
	}

	public DialogTask<Params, Result> setLoadingMessage(String title) {
		dialog.setLoadingText(title);
		return this;
	}

	@Override
	public void onException(Exception ex) {
		ToastUtils.show(context, ex.getMessage());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Params params) {
		new BaseAsyncTask<Params, Void, Result>() {

			@Override
			protected void onPostExecute(Result result) {
				super.onPostExecute(result);
				dialog.dismiss();
				if (result != null) {
					DialogTask.this.onSuccess(result);
				} else {
					DialogTask.this.onException(new Exception(context
							.getResources().getString(R.string.network_error)));
				}
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				dialog.show();
				DialogTask.this.onPrev();
			}

			@Override
			protected Result doInBackground(Params... params) {
				try {
					return DialogTask.this.onBackground(params[0]);
				} catch (Exception e) {
					DialogTask.this.onException(e);
				}
				return null;
			}
		}.execute(params);
	}
}
