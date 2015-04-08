package com.gdestiny.github.ui.fragment;

import org.eclipse.egit.github.core.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.fragment.BaseFragment;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.view.ImageViewEx;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.ImageLoaderUtils;

public class LeftMenuFragment extends BaseFragment implements OnClickListener {

	private OnClickListener clickListener;
	private ImageViewEx avatar;
	private TextView name;

	@SuppressWarnings("unused")
	private LeftMenuFragment() {
	}

	public LeftMenuFragment(OnClickListener clickListener) {
		this.clickListener = clickListener;
	}

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_left__menu);
	}

	@Override
	protected void initView() {
		findViewById(R.id.menu_avatar).setOnClickListener(this);
		findViewById(R.id.menu_repository).setOnClickListener(this);
		findViewById(R.id.menu_news).setOnClickListener(this);
		findViewById(R.id.menu_follower).setOnClickListener(this);
		findViewById(R.id.menu_following).setOnClickListener(this);
		findViewById(R.id.menu_gists).setOnClickListener(this);
		findViewById(R.id.menu_issue).setOnClickListener(this);
		findViewById(R.id.menu_bookmarks).setOnClickListener(this);
		findViewById(R.id.menu_setting).setOnClickListener(this);
		findViewById(R.id.menu_exit).setOnClickListener(this);
		findViewById(R.id.menu_search).setOnClickListener(this);

		avatar = (ImageViewEx) findViewById(R.id.menu_avatar);
		name = (TextView) findViewById(R.id.menu_name);
	}

	@Override
	protected void initData() {
		User user = GitHubApplication.getUser();
		name.setText(user.getLogin());
		ImageLoaderUtils.displayImage(user.getAvatarUrl(), avatar,
				R.drawable.default_avatar, R.drawable.default_avatar, false);
	}

	@Override
	public void onClick(View v) {
		if (clickListener != null)
			clickListener.onClick(v);
	}

	@Override
	public void initStatusPopup(TitleBar title) {

	}

}
