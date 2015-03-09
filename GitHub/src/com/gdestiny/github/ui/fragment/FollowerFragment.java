package com.gdestiny.github.ui.fragment;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;

import android.text.TextUtils;
import android.view.View;

import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;

public class FollowerFragment extends AbstractFollowFragment {

	private String user;

	public FollowerFragment(String user) {
		this.user = user;
	}

	public FollowerFragment() {
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
		// ��ֹ������ҳ���ص�
		if (!TextUtils.isEmpty(user))
			getPullToRefreshLayout().getHeaderTransformer()
					.setProgressbarVisibility(View.GONE);
	}

	@Override
	public void newPageData(GitHubClient params) {
		// TODO Auto-generated method stub
		UserService service = new UserService(params);
		if (TextUtils.isEmpty(user))
			setDataPage(service.pageFollowers(Constants.DEFAULT_PAGE_SIZE));
		else
			setDataPage(service
					.pageFollowers(user, Constants.DEFAULT_PAGE_SIZE));
	}

}
