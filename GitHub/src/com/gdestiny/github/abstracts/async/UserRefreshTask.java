package com.gdestiny.github.abstracts.async;

import org.eclipse.egit.github.core.User;

import android.content.Context;

import com.gdestiny.github.async.GitHubConsole;

public abstract class UserRefreshTask extends DialogTask<Void, User> {

	private String user;

	public UserRefreshTask(Context context, String user) {
		super(context);
		this.user = user;
		this.setLoadingMessage(user);
	}

	@Override
	public User onBackground(Void params) throws Exception {
		return GitHubConsole.getInstance().getUser(user);
	}

}
