package com.gdestiny.github.async;


import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.WatcherService;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.TestUtils;
import com.gdestiny.github.utils.ToastUtils;

import android.content.Context;

public class StarTask extends DialogTask<GitHubClient, Boolean> {

	private boolean isStar;
	private Repository repository;

	public StarTask(Context context, boolean isStar, Repository repository) {
		super(context);
		this.isStar = isStar;
		this.repository = repository;
		this.setTitle(repository.getName());
		if (isStar)
			this.setLoadingMessage(R.string.unstaring);
		else
			this.setLoadingMessage(R.string.staring);
	}

	@Override
	public void onPrev() {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean onBackground(GitHubClient params) throws Exception {
		// TODO Auto-generated method stub
		WatcherService service = new WatcherService(params);
		// if (isStar)
		// service.unwatch(repository);
		// else
		// service.watch(repository);
		TestUtils.interrupt(5000);
		return true;
	}

	@Override
	public void onSuccess(Boolean result) {
		// TODO Auto-generated method stub
		if (isStar)
			ToastUtils.show(context, R.string.unstar_succeed);
		else
			ToastUtils.show(context, R.string.star_succeed);
	}

}
