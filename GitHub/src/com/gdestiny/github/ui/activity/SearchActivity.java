package com.gdestiny.github.ui.activity;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseFragmentActivity;
import com.gdestiny.github.adapter.SearchHistoryAdapter;
import com.gdestiny.github.ui.fragment.SearchRepositoryFragment;
import com.gdestiny.github.ui.fragment.SearchUserFragment;
import com.gdestiny.github.ui.view.FocusedEditText;
import com.gdestiny.github.ui.view.IndicatorView;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.CacheUtils;
import com.gdestiny.github.utils.ViewUtils;
import com.google.gson.reflect.TypeToken;

public class SearchActivity extends BaseFragmentActivity implements
		View.OnClickListener {

	private FocusedEditText search;
	private View searchDel;
	private View searchView;
	private ListView history;
	private SearchHistoryAdapter historyAdapter;

	private ViewPager viewpager;
	private IndicatorView indicatorView;

	private SearchRepositoryFragment searchRepositoryFragment;
	private SearchUserFragment searchUserFragment;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_search);
	}

	@Override
	protected void initActionBar(TitleBar titleBar) {
		searchView = LayoutInflater.from(context).inflate(
				R.layout.layout_search, null);
		// titleBar.addView(searchView);
		// super.initActionBar(titleBar);
		// titleBar.showRightBtn();
		// titleBar.getRightBtn().setImageResource(R.drawable.common_search);
		// titleBar.setLeftLayout(null, "", null);

		ActionBar actionbar = getSupportActionBar();
		actionbar.setCustomView(searchView);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayUseLogoEnabled(false);

		searchView.findViewById(R.id.search_back_btn).setOnClickListener(this);
		searchView.findViewById(R.id.search_rignt_btn).setOnClickListener(this);
		search = (FocusedEditText) searchView.findViewById(R.id.search);
		searchDel = searchView.findViewById(R.id.search_delete);
		searchDel.setOnClickListener(this);
		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (TextUtils.isEmpty(s)) {
					ViewUtils.setVisibility(searchDel, View.GONE);
				} else {
					ViewUtils.setVisibility(searchDel, View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		search.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					ViewUtils.setVisibility(history, View.VISIBLE,
							R.anim.slide_in_from_top);
				} else {
					ViewUtils.setVisibility(history, View.GONE,
							R.anim.slide_out_to_top);
				}
			}
		});
	}

	// @Override
	// protected void onRightBtn() {
	// super.onRightBtn();
	// ViewUtils.setVisibility(searchView, View.VISIBLE,
	// R.anim.right_to_left_in);
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_back_btn:
			finish();
		case R.id.search_delete:
			search.setText("");
			break;
		case R.id.search_rignt_btn:
			onSearch(search.getText().toString());
			// onSearchRequested();
			break;
		}
	}

	private void onSearch(String query) {
		if (TextUtils.isEmpty(query)) {
			return;
		}
		// ViewUtils
		// .setVisibility(searchView, View.GONE, R.anim.left_to_right_out);
		// getTitlebar().setLeftLayout(null, curQuery, null);
		if (search.hasFocus())
			search.clearFocus();
		if (!historyAdapter.contains(query)) {
			historyAdapter.add(query);
		}
		AndroidUtils.Keyboard.hideKeyboard(context);
		searchRepositoryFragment.execute(query);
		searchUserFragment.execute(query);
	}

	@Override
	protected void initView() {
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		viewpager.setOffscreenPageLimit(3);

		indicatorView = (IndicatorView) findViewById(R.id.indicator);

		indicatorView.bind(viewpager);
		viewpager.setOnPageChangeListener(indicatorView);

		history = (ListView) findViewById(R.id.history);
		historyAdapter = new SearchHistoryAdapter(context);
		history.setAdapter(historyAdapter);
		history.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == historyAdapter.getCount() - 1) {
					historyAdapter.clear();
					return;
				}
				String query = historyAdapter.getItem(position);
				search.setText(query);
				onSearch(query);
			}
		});
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		indicatorView.add(R.string._repository,
				R.drawable.common_repository_normal).add(R.string.user,
				R.drawable.common_accout_white);

		searchRepositoryFragment = new SearchRepositoryFragment();
		searchUserFragment = new SearchUserFragment();
		viewpager
				.setAdapter(new SearchPageAdapter(getSupportFragmentManager()));

		List<String> cacheHistory = CacheUtils.getCacheObject(
				CacheUtils.NAME.SEARCH_HISTORY, new TypeToken<List<String>>() {
				}.getType());
		historyAdapter.addAll(cacheHistory);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		CacheUtils.cacheObject(CacheUtils.NAME.SEARCH_HISTORY,
				historyAdapter.getDatas());
	}

	@Override
	protected void onleftLayout() {
		finish();
	}

	private class SearchPageAdapter extends FragmentStatePagerAdapter {

		public SearchPageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				return searchRepositoryFragment;
			}
			return searchUserFragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

	}
}
