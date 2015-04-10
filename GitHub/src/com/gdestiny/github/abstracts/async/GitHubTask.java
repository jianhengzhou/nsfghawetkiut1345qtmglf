package com.gdestiny.github.abstracts.async;

import org.eclipse.egit.github.core.client.GitHubClient;


public class GitHubTask<Result> extends
		BaseAsyncTask<GitHubClient, Void, Result> {

	private TaskListener<Result> listener;

	@SuppressWarnings("unused")
	private GitHubTask() {
		throw new AssertionError();
	}

	public GitHubTask(TaskListener<Result> listener) {
		this.listener = listener;
	}

	public interface TaskListener<T> {
		public void onPrev();

		public T onExcute(GitHubClient client);

		public void onSuccess(T result);

		public void onError();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		listener.onPrev();
	}

	@Override
	protected Result doInBackground(GitHubClient... params) {
		// TODO Auto-generated method stub
		if (params == null || params.length == 0)
			return listener.onExcute(null);
		return listener.onExcute(params[0]);
	}

	@Override
	protected void onPostExecute(Result result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result == null && !(result instanceof Void))
			listener.onError();
		else
			listener.onSuccess(result);
	}

}
