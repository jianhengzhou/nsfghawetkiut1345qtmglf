package com.gdestiny.github.ui.activity;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseFragmentActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TestActivity extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_test);
		TextView tv = (TextView) findViewById(R.id.test_tv);
		tv.setText(getIntent().getStringExtra("test"));
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
		finish();
	}

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	}

}
