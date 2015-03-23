package com.gdestiny.github.async;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.GistService;

import android.content.Context;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.ToastUtils;

public class StarGistTask extends DialogTask<GitHubClient, Boolean> {

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
	public Boolean onBackground(GitHubClient params) throws Exception {
		GistService service = new GistService(params);
		if (isStar)
			service.unstarGist(gistId);
		else
			service.starGist(gistId);
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
