package com.gdestiny.github.async;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.GistService;

import com.gdestiny.github.R;
import com.gdestiny.github.async.abstracts.DialogTask;
import com.gdestiny.github.utils.TestUtils;
import com.gdestiny.github.utils.ToastUtils;

import android.content.Context;

public class ForkGistTask extends DialogTask<GitHubClient, Boolean> {

	@SuppressWarnings("unused")
	private String gistId;

	public ForkGistTask(Context context, String gistId) {
		super(context);
		this.gistId = gistId;
		this.setLoadingMessage(R.string.forking_gist);
	}

	@Override
	public Boolean onBackground(GitHubClient params) throws Exception {
		@SuppressWarnings("unused")
		GistService service = new GistService(params);
		// service.forkGist(gistId);
		TestUtils.interrupt(5000);
		return true;
	}

	@Override
	public void onSuccess(Boolean result) {
		ToastUtils.show(context, R.string.fork_succeed);
	}

}
