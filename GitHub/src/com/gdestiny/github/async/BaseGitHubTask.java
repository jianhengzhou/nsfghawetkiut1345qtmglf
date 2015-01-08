package com.gdestiny.github.async;

import org.eclipse.egit.github.core.client.GitHubClient;

import android.os.AsyncTask;

public class BaseGitHubTask<T> extends AsyncTask<GitHubClient, Void, T> {

	public interface TaskListener {
		
	}

	@Override
	protected T doInBackground(GitHubClient... params) {
		// TODO Auto-generated method stub
		return null;
	}

}
