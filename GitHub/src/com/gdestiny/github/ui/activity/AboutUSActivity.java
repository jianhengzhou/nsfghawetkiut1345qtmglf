package com.gdestiny.github.ui.activity;

import android.os.Bundle;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseFragmentActivity;
import com.gdestiny.github.ui.view.TitleBar;

public class AboutUSActivity extends BaseFragmentActivity {

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.act_about_us);
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
	protected void initActionBar(TitleBar titleBar) {
		super.initActionBar(titleBar);
		titleBar.hideRight();
		titleBar.setLeftLayout("drawable://" + R.drawable.test, "gdestiny", "guandichao@163.com");
	}

	@Override
	protected void onleftLayout() {
		// TODO Auto-generated method stub
		finish();
	}

}
