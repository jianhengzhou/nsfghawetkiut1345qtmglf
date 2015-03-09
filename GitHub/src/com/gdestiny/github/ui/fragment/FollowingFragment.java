package com.gdestiny.github.ui.fragment;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;

import android.text.TextUtils;
import android.view.View;

import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;

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
	public void newPageData(GitHubClient params) {
		UserService service = new UserService(params);
		if (TextUtils.isEmpty(user))
			setDataPage(service.pageFollowing(Constants.DEFAULT_PAGE_SIZE));
		else
			setDataPage(service
					.pageFollowing(user, Constants.DEFAULT_PAGE_SIZE));
	}

}
