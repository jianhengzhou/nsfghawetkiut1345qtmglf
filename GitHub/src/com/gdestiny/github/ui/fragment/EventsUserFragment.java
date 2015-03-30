package com.gdestiny.github.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.ui.view.TitleBar;

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
	protected void initData() {
		super.initData();
		// 防止与其他页面重叠
		if (!TextUtils.isEmpty(user))
			getPullToRefreshLayout().getHeaderTransformer()
					.setProgressbarVisibility(View.GONE);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

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
