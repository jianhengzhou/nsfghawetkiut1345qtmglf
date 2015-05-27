package com.gdestiny.github.async;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.utils.ToastUtils;

import android.content.Context;

public class FollowUserTask extends DialogTask<Void, Boolean> {

	private boolean isFollow;
	private String user;

	public FollowUserTask(Context context, boolean isFollow, String user) {
		super(context);
		this.isFollow = isFollow;
		this.user = user;
		this.setTitle(user);
		if (isFollow)
			this.setLoadingMessage(R.string.unfollowing);
		else
			this.setLoadingMessage(R.string.following);
	}

	@Override
	public Boolean onBackground(Void params) throws Exception {
		if (isFollow)
			GitHubConsole.getInstance().unfollow(user);
		else
			GitHubConsole.getInstance().follow(user);
		return true;
	}

	@Override
	public void onSuccess(Boolean result) {
		if (isFollow)
			ToastUtils.show(context, R.string.unfollowing_succeed);
		else
			ToastUtils.show(context, R.string.following_succeed);
	}

}
