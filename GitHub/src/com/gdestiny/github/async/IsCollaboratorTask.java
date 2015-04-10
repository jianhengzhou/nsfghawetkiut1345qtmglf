package com.gdestiny.github.async;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CollaboratorService;

import android.content.Context;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.DialogTask;

public abstract class IsCollaboratorTask extends
		DialogTask<GitHubClient, Boolean> {

	private Repository repository;
	private User user;

	public IsCollaboratorTask(Context context, Repository repository, User user) {
		super(context);
		this.repository = repository;
		this.user = user;
		this.setTitle(repository.getName());
		this.setLoadingMessage(R.string.is_collaborator);
	}

	@Override
	public Boolean onBackground(GitHubClient params) throws Exception {
		CollaboratorService service = new CollaboratorService(params);
		try {
			return service.isCollaborator(repository, user.getLogin());
		} catch (Exception e) {
			return false;
		}
	}

}
