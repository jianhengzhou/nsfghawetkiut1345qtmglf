package com.gdestiny.github.ui.fragment;

import android.text.TextUtils;
import android.view.View;

import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.ui.view.TitleBar;

public class FollowingFragment extends AbstractFollowFragment {

	private String user;

	public FollowingFragment(String user) {
		this.user = user;
	}

	public FollowingFragment() {
		this.user = null;
	}

	@Override
	public void initStatusPopup(TitleBar title) {
		if (TextUtils.isEmpty(user))
			super.initStatusPopup(title);
	}

	@Override
	protected void initData() {
		super.initData();
		// 防止与其他页面重叠
		if (!TextUtils.isEmpty(user))
			getPullToRefreshLayout().getHeaderTransformer()
					.setProgressbarVisibility(View.GONE);
	}

	@Override
	public void newPageData(Void params) {
		if (TextUtils.isEmpty(user))
			setDataPage(GitHubConsole.getInstance().pageFollowing());
		else
			setDataPage(GitHubConsole.getInstance().pageFollowing(user));
	}

}
