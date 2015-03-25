package com.gdestiny.github.async;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.WatcherService;

import com.gdestiny.github.R;
import com.gdestiny.github.async.abstracts.DialogTask;
import com.gdestiny.github.utils.ToastUtils;

import android.content.Context;

public class StarRepositoryTask extends DialogTask<GitHubClient, Boolean> {

	private boolean isStar;
	private Repository repository;

	public StarRepositoryTask(Context context, boolean isStar,
			Repository repository) {
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
	public Boolean onBackground(GitHubClient params) throws Exception {
		WatcherService service = new WatcherService(params);
		if (isStar)
			service.unwatch(repository);
		else
			service.watch(repository);
		return true;
	}

	@Override
	public void onSuccess(Boolean result) {
		if (isStar)
			ToastUtils.show(context, R.string.unstar_succeed);
		else
			ToastUtils.show(context, R.string.star_succeed);
	}

}
