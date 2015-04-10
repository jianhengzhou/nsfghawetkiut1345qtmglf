package com.gdestiny.github.ui.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.egit.github.core.Repository;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseFragmentActivity;
import com.gdestiny.github.abstracts.async.SimpleUpdateTask;
import com.gdestiny.github.abstracts.fragment.BaseLoadFragment;
import com.gdestiny.github.adapter.RepositoryAdapter;
import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.bean.comparator.RepositoryComparator;
import com.gdestiny.github.ui.activity.RepositoryDetailActivity;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.CacheUtils;
import com.gdestiny.github.utils.Constants.Sort;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.ToastUtils;
import com.gdestiny.github.utils.ViewUtils;
import com.google.gson.reflect.TypeToken;

public class RepositoryFragment extends BaseLoadFragment<Void, Boolean> {

	private ListView repositoryList;
	private RepositoryAdapter repositoryAdapter;

	@SuppressWarnings("rawtypes")
	private List viewRepository;
	@SuppressWarnings("rawtypes")
	private List myRepository;
	@SuppressWarnings("rawtypes")
	private List starRepository;

	private ImageView az;
	private int currAlphbet = -1;
	private HashMap<Character, Integer> alphaIndex = new HashMap<Character, Integer>();

	private Sort curSort = Sort.All;
	private boolean isSorting = false;

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_repository,
				R.id.pull_refresh_layout);
	}

	@Override
	protected void initView() {
		initStatusPopup(((BaseFragmentActivity) context).getTitlebar());
		repositoryList = (ListView) findViewById(R.id.repository_list);
		repositoryList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				IntentUtils.start(context, RepositoryDetailActivity.class,
						Constants.Extra.REPOSITORY,
						(Repository) viewRepository.get(position));
			}
		});

	}

	// @Override
	// public void onHiddenChanged(boolean hidden) {
	// super.onHiddenChanged(hidden);
	// if (!hidden)
	// initStatusPopup(((BaseFragmentActivity) context).getTitlebar());
	// }

	@Override
	public void initStatusPopup(final TitleBar title) {
		if (itemmap == null) {
			itemmap = new LinkedHashMap<Integer, Integer>();
			itemmap.put(R.string.sort, R.drawable.common_status_sort);
			itemmap.put(R.string.refresh, R.drawable.common_status_refresh);
		}
		// 二级菜单
		if (itemmapSecondly == null) {
			title.initSecondly();
			itemmapSecondly = new LinkedHashMap<Integer, Integer>();
			itemmapSecondly.put(R.string.all, R.drawable.common_code_grey);
			itemmapSecondly.put(R.string.star, R.drawable.common_star_grey);
			itemmapSecondly
					.put(R.string.own, R.drawable.common_own_people_grey);
			itemmapSecondly.put(R.string.user, R.drawable.circle_user_grey);
			itemmapSecondly.put(R.string.time, R.drawable.common_time_grey);
		}
		if (menuListener == null) {
			menuListener = new StatusPopUpWindow.StatusPopUpWindowItemClickListener() {

				@Override
				public void onitemclick(int titleId) {
					GLog.sysout(context.getResources().getString(titleId) + "");
					boolean dismiss = true;
					switch (titleId) {
					case R.string.refresh:
						if (isLoading()) {
							GLog.sysout("update is not complete");
							return;
						}
						execute();
						break;
					case R.string.sort:
						dismiss = false;
						title.showSecondly();
						break;
					case R.string.all:
						asyncSort(Sort.All, false);
						break;
					case R.string.star:
						asyncSort(Sort.Star, false);
						break;
					case R.string.own:
						asyncSort(Sort.Own, false);
						break;
					case R.string.user:
						asyncSort(Sort.User, false);
						break;
					case R.string.time:
						asyncSort(Sort.Time, false);
						break;
					}
					if (dismiss)
						title.dissmissStatus();
				}
			};
		}
		title.setSecondlyStatusItem(context, itemmapSecondly);
		title.setStatusItem(context, itemmap, menuListener);
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected void initData() {
		viewRepository = new ArrayList();
		repositoryAdapter = new RepositoryAdapter(context, viewRepository);
		repositoryList.setAdapter(repositoryAdapter);

		myRepository = CacheUtils.getCacheObject(
				CacheUtils.NAME.LIST_OWN_REPOSITORY,
				new TypeToken<List<Repository>>() {
				}.getType());
		starRepository = CacheUtils.getCacheObject(
				CacheUtils.NAME.LIST_STAR_REPOSITORY,
				new TypeToken<List<Repository>>() {
				}.getType());

		initAlaphtSort();

		if (myRepository != null && starRepository != null) {
			asyncSort(curSort, true);
		} else {
			execute();
		}
	}

	private void initAlaphtSort() {
		az = (ImageView) findViewById(R.id.a_z_alphabet);
		final TextView azToast = (TextView) findViewById(R.id.alphabet_toast);
		az.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int totalHeight = az.getHeight();
				float y = event.getY();
				int position = (int) ((y / totalHeight) / (1f / 28f));

				char ch = 0;
				if (position <= 0)
					ch = '↑';
				else if (position == 1)
					ch = '#';
				else if (position <= 26)
					ch = (char) (position - 2 + 'A');
				else if (position > 26)
					ch = 'z';

				if (currAlphbet != position) {
					azToast.setText(ch + "");
					currAlphbet = position;
					GLog.sysout(ch + "");
					if (alphaIndex.containsKey(ch))
						repositoryList.setSelection(alphaIndex.get(ch));
					else if (position == 0)
						repositoryList.setSelection(0);
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					azToast.setVisibility(View.GONE);
					az.setImageResource(R.drawable.a_z);
					currAlphbet = -1;
				} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
					azToast.setVisibility(View.VISIBLE);
					az.setImageResource(R.drawable.a_z_click);
				}
				return true;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public Boolean onBackground(Void params) throws Exception {
		// TODO Auto-generated method stub
		// TestUtils.interrupt(5000);
		myRepository = GitHubConsole.getInstance().getRepositories();
		starRepository = GitHubConsole.getInstance().getWatchRepositories();
		if (myRepository != null && starRepository != null) {
			// sort(curSort, true);
			@SuppressWarnings("rawtypes")
			List list = sort(curSort, true);
			if (list != null) {
				viewRepository.clear();
				viewRepository.addAll(list);
			}
			CacheUtils.cacheObject(CacheUtils.NAME.LIST_OWN_REPOSITORY,
					myRepository);
			CacheUtils.cacheObject(CacheUtils.NAME.LIST_STAR_REPOSITORY,
					starRepository);
			return true;
		}
		return null;
	}

	@Override
	public void onSuccess(Boolean result) {
		// TODO Auto-generated method stub
		super.onSuccess(result);
		repositoryAdapter.notifyDataSetChanged();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List sort(Sort sort, boolean isRefresh) {
		if (sort == curSort && !isRefresh)
			return null;
		curSort = sort;
		long temp = System.currentTimeMillis();
		GLog.sysout("sort begin:" + temp);
		List viewRepository = new ArrayList();
		switch (sort) {
		case Own:
			viewRepository.addAll(myRepository);
			break;
		case Star:
			viewRepository.addAll(starRepository);
			break;
		default:
			viewRepository.addAll(myRepository);
			viewRepository.addAll(starRepository);
			break;
		}
		Collections.sort(viewRepository, new RepositoryComparator(sort));
		// GLog.sysout("" + TestUtils.printListRepository(viewRepository));
		// 增加 字母
		if (!alphaIndex.isEmpty())
			alphaIndex.clear();
		if (sort == Sort.Time)
			return viewRepository;
		for (int i = 0; i < viewRepository.size(); i++) {
			Repository repo = (Repository) viewRepository.get(i);
			char alpha = repo.getName().charAt(0);
			if (sort == Sort.User)
				alpha = repo.getOwner().getLogin().charAt(0);
			else
				alpha = repo.getName().charAt(0);
			if (!Character.isLetter(alpha)) {
				alpha = '#';
			}
			alpha = Character.toUpperCase(alpha);
			if (!alphaIndex.containsKey(alpha)) {
				alphaIndex.put(alpha, i);
				viewRepository.add(i, alpha);
			}
		}
		GLog.sysout("sort time:" + (System.currentTimeMillis() - temp));
		return viewRepository;
	}

	private void asyncSort(final Sort sort, final boolean isRefresh) {
		if (isSorting) {
			ToastUtils.show(context,
					context.getResources().getString(R.string.sorting));
			return;
		}
		new SimpleUpdateTask(new SimpleUpdateTask.UpdateListener() {

			@Override
			public void onPrev() {
				// showProgress();
				isSorting = true;
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onExcute() {
				@SuppressWarnings("rawtypes")
				List list = sort(sort, isRefresh);
				if (list != null) {
					viewRepository.clear();
					viewRepository.addAll(list);
				}
			}

			@Override
			public void onSuccess() {
				// dismissProgress();
				isSorting = false;
				if (sort == Sort.Time)
					ViewUtils.setVisibility(az, View.GONE);
				else
					ViewUtils.setVisibility(az, View.VISIBLE);
				repositoryAdapter.notifyDataSetChanged();
			}
		}).execute();
	}

	@Override
	public void onRefresh() {
		execute();
	}

}
