package com.gdestiny.github.abstracts.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Message;

public abstract class HandlerUpdateTask {

	private Handler handler;
	private static ExecutorService threadPool;

	@SuppressWarnings("unused")
	private HandlerUpdateTask() {
	}

	public HandlerUpdateTask(Handler handler) {
		this.handler = handler;
		if (threadPool == null) {
			threadPool = Executors.newFixedThreadPool(3);
		}
	}

	abstract public Object onExcute();

	public void excute(final int what) {
		threadPool.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Object oj = onExcute();
				if (oj == null)
					handler.sendEmptyMessage(what);
				else {
					Message msg = handler.obtainMessage();
					msg.what = what;
					msg.obj = oj;
					handler.sendMessage(msg);
				}
			}
		});
	}

	public void excute() {
		threadPool.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Object oj = onExcute();
				if (oj == null)
					handler.sendEmptyMessage(0);
				else {
					Message msg = handler.obtainMessage();
					msg.what = 0;
					msg.obj = oj;
					handler.sendMessage(msg);
				}
			}
		});
	}
}
