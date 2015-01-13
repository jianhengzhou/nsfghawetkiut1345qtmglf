package com.gdestiny.github.async;

import org.eclipse.egit.github.core.client.GitHubClient;

import android.os.AsyncTask;

public class GitHubTask<Result> extends AsyncTask<GitHubClient, Void, Result> {

	public interface TaskListener {
		public void onExcute();
	}

	@Override
	protected Result doInBackground(GitHubClient... params) {
		// TODO Auto-generated method stub
		return null;
	}

}
