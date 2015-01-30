package com.gdestiny.github.ui.fragment;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.EventService;

import android.view.View;
import android.widget.AdapterView;

import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.utils.Constants;

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

}
