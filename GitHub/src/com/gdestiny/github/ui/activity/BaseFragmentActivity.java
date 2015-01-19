package com.gdestiny.github.ui.activity;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.gdestiny.github.R;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;

public abstract class BaseFragmentActivity extends SherlockFragmentActivity
		implements SwipeBackActivityBase {
	private SwipeBackActivityHelper mHelper;
	protected TitleBar titlebar;
	protected String mClassName;
	protected StringBuilder mBuffer = new StringBuilder();
	protected Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		mClassName = getClass().getSimpleName();
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onCreate()").toString());

		mHelper = new SwipeBackActivityHelper(this);
		mHelper.onActivityCreate();
		initView();
		initActionBar();
		initData();
	}

	protected void initActionBar() {
		titlebar = new TitleBar(this);

		ActionBar actionbar = getSupportActionBar();
		actionbar.setCustomView(titlebar);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayUseLogoEnabled(false);
		// ¼àÌý
		View backLayout = titlebar.findViewById(R.id.title_left_layout);
		backLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onleftLayout();
			}
		});
		ImageButton rightBtn = (ImageButton) titlebar
				.findViewById(R.id.titlebar_rignt_btn);
		rightBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onRightBtn();
			}
		});
		AndroidUtils.initMiBar(this);
	}

	public TitleBar getTitlebar() {
		return titlebar;
	}

	abstract protected void initView();

	abstract protected void initData();

	abstract protected void onleftLayout();

	protected void onRightBtn() {
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mHelper.onPostCreate();
	}

	@Override
	public View findViewById(int id) {
		View v = super.findViewById(id);
		if (v == null && mHelper != null)
			return mHelper.findViewById(id);
		return v;
	}

	@Override
	public SwipeBackLayout getSwipeBackLayout() {
		return mHelper.getSwipeBackLayout();
	}

	@Override
	public void setSwipeBackEnable(boolean enable) {
		getSwipeBackLayout().setEnableGesture(enable);
	}

	@Override
	public void scrollToFinishActivity() {
		Utils.convertActivityToTranslucent(this);
		getSwipeBackLayout().scrollToFinishActivity();
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onAttachedToWindow()").toString());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onNewIntent()").toString());
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onRestart()").toString());
	}

	@Override
	protected void onStart() {
		super.onStart();
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onStart()").toString());
	}

	@Override
	protected void onResume() {
		super.onResume();
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onResume()").toString());
	}

	@Override
	public boolean onSearchRequested() {
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onSearchRequested()").toString());
		return super.onSearchRequested();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onConfigurationChanged()")
						.append(newConfig.toString()).toString());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onSaveInstanceState()").toString());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onRestoreInstanceState()").toString());
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onBackPressed() {
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onBackPressed()").toString());
		super.onBackPressed();
	}

	@Override
	public void finish() {
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".finish()").toString());
		super.finish();
	}

	@Override
	protected void onPause() {
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onPause()").toString());
		super.onPause();
	}

	@Override
	protected void onStop() {
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onStop()").toString());
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onDestroy()").toString());
		super.onDestroy();
	}
}
