package com.gdestiny.github.ui.fragment;

import java.io.IOException;
import java.util.List;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.WatcherService;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.RepositoryAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.utils.TestUtils;
import com.gdestiny.github.utils.ToastUtils;

public class RepositoryFragment extends BaseLoadFragment {

	private ListView repositoryList;
	private RepositoryAdapter repositoryAdapter;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.currentView = inflater.inflate(R.layout.frag_repository, null);
		return this.currentView;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		repositoryList = (ListView) this.currentView
				.findViewById(R.id.repository_list);
		this.pullToRefreshLayout = (PullToRefreshLayout) this.currentView
				.findViewById(R.id.pull_refresh_layout);
		ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable()
				.listener(this).setup(pullToRefreshLayout);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		// showProgress();
		pullToRefreshLayout.setRefreshing(true);
		RepositoryTask task = new RepositoryTask();
		task.execute("");
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		// pullToRefreshLayout.setRefreshing(false);
	}

	private class RepositoryTask extends
			AsyncTask<String, Void, List<Repository>> {

		@Override
		protected List<Repository> doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<Repository> list = null;
			try {
				// list = new RepositoryService(GitHubApplication.getClient())
				// .getRepositories();
				list = new WatcherService(GitHubApplication.getClient())
						.getWatched();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			return list;
		}

		@Override
		protected void onPostExecute(List<Repository> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// dismissProgress();
			pullToRefreshLayout.setRefreshing(false);
			if (result == null) {
				ToastUtils.show(getActivity(), "error");
			} else {
				ToastUtils.show(getActivity(), "succeed");
				repositoryAdapter = new RepositoryAdapter(getActivity(), result);
				repositoryList.setAdapter(repositoryAdapter);
				System.out.println("" + TestUtils.printListRepository(result));
			}
		}

	}

}
