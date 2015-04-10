package com.gdestiny.github.ui.fragment;

import java.util.List;

import org.eclipse.egit.github.core.User;

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
import com.gdestiny.github.adapter.SearchUserAdapter;
import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.async.refresh.RefreshUserTask;
import com.gdestiny.github.bean.SearchUser;
import com.gdestiny.github.ui.activity.UserNavigationActivity;
import com.gdestiny.github.ui.view.MoreListView;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.CacheUtils;
import com.gdestiny.github.utils.CommonUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.IntentUtils;
import com.google.gson.reflect.TypeToken;

public class SearchUserFragment extends
		BaseLoadFragment<String, List<SearchUser>> {

	private SearchUserAdapter adapter;
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
				SearchUser user = adapter.getItem(position);
				if (CommonUtils.isAuthUser(user.getLogin()))
					return;
				new RefreshUserTask(context, user.getLogin()) {

					@Override
					public void onSuccess(User result) {
						IntentUtils
								.create(context, UserNavigationActivity.class)
								.putExtra(Constants.Extra.USER, result).start();
					}
				}.execute();
			}
		});
		adapter = new SearchUserAdapter(context);
		list.setAdapter(adapter);
	}

	@Override
	protected void initData() {
		new SimpleUpdateResultTask<List<SearchUser>>(
				new SimpleUpdateResultTask.UpdateListener<List<SearchUser>>() {

					@Override
					public void onPrev() {

					}

					@Override
					public List<SearchUser> onExcute() {
						if (CacheUtils.contain(getCacheName())) {
							List<SearchUser> list = CacheUtils.getCacheObject(
									getCacheName(),
									new TypeToken<List<SearchUser>>() {
									}.getType());
							return list;
						}
						return null;
					}

					@Override
					public void onSuccess(List<SearchUser> result) {
						if (result != null)
							adapter.setDatas(result);
					}
				}).execute();

	}

	@Override
	public String getCacheName() {
		return CacheUtils.NAME.SEARCH_USER;
	}

	@Override
	public List<SearchUser> onBackground(String params) throws Exception {
		List<SearchUser> list = null;
		if (TextUtils.isEmpty(params)) {
			return null;
		}
		if (!params.equals(query))
			query = params;
		list = GitHubConsole.getInstance().searchUsers(query);
		CacheUtils.cacheObject(getCacheName(), list);
		return list;
	}

	@Override
	public void onSuccess(List<SearchUser> result) {
		super.onSuccess(result);
		adapter.setDatas(result);
	}

	@Override
	public void onRefresh() {
		execute(query);
	}

	@Override
	public void initStatusPopup(TitleBar title) {

	}

}
