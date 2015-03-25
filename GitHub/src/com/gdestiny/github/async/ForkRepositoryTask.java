package com.gdestiny.github.async;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

import com.gdestiny.github.R;
import com.gdestiny.github.async.abstracts.DialogTask;
import com.gdestiny.github.utils.TestUtils;
import com.gdestiny.github.utils.ToastUtils;

import android.content.Context;

public class ForkRepositoryTask extends DialogTask<GitHubClient, Boolean> {

	@SuppressWarnings("unused")
	private Repository repository;

	public ForkRepositoryTask(Context context, Repository repository) {
		super(context);
		this.repository = repository;
		this.setTitle(repository.getName());
		this.setLoadingMessage(R.string.forking);
	}


	@Override
	public Boolean onBackground(GitHubClient params) throws Exception {
		@SuppressWarnings("unused")
		RepositoryService service = new RepositoryService(params);
		// service.forkRepository(repository);
		TestUtils.interrupt(5000);
		return true;
	}

	@Override
	public void onSuccess(Boolean result) {
		ToastUtils.show(context, R.string.fork_succeed);
	}

}
