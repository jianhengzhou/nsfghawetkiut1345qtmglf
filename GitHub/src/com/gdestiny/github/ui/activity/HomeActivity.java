package com.gdestiny.github.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseFragmentActivity;
import com.gdestiny.github.abstracts.fragment.BaseFragment;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.fragment.EventsUserReceivedFragment;
import com.gdestiny.github.ui.fragment.FollowerFragment;
import com.gdestiny.github.ui.fragment.FollowingFragment;
import com.gdestiny.github.ui.fragment.GistFragment;
import com.gdestiny.github.ui.fragment.IssueDashboardFragment;
import com.gdestiny.github.ui.fragment.LeftMenuFragment;
import com.gdestiny.github.ui.fragment.RepositoryFragment;
import com.gdestiny.github.ui.view.ResideMenu;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.PreferencesUtils;
import com.gdestiny.github.utils.ToastUtils;

public class HomeActivity extends BaseFragmentActivity implements
		OnClickListener {

	private long keyTime; // again exit
	public static final int exitLimit = 2000;
	private ResideMenu resideMenu;
	private BaseFragment currentFragment;
	private String currentFragmentTag;

	public ResideMenu getResideMenu() {
		return resideMenu;
	}

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

		int startup = PreferencesUtils.getInt(context, "startup", 0);
		switch (startup) {
		case 0:
			currentFragment = new RepositoryFragment();
			currentFragmentTag = getResources().getString(R.string.repository);
			break;
		case 1:
			currentFragment = new EventsUserReceivedFragment(GitHubApplication
					.getUser().getLogin());
			currentFragmentTag = getResources().getString(R.string.events);
			break;
		case 2:
			currentFragment = new FollowerFragment();
			currentFragmentTag = getResources().getString(R.string.followers);
			break;
		case 3:
			currentFragment = new FollowingFragment();
			currentFragmentTag = getResources().getString(R.string.following);
			break;
		case 4:
			currentFragment = new GistFragment();
			currentFragmentTag = getResources().getString(R.string.gists);
			break;
		case 5:
			currentFragment = new IssueDashboardFragment();
			currentFragmentTag = getResources().getString(
					R.string.issue_dashboard);
			break;

		}
		getTitlebar().setTitleText(currentFragmentTag);
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
			close = false;
			IntentUtils.start(context, UserActivity.class);
			break;
		case R.id.menu_search:
			close = false;
			IntentUtils.start(context, SearchActivity.class);
			break;
		case R.id.menu_repository:
			changeOrNewFragment(v);
			break;
		case R.id.menu_news:
			changeOrNewFragment(v);
			break;
		case R.id.menu_follower:
			changeOrNewFragment(v);
			break;
		case R.id.menu_following:
			changeOrNewFragment(v);
			break;
		case R.id.menu_issue:
			changeOrNewFragment(v);
			break;
		case R.id.menu_exit:
			close = false;
			finish();
			break;
		case R.id.menu_setting:
			close = false;
			IntentUtils.start(context, SettingActivity.class);
			break;
		case R.id.menu_gists:
			changeOrNewFragment(v);
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
		BaseFragment newFragment = (BaseFragment) getFragment(tag);
		if (currentFragment != null && currentFragment == newFragment) {
			currentFragment.onShowRepeat(context);
			GLog.sysout("currentFragment.onShowRepeat(context);");
		} else {
			if (newFragment == null) {
				GLog.sysout("newFragment == null");
				switch (v.getId()) {
				case R.id.menu_avatar:
					break;
				case R.id.menu_repository:
					newFragment = new RepositoryFragment();
					break;
				case R.id.menu_news:
					newFragment = new EventsUserReceivedFragment(
							GitHubApplication.getUser().getLogin());
					break;
				case R.id.menu_follower:
					newFragment = new FollowerFragment();
					break;
				case R.id.menu_following:
					newFragment = new FollowingFragment();
					break;
				case R.id.menu_issue:
					newFragment = new IssueDashboardFragment();
					break;
				case R.id.menu_gists:
					newFragment = new GistFragment();
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
