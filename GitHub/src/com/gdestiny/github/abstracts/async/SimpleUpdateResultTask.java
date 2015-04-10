package com.gdestiny.github.abstracts.async;


public class SimpleUpdateResultTask<Result> extends
		BaseAsyncTask<Void, Void, Result> {

	private UpdateListener<Result> listener;

	@SuppressWarnings("unused")
	private SimpleUpdateResultTask() {
		throw new AssertionError();
	}

	public SimpleUpdateResultTask(UpdateListener<Result> listener) {
		this.listener = listener;
	}

	public interface UpdateListener<Result> {
		public void onPrev();

		public Result onExcute();

		public void onSuccess(Result result);

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		listener.onPrev();
	}

	@Override
	protected Result doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return listener.onExcute();
	}

	@Override
	protected void onPostExecute(Result result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		listener.onSuccess(result);
	}

}
