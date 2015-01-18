package com.gdestiny.github.ui.fragment;

import org.eclipse.egit.github.core.User;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.view.ImageViewEx;
import com.gdestiny.github.utils.ImageLoaderUtils;

public class LeftMenuFragment extends BaseFragment implements OnClickListener {

	private OnClickListener clickListener;
	private ImageViewEx avatar;
	private TextView name;

	public LeftMenuFragment(OnClickListener clickListener) {
		this.clickListener = clickListener;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.currentView = inflater.inflate(R.layout.frag_left__menu, null);
		this.currentView.findViewById(R.id.menu_avatar)
				.setOnClickListener(this);
		this.currentView.findViewById(R.id.menu_repository).setOnClickListener(
				this);
		this.currentView.findViewById(R.id.menu_news).setOnClickListener(this);
		this.currentView.findViewById(R.id.menu_follower).setOnClickListener(
				this);
		this.currentView.findViewById(R.id.menu_following).setOnClickListener(
				this);
		this.currentView.findViewById(R.id.menu_gists).setOnClickListener(this);
		this.currentView.findViewById(R.id.menu_issue).setOnClickListener(this);
		this.currentView.findViewById(R.id.menu_bookmarks).setOnClickListener(
				this);
		this.currentView.findViewById(R.id.menu_setting).setOnClickListener(
				this);
		this.currentView.findViewById(R.id.menu_exit).setOnClickListener(this);
		return this.currentView;
	}

	@Override
	protected void initView() {
		avatar = (ImageViewEx) this.currentView.findViewById(R.id.menu_avatar);
		name = (TextView) this.currentView.findViewById(R.id.menu_name);
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

}
