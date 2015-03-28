package com.gdestiny.github.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.utils.AndroidUtils;

public class SplashActivity extends Activity {

	private static final int limit = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.act_splash, null);
		setContentView(view);
		AndroidUtils.initMiBar(this);

		// version
		TextView version = (TextView) view.findViewById(R.id.version);
		version.setText("version " + AndroidUtils.getVersion(this));
		// 渐变展示启动屏
		ScaleAnimation aa = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		aa.setFillAfter(true);
		aa.setDuration(limit);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				startMain();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				System.out.println(animation.getStartOffset());
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});
	}

	private void startMain() {
		Intent intent = null;
		if (GitHubApplication.isLogin()) {
			intent = new Intent(SplashActivity.this, HomeActivity.class);
			// intent = new Intent(SplashActivity.this, FeedbackActivity.class);
		} else {
			intent = new Intent(SplashActivity.this, LoginActivity.class);
		}
		startActivity(intent);
		SplashActivity.this.finish();
	}

	@Override
	public void onBackPressed() {
	}

}
