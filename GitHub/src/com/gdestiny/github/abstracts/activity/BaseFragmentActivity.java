package com.gdestiny.github.abstracts.activity;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.gdestiny.github.R;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseFragmentActivity extends SherlockFragmentActivity
		implements SwipeBackActivityBase {

	// ///////////////////////////////////////////////////////////////////////////////
	// 另类的返回
	/** 手势监听 */
	// private android.view.GestureDetector mGestureDetector;
	// /** 是否需要监听手势关闭功能 */
	// private boolean mNeedBackGesture = true;
	//
	// private void initGestureDetector() {
	// if (mGestureDetector == null) {
	// OnGestureListener gs = new OnGestureListener() {
	//
	// @Override
	// public boolean onSingleTapUp(MotionEvent e) {
	// return false;
	// }
	//
	// @Override
	// public void onShowPress(MotionEvent e) {
	// }
	//
	// @Override
	// public boolean onScroll(MotionEvent e1, MotionEvent e2,
	// float distanceX, float distanceY) {
	// if ((e2.getX() - e1.getX()) > 100
	// && Math.abs(e1.getY() - e2.getY()) < 60) {
	// onBackPressed();
	// return true;
	// }
	// return false;
	// }
	//
	// @Override
	// public void onLongPress(MotionEvent e) {
	//
	// }
	//
	// @Override
	// public boolean onFling(MotionEvent e1, MotionEvent e2,
	// float velocityX, float velocityY) {
	// return false;
	// }
	//
	// @Override
	// public boolean onDown(MotionEvent e) {
	// return false;
	// }
	// };
	// mGestureDetector = new android.view.GestureDetector(
	// getApplicationContext(), gs);
	// }
	// }
	//
	// @Override
	// public boolean dispatchTouchEvent(MotionEvent ev) {
	// if (mNeedBackGesture) {
	// return mGestureDetector.onTouchEvent(ev)
	// || super.dispatchTouchEvent(ev);
	// }
	// return super.dispatchTouchEvent(ev);
	// }
	//
	// public void setNeedBackGesture(boolean mNeedBackGesture) {
	// this.mNeedBackGesture = mNeedBackGesture;
	// }

	// ///////////////////////////////////////////////////////////////////////////////

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
		// 监听
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
		// initSystemBar();
	}

	@TargetApi(19)
	private static void setTranslucentStatus(Activity activity, boolean on) {
		Window win = activity.getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	public void initSystemBar() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			// GLog.sysout("Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT");
			setTranslucentStatus(this, true);

			com.gdestiny.github.utils.SystemBarTintManager tintManager = new com.gdestiny.github.utils.SystemBarTintManager(
					this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setNavigationBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.common_icon_blue);
		}
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
		MobclickAgent.onResume(this);
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
		MobclickAgent.onPause(this);
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
