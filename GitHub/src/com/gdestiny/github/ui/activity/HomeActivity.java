package com.gdestiny.github.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.gdestiny.github.R;
import com.gdestiny.github.ui.fragment.LeftMenuFragment;
import com.gdestiny.github.ui.fragment.NewsFragment;
import com.gdestiny.github.ui.fragment.RepositoryFragment;
import com.gdestiny.github.ui.view.ResideMenu;

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
		showFragment(currentFragment = new RepositoryFragment());
	}

	private void showFragment(Fragment fragment) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.home_frame, fragment);
		ft.commit();
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
		}
		resideMenu.closeMenu();
	}
}
