package com.gdestiny.github.async.abstracts;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Repository;

import android.content.Context;

import com.gdestiny.github.async.GitHubConsole;

public abstract class RepositoryRefreshTask extends
		DialogTask<Void, Repository> {

	private IRepositoryIdProvider repository;

	public RepositoryRefreshTask(Context context, final String id) {
		super(context);
		this.repository = new IRepositoryIdProvider() {

			@Override
			public String generateId() {
				return id;
			}
		};
		this.setLoadingMessage(repository.generateId());
	}

	public RepositoryRefreshTask(Context context,
			IRepositoryIdProvider repository) {
		super(context);
		this.repository = repository;
		this.setLoadingMessage(repository.generateId());
	}

	@Override
	public Repository onBackground(Void params) throws Exception {
		return GitHubConsole.getInstance().getRepository(repository);
	}

}
