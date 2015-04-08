package com.gdestiny.github.abstracts.async;

import android.os.AsyncTask;

import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;

public abstract class BaseAsyncTask<Params, Progress, Result> extends
		AsyncTask<Params, Progress, Result> {

	protected boolean running;

	protected long mTimeStart;

	protected long mTimeEnd;

	protected long mMemoryStart;

	protected long mMemoryEnd;

	protected StringBuilder mBuffer = new StringBuilder();

	/**
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		mTimeStart = System.currentTimeMillis();
		mMemoryStart = Runtime.getRuntime().freeMemory();
		running = true;
	}

	/**
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Result result) {
		running = false;
		mTimeEnd = System.currentTimeMillis();
		mMemoryEnd = Runtime.getRuntime().freeMemory();

		String classname = getClass().getSimpleName();
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(classname)
						.append(": it coasts ").append(mTimeEnd - mTimeStart)
						.append(" milliseconds.").toString());
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(classname)
						.append(": it coasts ")
						.append(mMemoryStart - mMemoryEnd)
						.append(" bytes memory.").toString());
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		running = false;
		super.onCancelled();
	}

	@Override
	protected void onCancelled(Result result) {
		// TODO Auto-generated method stub
		running = false;
		super.onCancelled(result);
	}

	public boolean isRunning() {
		return running;
	}

}
