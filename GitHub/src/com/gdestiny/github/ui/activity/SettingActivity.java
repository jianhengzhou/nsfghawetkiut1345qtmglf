package com.gdestiny.github.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.gdestiny.github.R;
import com.gdestiny.github.ui.activity.abstracts.BaseFragmentActivity;
import com.gdestiny.github.ui.dialog.MaterialUpdateDialog;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.AndroidUtils;
import com.umeng.update.UmengUpdateAgent;

public class SettingActivity extends BaseFragmentActivity implements
		View.OnClickListener {

	private MaterialUpdateDialog updateDialog;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.act_setting);

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		findViewById(R.id.rate).setOnClickListener(this);
		findViewById(R.id.share).setOnClickListener(this);
		findViewById(R.id.check_for_update).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rate:
			AndroidUtils.market(context);
			break;
		case R.id.share:
			AndroidUtils
					.share(context, "Github", "https://github.com/gdestiny");
			break;
		case R.id.check_for_update:
			if (updateDialog == null)
				updateDialog = new MaterialUpdateDialog(context);
			UmengUpdateAgent.forceUpdate(this);
			break;
		}
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initActionBar(TitleBar titleBar) {
		// TODO Auto-generated method stub
		// super.initActionBar(titleBar);
		getSupportActionBar().hide();
	}

	@Override
	protected void onleftLayout() {
		// TODO Auto-generated method stub

	}

}
