package com.gdestiny.github.abstracts.async;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.RepositoryCommit;

import android.content.Context;

import com.gdestiny.github.async.GitHubConsole;

public abstract class CommitRefreshTask extends
		DialogTask<Void, RepositoryCommit> {

	private String sha;
	private IRepositoryIdProvider repository;

	public CommitRefreshTask(Context context, IRepositoryIdProvider repository,
			String sha) {
		super(context);
		this.sha = sha;
		this.repository = repository;
		if (sha.length() > 10)
			this.setLoadingMessage("commit " + sha.substring(0, 10));
		else
			this.setLoadingMessage("commit " + sha);
	}

	@Override
	public RepositoryCommit onBackground(Void params) throws Exception {
		return GitHubConsole.getInstance().getCommit(repository, sha);
	}

}
