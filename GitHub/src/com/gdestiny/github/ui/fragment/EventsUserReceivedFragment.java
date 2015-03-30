package com.gdestiny.github.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.GLog;

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
		// ��ֹ������ҳ���ص�
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
		title.showRightBtn();
		title.getRightBtn().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isLoading())
					onRefreshStarted(v);
				else
					GLog.sysout("no need to refresh");
			}
		});
	}

}
