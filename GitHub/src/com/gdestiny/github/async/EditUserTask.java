package com.gdestiny.github.async;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;

import android.content.Context;

import com.gdestiny.github.R;
import com.gdestiny.github.async.abstracts.DialogTask;
import com.gdestiny.github.utils.ToastUtils;

public class EditUserTask extends DialogTask<GitHubClient, User> {

	private User user;

	public EditUserTask(Context context, User user) {
		super(context);
		this.user = user;
		this.setLoadingMessage(R.string.edit_user);
	}

	@Override
	public User onBackground(GitHubClient params) throws Exception {
		// throw new Exception("");
		UserService service = new UserService(params);
		return service.editUser(user);
	}

	@Override
	public void onSuccess(User result) {
		ToastUtils.show(context, R.string.edit_succeed);
	}

}
