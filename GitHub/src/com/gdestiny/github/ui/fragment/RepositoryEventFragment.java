package com.gdestiny.github.ui.fragment;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.EventService;

import android.view.View;
import android.widget.AdapterView;

import com.gdestiny.github.ui.activity.RepositoryDetailActivity;
import com.gdestiny.github.utils.Constants;

public class RepositoryEventFragment extends AbstractEventFragment {

	private Repository repository;

	@Override
	protected void initData() {
		repository = (Repository) context.getIntent().getSerializableExtra(
				RepositoryDetailActivity.EXTRA_REPOSITORY);
		super.initData();
		// 防止与其他页面重叠
		getPullToRefreshLayout().getHeaderTransformer()
				.setProgressbarVisibility(View.GONE);
	}

	@Override
	public void newPageData(GitHubClient params) {
		EventService service = new EventService(params);
		setDataPage(service.pageEvents(repository, Constants.DEFAULT_PAGE_SIZE));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

}
