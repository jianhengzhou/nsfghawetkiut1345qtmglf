package com.gdestiny.github.async.refresh;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Repository;

import android.content.Context;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.async.GitHubConsole;

public abstract class RefreshRepositoryTask extends
		DialogTask<Void, Repository> {

	private IRepositoryIdProvider repository;

	public RefreshRepositoryTask(Context context, final String id) {
		super(context);
		this.repository = new IRepositoryIdProvider() {

			@Override
			public String generateId() {
				return id;
			}
		};
		this.setLoadingMessage(R.string.loading);
	}

	public RefreshRepositoryTask(Context context,
			IRepositoryIdProvider repository) {
		super(context);
		this.repository = repository;
		this.setLoadingMessage(R.string.loading);
	}

	@Override
	public Repository onBackground(Void params) throws Exception {
		return GitHubConsole.getInstance().getRepository(repository);
	}

}
