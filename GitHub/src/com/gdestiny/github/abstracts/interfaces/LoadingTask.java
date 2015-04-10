package com.gdestiny.github.abstracts.interfaces;

public interface LoadingTask<Params, Result> {
	public void onPrev();

	public Result onBackground(Params params) throws Exception;

	public void onSuccess(Result result);

	public void onException(Exception ex);

	public void execute(Params params);
}
