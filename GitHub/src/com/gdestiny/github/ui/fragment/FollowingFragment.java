package com.gdestiny.github.ui.fragment;

import java.util.List;

import org.eclipse.egit.github.core.User;

import android.text.TextUtils;

import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.CacheUtils;
import com.google.gson.reflect.TypeToken;

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
	public void newListAdapter() {
		List<User> list = CacheUtils.getCacheObject(getCacheName(),
				new TypeToken<List<User>>() {
				}.getType());
		setDatas(list);

		super.newListAdapter();
	}

	@Override
	public String getCacheName() {
		if (TextUtils.isEmpty(user))
			return CacheUtils.NAME.LIST_FOLLOWING;
		return CacheUtils.DIR.USER + user + "@"
				+ CacheUtils.NAME.LIST_FOLLOWING;
	}

	@Override
	protected void initData() {
		super.initData();
	}

	@Override
	public void newPageData(Void params) {
		if (TextUtils.isEmpty(user))
			setDataPage(GitHubConsole.getInstance().pageFollowing());
		else
			setDataPage(GitHubConsole.getInstance().pageFollowing(user));
	}

}
