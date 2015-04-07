package com.gdestiny.github.ui.activity;

import org.eclipse.egit.github.core.client.GitHubClient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.gdestiny.github.R;
import com.gdestiny.github.ui.activity.abstracts.BaseLoadFragmentActivity;
import com.gdestiny.github.ui.view.TitleBar;

public class SearchActivity extends
		BaseLoadFragmentActivity<GitHubClient, Void> implements
		View.OnClickListener {

	private EditText search;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_search, R.id.pull_refresh_layout);
	}

	@Override
	protected void initActionBar(TitleBar titleBar) {
		View searchView = LayoutInflater.from(context).inflate(
				R.layout.layout_search, null);

		ActionBar actionbar = getSupportActionBar();
		actionbar.setCustomView(searchView);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayUseLogoEnabled(false);

		searchView.findViewById(R.id.search_back_btn).setOnClickListener(this);
		searchView.findViewById(R.id.search_rignt_btn).setOnClickListener(this);
		search = (EditText) searchView.findViewById(R.id.search);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_back_btn:
			finish();
			break;
		case R.id.search_rignt_btn:
			search.getText();
			onSearchRequested();
			break;
		}
	}

	@Override
	protected void initView() {
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public Void onBackground(GitHubClient params) throws Exception {
		// TODO Auto-generated method stub
		return super.onBackground(params);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onleftLayout() {
		finish();
	}

}
