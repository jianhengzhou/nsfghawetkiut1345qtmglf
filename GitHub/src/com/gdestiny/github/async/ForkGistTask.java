package com.gdestiny.github.async;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.utils.ToastUtils;

import android.content.Context;

public class ForkGistTask extends DialogTask<Void, Boolean> {

	private String gistId;

	public ForkGistTask(Context context, String gistId) {
		super(context);
		this.gistId = gistId;
		this.setLoadingMessage(R.string.forking_gist);
	}

	@Override
	public Boolean onBackground(Void params) throws Exception {
		GitHubConsole.getInstance().forkGist(gistId);
		return true;
	}

	@Override
	public void onSuccess(Boolean result) {
		ToastUtils.show(context, R.string.fork_succeed);
	}

}
