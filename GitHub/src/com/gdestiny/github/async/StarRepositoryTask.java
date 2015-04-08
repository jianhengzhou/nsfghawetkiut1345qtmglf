package com.gdestiny.github.async;

import org.eclipse.egit.github.core.Repository;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.utils.ToastUtils;

import android.content.Context;

public class StarRepositoryTask extends DialogTask<Void, Boolean> {

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
	public Boolean onBackground(Void params) throws Exception {
		if (isStar)
			GitHubConsole.getInstance().unwatch(repository);
		else
			GitHubConsole.getInstance().watch(repository);
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
