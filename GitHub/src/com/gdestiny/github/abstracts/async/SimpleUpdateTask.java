package com.gdestiny.github.abstracts.async;


public class SimpleUpdateTask extends BaseAsyncTask<Void, Void, Void> {

	private UpdateListener listener;

	@SuppressWarnings("unused")
	private SimpleUpdateTask() {
		throw new AssertionError();
	}

	public SimpleUpdateTask(UpdateListener listener) {
		this.listener = listener;
	}

	public interface UpdateListener {
		public void onPrev();

		public void onExcute();

		public void onSuccess();

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		listener.onPrev();
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		listener.onExcute();
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		listener.onSuccess();
	}

}
