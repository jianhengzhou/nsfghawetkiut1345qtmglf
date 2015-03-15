package com.gdestiny.github.ui.activity;

import org.eclipse.egit.github.core.User;

import android.os.Bundle;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.view.ImageViewEx;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.ImageLoaderUtils;

public class UserActivity extends BaseFragmentActivity {

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_user);
	}

	@Override
	protected void initActionBar(TitleBar titleBar) {
		super.initActionBar(titleBar);
		// titleBar.hideRight();
		// titleBar.setLeftLayout(null, null, null);
		// titleBar.getBackground().setAlpha(100);
		getSupportActionBar().hide();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		User user = GitHubApplication.getUser();

		ImageViewEx avator = (ImageViewEx) findViewById(R.id.avatar);
		ImageLoaderUtils.displayImage(user.getAvatarUrl(), avator,
				R.drawable.default_avatar, R.drawable.default_avatar, false);

		TextView loginName = (TextView) findViewById(R.id.login_name);
		loginName.setText(user.getLogin());

		TextView name = (TextView) findViewById(R.id.name);
		name.setText(user.getName() + "");

		System.out.println(user.getPlan().getName());
	}

	@Override
	protected void onleftLayout() {
		// TODO Auto-generated method stub

	}

}
