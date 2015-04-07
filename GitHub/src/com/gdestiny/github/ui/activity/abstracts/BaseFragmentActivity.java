package com.gdestiny.github.ui.activity.abstracts;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.gdestiny.github.R;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;

public abstract class BaseFragmentActivity extends SherlockFragmentActivity
		implements SwipeBackActivityBase {
	private SwipeBackActivityHelper mHelper;
	protected TitleBar titlebar;
	protected String mClassName;
	protected StringBuilder mBuffer = new StringBuilder();
	protected Activity context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		mClassName = getClass().getSimpleName();
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onCreate()").toString());

		titlebar = new TitleBar(this);
		initActionBar(titlebar);
		mHelper = new SwipeBackActivityHelper(this);
		mHelper.onActivityCreate();
		setContentView(savedInstanceState);
		initView();
		initData();
	}

	abstract protected void setContentView(Bundle savedInstanceState);

	abstract protected void initView();

	abstract protected void initData();

	abstract protected void onleftLayout();

	protected void initActionBar(TitleBar titleBar) {

		ActionBar actionbar = getSupportActionBar();
		actionbar.setCustomView(titleBar);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayUseLogoEnabled(false);
		// ¼àÌý
		View backLayout = titleBar.findViewById(R.id.title_left_layout);
		backLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onleftLayout();
			}
		});
		ImageButton rightBtn = (ImageButton) titleBar
				.findViewById(R.id.titlebar_rignt_btn);
		rightBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onRightBtn();
			}
		});
		// com.gdestiny.github.utils.AndroidUtils.initMiBar(this);
	}

	public TitleBar getTitlebar() {
		return titlebar;
	}

	protected void onRightBtn() {
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
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

	public Fragment getFragment(String tag) {
		return getSupportFragmentManager().findFragmentByTag(tag);
	}

	public void changeFragment(int replaceLayoutId, Fragment f, String tag,
			boolean addToBackStack, boolean anim) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// if(anim)
		// ft.setCustomAnimations(R.anim.left_to_right_in,
		// R.anim.left_to_right_out,
		// R.anim.right_to_left_in,
		// R.anim.right_to_left_out);
		Fragment currentFragment = getFragment(tag);
		if (currentFragment != null) {
			ft.remove(currentFragment);
		}
		ft.add(replaceLayoutId, f, tag);

		// ft.replace(replaceLayoutId, f,tag);
		// if(addToBackStack)ft.addToBackStack(null);
		ft.commit();
	}

	public void changeFragment(int replaceLayoutId, Fragment orgF,
			Fragment newF, String newFTag) {
		if (orgF == newF) {
			return;
		}
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if (orgF != null) {
			if (orgF.isAdded()) {
				ft.hide(orgF);
			}
		}
		if (newF != null) {
			if (newF.isAdded()) {
				ft.show(newF);
			} else {
				ft.add(replaceLayoutId, newF, newFTag);
			}
		}
		ft.commitAllowingStateLoss();
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			onResultOk(requestCode, data);
		} else {
			onResultCancle(requestCode, data);
		}
	}

	public void onResultOk(int requestCode, Intent data) {

	}

	public void onResultCancle(int requestCode, Intent data) {

	}
}
