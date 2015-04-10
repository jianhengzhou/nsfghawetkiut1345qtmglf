package com.gdestiny.github.ui.fragment;

import java.util.List;

import org.eclipse.egit.github.core.event.Event;

import android.view.View;

import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.CacheUtils;
import com.gdestiny.github.utils.GLog;
import com.google.gson.reflect.TypeToken;

public class EventsUserReceivedFragment extends AbstractEventFragment {

	private String user;

	public EventsUserReceivedFragment(String user) {
		this.user = user;
	}

	@Override
	public void newPageData(Void params) {
		setDataPage(GitHubConsole.getInstance().pageUserReceivedEvents(user));
	}

	@Override
	protected void initData() {
		super.initData();
		// 防止与其他页面重叠
		// if (!TextUtils.isEmpty(user))
		// getPullToRefreshLayout().getHeaderTransformer()
		// .setProgressbarVisibility(View.GONE);
	}

	@Override
	public void newListAdapter() {
		List<Event> list = CacheUtils.getCacheObject(getCacheName(),
				new TypeToken<List<Event>>() {
				}.getType());
		setDatas(list);

		super.newListAdapter();
	}

	@Override
	public String getCacheName() {
		return CacheUtils.NAME.LIST_EVENTS;
	}

	@Override
	public void initStatusPopup(TitleBar title) {
		title.showRightBtn();
		title.getRightBtn().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isLoading())
					onRefresh();
				else
					GLog.sysout("no need to refresh");
			}
		});
	}

}
