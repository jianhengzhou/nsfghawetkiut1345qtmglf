package com.gdestiny.github.abstracts.async;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Issue;

import android.content.Context;

import com.gdestiny.github.async.GitHubConsole;

public abstract class IssueRefreshTask extends DialogTask<Void, Issue> {

	private int issueNumber;
	private IRepositoryIdProvider repository;

	public IssueRefreshTask(Context context, IRepositoryIdProvider repository,
			int issueNumber) {
		super(context);
		this.issueNumber = issueNumber;
		this.repository = repository;
		this.setLoadingMessage("issue#" + issueNumber);
	}

	@Override
	public Issue onBackground(Void params) throws Exception {
		return GitHubConsole.getInstance().getIssue(repository, issueNumber);
	}

}
