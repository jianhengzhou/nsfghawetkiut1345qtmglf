package com.gdestiny.github.ui.fragment;

import java.util.LinkedHashMap;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.EventService;

import android.view.View;
import android.widget.AdapterView;

import com.gdestiny.github.R;
import com.gdestiny.github.ui.activity.RepositoryDetailActivity;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;

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

	@Override
	public void initStatusPopup(final TitleBar title) {
		if (itemmap == null) {
			itemmap = new LinkedHashMap<Integer, Integer>();
			itemmap.put(R.string.star, R.drawable.common_star_grey);
			itemmap.put(R.string.fork, R.drawable.common_branch_grey);
			itemmap.put(R.string.contributors,
					R.drawable.common_own_people_grey);
			itemmap.put(R.string.share, R.drawable.common_share_grey);
			itemmap.put(R.string.refresh, R.drawable.common_status_refresh);
			itemmap.put(R.string.events_l, R.drawable.common_status_refresh);
		}
		if (menuListener == null) {
			menuListener = new StatusPopUpWindow.StatusPopUpWindowItemClickListener() {

				@Override
				public void onitemclick(int titleId) {
					GLog.sysout(context.getResources().getString(titleId) + "");
					boolean dismiss = true;
					switch (titleId) {
					case R.string.refresh:
						if (isLoading()) {
							GLog.sysout("update is not complete");
							return;
						}
						onRefreshStarted(null);
						break;
					default:
						((RepositoryDetailActivity) context).onMenu(titleId);
						break;
					}
					if (dismiss)
						title.dissmissStatus();
				}
			};
		}
	}

}
