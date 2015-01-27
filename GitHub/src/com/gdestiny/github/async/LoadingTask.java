package com.gdestiny.github.async;

public interface LoadingTask<Params, Result> {
	public void onPrev();

	public Result doInBackground(Params params);

	public void onSuccess(Result result);

	public void onError();

	public void execute(Params params);
}
