package com.gdestiny.github.ui.activity;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.gdestiny.github.R;
import com.special.ResideMenu.ResideMenu;

public class HomeActivity extends BaseFragmentActivity implements
		OnRefreshListener {

	private ResideMenu resideMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_home);
		ActionBarPullToRefresh.from(this).allChildrenArePullable()
				.listener(this)
				.setup((PullToRefreshLayout) findViewById(R.id.ptr_layout));
		initMenu();
	}

	@SuppressWarnings("deprecation")
	private void initMenu() {
		resideMenu = new ResideMenu(this);
		resideMenu.setBackground(R.drawable.common_menu_background);
		resideMenu.setScaleValue(0.7f);
		resideMenu.setDirectionDisable(ResideMenu.DIRECTION_RIGHT);
		resideMenu.attachToActivity(this);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return resideMenu.dispatchTouchEvent(ev);
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		((PullToRefreshLayout) findViewById(R.id.ptr_layout))
				.setRefreshComplete();
	}

}
