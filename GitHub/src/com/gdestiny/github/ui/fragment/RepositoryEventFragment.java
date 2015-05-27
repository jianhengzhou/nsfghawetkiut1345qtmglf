package com.gdestiny.github.ui.fragment;

import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.event.Event;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.EventAdapter;
import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.ui.activity.RepositoryDetailActivity;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.CacheUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.google.gson.reflect.TypeToken;

public class RepositoryEventFragment extends AbstractEventFragment {

	private Repository repository;

	@Override
	protected void initData() {
		repository = (Repository) context.getIntent().getSerializableExtra(
				Constants.Extra.REPOSITORY);
		super.initData();
		List<Event> list = CacheUtils.getCacheObject(getCacheName(),
				new TypeToken<List<Event>>() {
				}.getType());
		if (list != null) {
			setDatas(list);
			((EventAdapter) getBaseAdapter()).setDatas(list);
		}

	}

	@Override
	public String getCacheName() {
		return CacheUtils.DIR.EVENT + repository.getName() + "#"
				+ CacheUtils.NAME.LIST_EVENTS;
	}

	@Override
	public void newPageData(Void params) {
		setDataPage(GitHubConsole.getInstance().pageEvents(repository));
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
						onRefresh();
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
