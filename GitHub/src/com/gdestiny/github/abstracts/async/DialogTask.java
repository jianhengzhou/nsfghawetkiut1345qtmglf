package com.gdestiny.github.abstracts.async;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.interfaces.LoadingTask;
import com.gdestiny.github.ui.dialog.MaterialDialog;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.ToastUtils;

public abstract class DialogTask<Params, Result> implements
		LoadingTask<Params, Result> {

	protected Context context;
	private MaterialDialog dialog;

	private Handler handler;

	private boolean exception = false;

	public DialogTask(Context context) {
		this.context = context;
		dialog = new MaterialDialog(context).inProgress();

		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				DialogTask.this.onException((Exception) msg.obj);
			}

		};
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
	public void onPrev() {
		dialog.show();
		GLog.sysout("onPrev");
	}

	@Override
	public void onException(Exception ex) {
		if (exception) {
			String message = ex.getMessage();
			if (TextUtils.isEmpty(message))
				message = "Unknow Error";
			ToastUtils.show(context, message);
			exception = false;
		}
	}

	public void execute() {
		execute(null);
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
				// DialogTask.this.onPrev();
				dialog.show();
				exception = false;
			}

			@Override
			protected Result doInBackground(Params... params) {
				try {
					if (params == null)
						return DialogTask.this.onBackground(null);
					return DialogTask.this.onBackground(params[0]);
				} catch (final Exception e) {
					e.printStackTrace();
					exception = true;
					Message msg = handler.obtainMessage();
					msg.obj = e;
					handler.sendMessage(msg);
				}
				return null;
			}
		}.execute(params);
	}
}
