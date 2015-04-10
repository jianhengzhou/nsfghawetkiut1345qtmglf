package com.gdestiny.github.ui.fragment;

import java.util.List;

import org.eclipse.egit.github.core.User;

import android.text.TextUtils;

import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.CacheUtils;
import com.google.gson.reflect.TypeToken;

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
	public String getCacheName() {
		if (TextUtils.isEmpty(user))
			return CacheUtils.NAME.LIST_FOLLOWER;
		return CacheUtils.DIR.USER + user + "@" + CacheUtils.NAME.LIST_FOLLOWER;
	}

	@Override
	public void newListAdapter() {
		List<User> list = CacheUtils.getCacheObject(getCacheName(),
				new TypeToken<List<User>>() {
				}.getType());
		setDatas(list);

		super.newListAdapter();
	}

	@Override
	protected void initData() {
		super.initData();
		// 防止与其他页面重叠
	}

	@Override
	public void newPageData(Void params) {
		if (TextUtils.isEmpty(user))
			setDataPage(GitHubConsole.getInstance().pageFollowers());
		else
			setDataPage(GitHubConsole.getInstance().pageFollowers(user));
	}

}
