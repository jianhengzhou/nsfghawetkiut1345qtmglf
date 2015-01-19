package com.gdestiny.github.ui.activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.gdestiny.github.R;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.utils.AndroidUtils;

public class SplashActivity extends Activity {

	private Handler handler;
	private static final int limit = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_splash);
		AndroidUtils.initMiBar(this);
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				Intent intent = null;
				if (GitHubApplication.isLogin()) {
					intent = new Intent(SplashActivity.this, HomeActivity.class);
				} else {
					intent = new Intent(SplashActivity.this,
							LoginActivity.class);
				}
				startActivity(intent);
				SplashActivity.this.finish();
			}

		};
		ExecutorService singleThread = Executors.newSingleThreadExecutor();
		singleThread.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(limit);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0);
			}
		});
	}

}
