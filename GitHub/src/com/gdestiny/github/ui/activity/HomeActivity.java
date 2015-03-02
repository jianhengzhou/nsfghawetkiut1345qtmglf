package com.gdestiny.github.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.async.DialogTask;
import com.gdestiny.github.ui.fragment.BaseLoadFragment;
import com.gdestiny.github.ui.fragment.EventsUserReceivedFragment;
import com.gdestiny.github.ui.fragment.FollowerFragment;
import com.gdestiny.github.ui.fragment.FollowingFragment;
import com.gdestiny.github.ui.fragment.LeftMenuFragment;
import com.gdestiny.github.ui.fragment.RepositoryFragment;
import com.gdestiny.github.ui.view.ResideMenu;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.TestUtils;
import com.gdestiny.github.utils.ToastUtils;

public class HomeActivity extends BaseFragmentActivity implements
		OnClickListener {

	private long keyTime; // again exit
	public static final int exitLimit = 2000;
	private ResideMenu resideMenu;
	private BaseLoadFragment<?, ?> currentFragment;
	private String currentFragmentTag;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_home);
		setSwipeBackEnable(false);
		initMenu();
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		getTitlebar().setTitleIcon(GitHubApplication.getUser().getAvatarUrl());

		currentFragment = new RepositoryFragment();
		currentFragmentTag = getResources().getString(R.string.repository);
		changeFragment(R.id.home_frame, null, currentFragment,
				currentFragmentTag);
	}

	@Override
	protected void onleftLayout() {
		resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
	}

	@SuppressWarnings("deprecation")
	private void initMenu() {
		resideMenu = new ResideMenu(this);
		resideMenu.setBackground(R.drawable.common_menu_background);
		resideMenu.setScaleValue(0.68f);
		resideMenu.setDirectionDisable(ResideMenu.DIRECTION_RIGHT);
		resideMenu.attachToActivity(this);
		resideMenu.setLeftMenuFragment(this, new LeftMenuFragment(this));
		resideMenu.setMenuListener(new ResideMenu.OnMenuListener() {

			@Override
			public void openMenu() {
				GLog.sysout("resideMenu openMenu");
				hideHeaderView(currentFragment);
			}

			@Override
			public void closeMenu() {
				GLog.sysout("resideMenu closeMenu");
				showRefreshHeader(currentFragment);
			}

			@Override
			public void onMove() {
				if (!resideMenu.isOpened()) {
					hideHeaderView(currentFragment);
				}
			}
		});
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return resideMenu.dispatchTouchEvent(ev);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		boolean close = true;
		switch (v.getId()) {
		case R.id.menu_avatar:
			break;
		case R.id.menu_repository:
			changeOrNewFragment(v);
			break;
		case R.id.menu_news:
			changeOrNewFragment(v);
			break;
		case R.id.menu_follower:
			// changeOrNewFragment(v);
			close = false;
			new DialogTask<Void, Void>(context) {

				@Override
				public void onPrev() {
					// TODO Auto-generated method stub

				}

				@Override
				public Void onBackground(Void params) throws Exception {
					// TODO Auto-generated method stub
					TestUtils.interrupt(5000);
					return null;
				}

				@Override
				public void onSuccess(Void result) {
					// TODO Auto-generated method stub

				}
			}.setTitle("lodaing test").setLoadingMessage("test message")
					.execute(null);
			break;
		case R.id.menu_following:
			changeOrNewFragment(v);
			break;
		case R.id.menu_exit:
			close = false;
			finish();
			break;
		case R.id.menu_setting:
			close = false;
			IntentUtils.start(context, LoginActivity.class);
			break;
		case R.id.menu_gists:
			close = false;
			IntentUtils.start(context, CommitDetailActivity.class);
			break;
		}
		if (close)
			resideMenu.closeMenu();
	}

	private void changeOrNewFragment(View v) {
		String tag = null;
		if (v instanceof TextView) {
			tag = ((TextView) v).getText().toString();
		} else if (v instanceof ImageView) {
			tag = "user";
		}
		BaseLoadFragment<?, ?> newFragment = (BaseLoadFragment<?, ?>) getFragment(tag);
		if (currentFragment != null && currentFragment == newFragment) {
			currentFragment.onShowRepeat(context);
			GLog.sysout("currentFragment.onShowRepeat(context);");
		} else {
			hideHeaderView(currentFragment);
			if (newFragment == null) {
				GLog.sysout("newFragment == null");
				switch (v.getId()) {
				case R.id.menu_avatar:
					break;
				case R.id.menu_repository:
					newFragment = new RepositoryFragment();
					break;
				case R.id.menu_news:
					newFragment = new EventsUserReceivedFragment();
					break;
				case R.id.menu_follower:
					newFragment = new FollowerFragment();
					break;
				case R.id.menu_following:
					newFragment = new FollowingFragment();
					break;
				}
			}
			changeFragment(R.id.home_frame, currentFragment, newFragment, tag);
			newFragment.onShowInParentActivity(context);
			getTitlebar().setTitleText(tag);
			currentFragment = newFragment;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (resideMenu.isOpened()) {
			resideMenu.closeMenu();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long temp = event.getEventTime();
			GLog.sysout((temp - keyTime) + "");
			if ((temp - keyTime) > exitLimit) {
				ToastUtils.show(context,
						getResources().getString(R.string.again_exit));
				keyTime = temp;
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
