package com.gdestiny.github.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.gdestiny.github.R;
import com.gdestiny.github.ui.fragment.FollowingFragment;
import com.gdestiny.github.ui.fragment.LeftMenuFragment;
import com.gdestiny.github.ui.fragment.RepositoryFragment;
import com.gdestiny.github.ui.view.ResideMenu;
import com.gdestiny.github.utils.GLog;

public class HomeActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ResideMenu resideMenu;
	private Fragment currentFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_home);
		// ActionBarPullToRefresh.from(this).allChildrenArePullable()
		// .listener(this)
		// .setup((PullToRefreshLayout) findViewById(R.id.ptr_layout));
		initMenu();
		RepositoryFragment repositoryFragment = new RepositoryFragment();
		showFragment(repositoryFragment);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onleftLayout() {
		// TODO Auto-generated method stub
		resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
	}

	private void showFragment(Fragment fragment) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// fragmentTransaction.setCustomAnimations(R.anim.in, R.anim.out);
		if (!fragment.isAdded()) {
			ft.add(R.id.home_frame, fragment);
		} else {
			ft.show(fragment);
		}
		if (currentFragment != null)
			ft.hide(currentFragment);
		ft.commit();
		currentFragment = fragment;
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
		switch (v.getId()) {
		case R.id.menu_avatar:
			break;
		case R.id.menu_repository:
			break;
		case R.id.menu_news:
			break;
		case R.id.menu_following:
			showFragment(new FollowingFragment());
			break;
		}
		resideMenu.closeMenu();
	}

}
