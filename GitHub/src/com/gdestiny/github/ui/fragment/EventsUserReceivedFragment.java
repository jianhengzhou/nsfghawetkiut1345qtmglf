package com.gdestiny.github.ui.fragment;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.EventService;

import android.view.View;
import android.widget.AdapterView;

import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;

public class EventsUserReceivedFragment extends AbstractEventFragment {

	@Override
	public void newPageData(GitHubClient params) {
		EventService service = new EventService(params);
		setDataPage(service.pageUserReceivedEvents(GitHubApplication.getUser()
				.getLogin(), false, Constants.DEFAULT_PAGE_SIZE));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initStatusPopup(TitleBar title) {
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
