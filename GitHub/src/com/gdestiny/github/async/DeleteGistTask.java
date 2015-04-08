package com.gdestiny.github.async;

import android.content.Context;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.utils.ToastUtils;

public class DeleteGistTask extends DialogTask<Void, Boolean> {

	private String gistId;

	public DeleteGistTask(Context context, String gistId) {
		super(context);
		this.gistId = gistId;
		this.setLoadingMessage(R.string.deleting_gist);
	}

	@Override
	public Boolean onBackground(Void params) throws Exception {
		GitHubConsole.getInstance().deleteGist(gistId);
		return true;
	}

	@Override
	public void onSuccess(Boolean result) {
		ToastUtils.show(context, R.string.delete_succeed);
	}

}
