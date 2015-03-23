package com.gdestiny.github.async;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.GistService;

import android.content.Context;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.ToastUtils;

public class DeleteGistTask extends DialogTask<GitHubClient, Boolean> {

	private String gistId;

	public DeleteGistTask(Context context, String gistId) {
		super(context);
		this.gistId = gistId;
		this.setLoadingMessage(R.string.deleting_gist);
	}

	@Override
	public Boolean onBackground(GitHubClient params) throws Exception {
		GistService service = new GistService(params);
		service.deleteGist(gistId);
		return true;
	}

	@Override
	public void onSuccess(Boolean result) {
		ToastUtils.show(context, R.string.delete_succeed);
	}

}
