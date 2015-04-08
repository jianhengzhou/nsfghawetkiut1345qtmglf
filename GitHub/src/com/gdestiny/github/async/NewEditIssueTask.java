package com.gdestiny.github.async;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;

import android.content.Context;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.utils.ToastUtils;

public class NewEditIssueTask extends DialogTask<Void, Issue> {

	private Repository repository;
	private Issue issue;

	private boolean isNew = false;

	public NewEditIssueTask(Context context, Repository repository, Issue issue) {
		super(context);
		this.repository = repository;
		this.issue = issue;
		this.setTitle(repository.getName());
		if (issue.getNumber() == 0) {
			isNew = true;
			this.setLoadingMessage(R.string.newing_issue);
		} else {
			isNew = false;
			this.setLoadingMessage(R.string.editing_issue);
		}
	}

	@Override
	public Issue onBackground(Void params) throws Exception {
		if (isNew)
			return GitHubConsole.getInstance().createIssue(repository, issue);
		else
			return GitHubConsole.getInstance().editIssue(repository, issue);

	}

	@Override
	public void onSuccess(Issue result) {
		if (isNew)
			ToastUtils.show(context, R.string.newing_issue_succeed);
		else
			ToastUtils.show(context, R.string.editing_issue_succeed);
	}

}
