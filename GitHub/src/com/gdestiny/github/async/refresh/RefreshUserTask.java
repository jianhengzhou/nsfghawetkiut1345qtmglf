package com.gdestiny.github.async.refresh;

import org.eclipse.egit.github.core.User;

import android.content.Context;

import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.async.GitHubConsole;

public abstract class RefreshUserTask extends DialogTask<Void, User> {

	private String user;

	public RefreshUserTask(Context context, String user) {
		super(context);
		this.user = user;
		this.setLoadingMessage(user);
	}

	@Override
	public User onBackground(Void params) throws Exception {
		return GitHubConsole.getInstance().getUser(user);
	}

}
