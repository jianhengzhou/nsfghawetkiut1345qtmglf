package com.gdestiny.github.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.service.IssueService;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseFragmentActivity;
import com.gdestiny.github.abstracts.fragment.BaseFragment;
import com.gdestiny.github.abstracts.fragment.BaseLoadFragment;
import com.gdestiny.github.adapter.SimplePageAdapter;
import com.gdestiny.github.ui.activity.HomeActivity;
import com.gdestiny.github.ui.view.IndicatorView;
import com.gdestiny.github.ui.view.ResideMenu;
import com.gdestiny.github.ui.view.TitleBar;

public class IssueDashboardFragment extends BaseFragment {

	private ResideMenu resideMenu;
	private ViewPager viewpager;
	private IndicatorView indicatorView;
	private List<BaseLoadFragment<?, ?>> fragments = new ArrayList<BaseLoadFragment<?, ?>>();

	private SimplePageAdapter adapter;

	public BaseLoadFragment<?, ?> getCurrentFragment() {
		if (indicatorView.getCurrentPosition() >= fragments.size())
			return null;
		return fragments.get(indicatorView.getCurrentPosition());
	}

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_issue_dashboard);
	}

	@Override
	protected void initView() {
		viewpager = (ViewPager) findViewById(R.id.viewpager);
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
		indicatorView.add(R.string.created, R.drawable.common_created_white)
				.add(R.string.assigned, R.drawable.common_assigned_white)
				.add(R.string.mentioned, R.drawable.common_mention_white);

		fragments.add(new IssueDashboardIssueFragment(
				IssueService.FILTER_CREATED));
		fragments.add(new IssueDashboardIssueFragment(
				IssueService.FILTER_ASSIGNED));
		fragments.add(new IssueDashboardIssueFragment(
				IssueService.FILTER_MENTIONED));

		adapter = new SimplePageAdapter(
				((BaseFragmentActivity) context).getSupportFragmentManager(),
				fragments);
		viewpager.setAdapter(adapter);

		resideMenu = ((HomeActivity) context).getResideMenu();
	}

	@Override
	public void initStatusPopup(TitleBar title) {
		title.hideRight();
	}

}
