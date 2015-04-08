package com.gdestiny.github.async;

import org.eclipse.egit.github.core.Repository;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.utils.ToastUtils;

import android.content.Context;

public class ForkRepositoryTask extends DialogTask<Void, Boolean> {

	private Repository repository;

	public ForkRepositoryTask(Context context, Repository repository) {
		super(context);
		this.repository = repository;
		this.setTitle(repository.getName());
		this.setLoadingMessage(R.string.forking);
	}

	@Override
	public Boolean onBackground(Void params) throws Exception {
		GitHubConsole.getInstance().fork(repository);
		return true;
	}

	@Override
	public void onSuccess(Boolean result) {
		ToastUtils.show(context, R.string.fork_succeed);
	}

}
