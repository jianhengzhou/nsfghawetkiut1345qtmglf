package com.gdestiny.github.async;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

import android.content.Context;

import com.gdestiny.github.R;

public abstract class RepositoryRefreshTask extends DialogTask<GitHubClient, Repository> {

	private IRepositoryIdProvider repository;

	public RepositoryRefreshTask(Context context, IRepositoryIdProvider repository) {
		super(context);
		this.repository = repository;
		this.setLoadingMessage(R.string.loading);
	}

	@Override
	public Repository onBackground(GitHubClient params) throws Exception {
		RepositoryService service = new RepositoryService(params);
		return service.getRepository(repository);
	}

}
