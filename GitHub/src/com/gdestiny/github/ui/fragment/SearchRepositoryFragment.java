package com.gdestiny.github.ui.fragment;

import java.util.List;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.SearchRepository;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.SimpleUpdateResultTask;
import com.gdestiny.github.abstracts.fragment.BaseLoadFragment;
import com.gdestiny.github.adapter.SearchRepositoryAdapter;
import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.async.refresh.RefreshRepositoryTask;
import com.gdestiny.github.ui.activity.RepositoryDetailActivity;
import com.gdestiny.github.ui.view.MoreListView;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.CacheUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.IntentUtils;
import com.google.gson.reflect.TypeToken;

public class SearchRepositoryFragment extends
		BaseLoadFragment<String, List<SearchRepository>> {

	private SearchRepositoryAdapter adapter;
	private String query;

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_search_list,
				R.id.pull_refresh_layout);
	}

	@Override
	protected void initView() {
		MoreListView list = (MoreListView) findViewById(R.id.list);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				new RefreshRepositoryTask(context, adapter.getItem(position)) {

					@Override
					public void onSuccess(Repository result) {
						IntentUtils.start(context,
								RepositoryDetailActivity.class,
								Constants.Extra.REPOSITORY, result);
					}
				}.execute();
			}
		});
		adapter = new SearchRepositoryAdapter(context);
		list.setAdapter(adapter);
	}

	@Override
	protected void initData() {
		new SimpleUpdateResultTask<List<SearchRepository>>(
				new SimpleUpdateResultTask.UpdateListener<List<SearchRepository>>() {

					@Override
					public void onPrev() {

					}

					@Override
					public List<SearchRepository> onExcute() {
						if (CacheUtils.contain(getCacheName())) {
							List<SearchRepository> list = CacheUtils
									.getCacheObject(
											getCacheName(),
											new TypeToken<List<SearchRepository>>() {
											}.getType());
							return list;
						}
						return null;
					}

					@Override
					public void onSuccess(List<SearchRepository> result) {
						if (result != null)
							adapter.setData(result);
					}
				}).execute();
	}

	@Override
	public String getCacheName() {
		return CacheUtils.NAME.SEARCH_REPO;
	}

	@Override
	public List<SearchRepository> onBackground(String params) throws Exception {
		List<SearchRepository> list = null;
		if (TextUtils.isEmpty(params)) {
			throw new Exception("search nothing");
		}
		if (!params.equals(query))
			query = params;
		list = GitHubConsole.getInstance().searchRepositories(query);
		CacheUtils.cacheObject(getCacheName(), list);
		return list;
	}

	@Override
	public void onSuccess(List<SearchRepository> result) {
		super.onSuccess(result);
		if (result == null || result.size() == 0) {
		}
		adapter.setData(result);
	}

	@Override
	public void onRefresh() {
		execute(query);
	}

	@Override
	public void initStatusPopup(TitleBar title) {

	}

}
