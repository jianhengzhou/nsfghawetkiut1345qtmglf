package com.gdestiny.github.ui.fragment;

import java.util.List;

import org.eclipse.egit.github.core.event.Event;

import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.CacheUtils;
import com.google.gson.reflect.TypeToken;

public class EventsUserFragment extends AbstractEventFragment {

	private String user;

	public EventsUserFragment(String user) {
		this.user = user;
	}

	@Override
	public void newPageData(Void params) {
		setDataPage(GitHubConsole.getInstance().pageUserEvents(user));
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
		return CacheUtils.DIR.USER + user + "@" + CacheUtils.NAME.LIST_EVENTS;
	}

	@Override
	protected void initData() {
		super.initData();
	}

	@Override
	public void initStatusPopup(TitleBar title) {
		// if(TextUtils.)
		// title.showRightBtn();
		// title.getRightBtn().setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if (!isLoading())
		// onRefreshStarted(v);
		// else
		// GLog.sysout("no need to refresh");
		// }
		// });
	}

}
