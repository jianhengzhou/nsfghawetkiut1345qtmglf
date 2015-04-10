package com.gdestiny.github.async.refresh;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.RepositoryCommit;

import android.content.Context;

import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.async.GitHubConsole;

public abstract class RefreshCommitTask extends
		DialogTask<Void, RepositoryCommit> {

	private String sha;
	private IRepositoryIdProvider repository;

	public RefreshCommitTask(Context context, IRepositoryIdProvider repository,
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
