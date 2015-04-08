package com.gdestiny.github.async;

import org.eclipse.egit.github.core.User;

import android.content.Context;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.utils.ToastUtils;

public class EditUserTask extends DialogTask<Void, User> {

	private User user;

	public EditUserTask(Context context, User user) {
		super(context);
		this.user = user;
		this.setLoadingMessage(R.string.edit_user);
	}

	@Override
	public User onBackground(Void params) throws Exception {
		// throw new Exception("");
		return GitHubConsole.getInstance().editUser(user);
	}

	@Override
	public void onSuccess(User result) {
		ToastUtils.show(context, R.string.edit_succeed);
	}

}
