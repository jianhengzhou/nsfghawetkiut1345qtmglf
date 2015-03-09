package com.gdestiny.github.ui.fragment;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.EventService;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;

public class EventsUserFragment extends AbstractEventFragment {

	private String user;

	public EventsUserFragment(String user) {
		this.user = user;
	}

	@Override
	public void newPageData(GitHubClient params) {
		EventService service = new EventService(params);
		setDataPage(service.pageUserEvents(user, false,
				Constants.DEFAULT_PAGE_SIZE));
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
