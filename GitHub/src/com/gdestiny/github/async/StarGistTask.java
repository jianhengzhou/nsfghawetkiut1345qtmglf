package com.gdestiny.github.async;

import android.content.Context;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.utils.ToastUtils;

public class StarGistTask extends DialogTask<Void, Boolean> {

	private boolean isStar;
	private String gistId;

	public StarGistTask(Context context, boolean isStar, String gistId) {
		super(context);
		this.isStar = isStar;
		this.gistId = gistId;
		if (isStar)
			this.setLoadingMessage(R.string.unstaring_gist);
		else
			this.setLoadingMessage(R.string.staring_gist);
	}

	@Override
	public Boolean onBackground(Void params) throws Exception {
		if (isStar)
			GitHubConsole.getInstance().unstarGist(gistId);
		else
			GitHubConsole.getInstance().starGist(gistId);
		return true;
	}

	@Override
	public void onSuccess(Boolean result) {
		// TODO Auto-generated method stub
		if (isStar)
			ToastUtils.show(context, R.string.unstar_succeed);
		else
			ToastUtils.show(context, R.string.star_succeed);
	}

}
