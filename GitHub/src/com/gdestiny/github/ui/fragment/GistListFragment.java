package com.gdestiny.github.ui.fragment;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.GistService;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.GistAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.activity.GistDetailActivity;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.IntentUtils;

public class GistListFragment extends BaseLoadPageFragment<Gist, GitHubClient> {

	public enum GistType {
		MINE, STAR, PUBLIC
	};

	private GistType type;
	private GistAdapter adapter;

	public GistListFragment(GistType type) {
		this.type = type;
	}

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_gist_list,
				R.id.pull_refresh_layout, R.id.list);
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {
		execute(GitHubApplication.getClient());
		if (type != GistType.MINE) {
			getPullToRefreshLayout().getHeaderTransformer()
					.setProgressbarVisibility(View.GONE);
		}
	}

	@Override
	public void newListAdapter() {
		adapter = new GistAdapter(context);
		adapter.setDatas(getDatas());
		setBaseAdapter(adapter);
	}

	@Override
	public void newPageData(GitHubClient params) {
		GistService service = new GistService(params);
		switch (type) {
		case MINE:
			setDataPage(service.pageGists(GitHubApplication.getUser()
					.getLogin(), Constants.DEFAULT_PAGE_SIZE));
			break;
		case PUBLIC:
			setDataPage(service.pagePublicGists(Constants.DEFAULT_PAGE_SIZE));
			break;
		case STAR:
			setDataPage(service.pageStarredGists(Constants.DEFAULT_PAGE_SIZE));
			break;
		}
	}

	@Override
	public void initStatusPopup(final TitleBar title) {

	}

	@Override
	public void onResultOk(int requestCode, Intent data) {
		super.onResultOk(requestCode, data);
		if (requestCode == Constants.Request.GIST_DETAIL) {
			Gist gist = (Gist) data.getSerializableExtra(Constants.Extra.GIST);
			int position = data.getIntExtra(Constants.Extra.POSITION, -1);
			if (gist == null) {
				getDatas().remove(position);
			} else {
				getDatas().remove(position);
				getDatas().add(position, gist);
			}
			getBaseAdapter().notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		IntentUtils.create(context, GistDetailActivity.class)
				.putExtra(Constants.Extra.GIST, getDatas().get(position))
				.putExtra(Constants.Extra.POSITION, position)
				.startForResult(this, Constants.Request.GIST_DETAIL);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		return false;
	}

	@Override
	public void onRefreshStarted(View view) {
		super.onRefreshStarted(view);
		execute(GitHubApplication.getClient());
	}

}