package com.gdestiny.github.ui.fragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.egit.github.core.Gist;

import android.content.Intent;
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
import com.gdestiny.github.ui.activity.NewGistActivity;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.fragment.GistListFragment.GistType;
import com.gdestiny.github.ui.view.IndicatorView;
import com.gdestiny.github.ui.view.ResideMenu;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.IntentUtils;

public class GistFragment extends BaseFragment {

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
				.add(R.string._public, R.drawable.common_all_white);

		fragments.add(new GistListFragment(GistType.MINE));
		fragments.add(new GistListFragment(GistType.STAR));
		fragments.add(new GistListFragment(GistType.PUBLIC));

		adapter = new SimplePageAdapter(
				((BaseFragmentActivity) context).getSupportFragmentManager(),
				fragments);
		viewpager.setAdapter(adapter);

		resideMenu = ((HomeActivity) context).getResideMenu();
	}

	@Override
	public void onResultOk(int requestCode, Intent data) {
		super.onResultOk(requestCode, data);
		if (requestCode == Constants.Request.NEW_GIST) {
			Gist gist = (Gist) data.getSerializableExtra(Constants.Extra.GIST);
			if (gist != null) {
				GistListFragment mineFragment = (GistListFragment) fragments
						.get(0);
				mineFragment.getDatas().add(0, gist);
				mineFragment.noData(false);
				mineFragment.getBaseAdapter().notifyDataSetChanged();
			}
		}
	}

	@Override
	public void initStatusPopup(final TitleBar title) {
		title.showStatusBtn();
		if (itemmap == null) {
			itemmap = new LinkedHashMap<Integer, Integer>();
			itemmap.put(R.string.new_gist, R.drawable.common_add);
			itemmap.put(R.string.refresh, R.drawable.common_status_refresh);
		}
		if (menuListener == null) {
			menuListener = new StatusPopUpWindow.StatusPopUpWindowItemClickListener() {

				@Override
				public void onitemclick(int titleId) {
					GLog.sysout(context.getResources().getString(titleId) + "");
					boolean dismiss = true;
					switch (titleId) {
					case R.string.refresh:
						if (getCurrentFragment().isLoading()) {
							GLog.sysout("update is not complete");
							return;
						}
						getCurrentFragment().onRefresh();
						break;
					case R.string.random:
						break;
					case R.string.new_gist:
						IntentUtils.create(context, NewGistActivity.class)
								.startForResult(GistFragment.this,
										Constants.Request.NEW_GIST);
						break;
					default:
						break;
					}
					if (dismiss)
						title.dissmissStatus();
				}
			};
		}
		title.setStatusItem(context, itemmap, menuListener);
	}

}
