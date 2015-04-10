package com.gdestiny.github.async.refresh;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Issue;

import android.content.Context;

import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.async.GitHubConsole;

public abstract class RefreshIssueTask extends DialogTask<Void, Issue> {

	private int issueNumber;
	private IRepositoryIdProvider repository;

	public RefreshIssueTask(Context context, IRepositoryIdProvider repository,
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
