package com.gdestiny.github.ui.fragment;

import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.SimplePageAdapter;
import com.gdestiny.github.ui.activity.BaseFragmentActivity;
import com.gdestiny.github.ui.activity.HomeActivity;
import com.gdestiny.github.ui.view.IndicatorView;
import com.gdestiny.github.ui.view.ResideMenu;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.GLog;

public class GistFragment extends BaseFragment {

	private ResideMenu resideMenu;
	private ViewPager viewpager;
	private IndicatorView indicatorView;
	private List<BaseLoadFragment<?, ?>> fragments = new ArrayList<BaseLoadFragment<?, ?>>();

	private SimplePageAdapter adapter;

	public BaseLoadFragment<?, ?> getCurrentFragment() {
		if(indicatorView.getCurrentPosition() >= fragments.size())
			return null;
		return fragments.get(indicatorView.getCurrentPosition());
	}

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_gist);
	}

	@Override
	protected void initView() {
		viewpager = (ViewPager) findViewById(R.id.gist_viewpager);
		viewpager.setOffscreenPageLimit(3);

		indicatorView = (IndicatorView) findViewById(R.id.indicator);

		indicatorView.bind(viewpager);
		viewpager.setOnPageChangeListener(indicatorView);
		indicatorView
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						if (position == 0) {
							// resideMenu.clearIgnoredViewList();
							resideMenu.removeIgnoredView(viewpager);
						} else {
							if (resideMenu.getIgnoredViews() != null
									&& resideMenu.getIgnoredViews().size() == 0)
								resideMenu.addIgnoredView(viewpager);
						}
						if (position != indicatorView.getCurrentPosition()) {
							GLog.sysout("hide:"
									+ indicatorView.getCurrentPosition()
									+ ",show:" + position);
							BaseFragmentActivity.hideHeaderView(fragments
									.get(indicatorView.getCurrentPosition()));
							BaseFragmentActivity.showRefreshHeader(fragments
									.get(position));
						}
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
					}
				});
	}

	@Override
	protected void initData() {
		indicatorView.add(R.string.mine, R.drawable.common_people_white)
				.add(R.string.star, R.drawable.common_star_white)
				.add(R.string._all, R.drawable.common_all_white);

		fragments.add(new _EmptyFragment());
		fragments.add(new _EmptyFragment());
		fragments.add(new _EmptyFragment());

		adapter = new SimplePageAdapter(
				((BaseFragmentActivity) context).getSupportFragmentManager(),
				fragments);
		viewpager.setAdapter(adapter);
//		viewpager.setBackgroundColor(getResources().getColor(R.color.common_icon_blue));

		resideMenu = ((HomeActivity) context).getResideMenu();
	}

	@Override
	public void initStatusPopup(TitleBar title) {
	}

}
