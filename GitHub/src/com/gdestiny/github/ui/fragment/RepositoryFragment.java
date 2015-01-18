package com.gdestiny.github.ui.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.WatcherService;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.gdestiny.github.adapter.RepositoryAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.async.GitHubTask;
import com.gdestiny.github.async.GitHubTask.TaskListener;
import com.gdestiny.github.bean.comparator.RepositoryComparator;
import com.gdestiny.github.ui.activity.BaseFragmentActivity;
import com.gdestiny.github.ui.activity.RepositoryDetailActivity;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants.Sort;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.TestUtils;
import com.gdestiny.github.utils.ToastUtils;

public class RepositoryFragment extends BaseLoadFragment {

	private ListView repositoryList;
	private RepositoryAdapter repositoryAdapter;

	@SuppressWarnings("rawtypes")
	private List viewRepository;
	@SuppressWarnings("rawtypes")
	private List myRepository;
	@SuppressWarnings("rawtypes")
	private List starRepository;

	private Sort curSort = Sort.All;
	private int currAlphbet = -1;
	private HashMap<Character, Integer> alphaIndex = new HashMap<Character, Integer>();

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.currentView = inflater.inflate(R.layout.frag_repository, null);
		return this.currentView;
	}

	@Override
	protected void initView() {
		repositoryList = (ListView) this.currentView
				.findViewById(R.id.repository_list);
		repositoryList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				// IntentUtils.start(context, RepositoryDetailActivity.class,
				// RepositoryDetailActivity.data,
				// (Repository) viewRepository.get(position));
			}
		});
		this.pullToRefreshLayout = (PullToRefreshLayout) this.currentView
				.findViewById(R.id.pull_refresh_layout);
		ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable()
				.listener(this).setup(pullToRefreshLayout);
		initStatusPopup(((BaseFragmentActivity) context).getTitlebar());

	}

	private void initStatusPopup(final TitleBar title) {
		LinkedHashMap<Integer, Integer> itemmap = new LinkedHashMap<Integer, Integer>();
		itemmap.put(R.string.sort, R.drawable.common_status_sort);
		itemmap.put(R.string.refresh, R.drawable.common_status_refresh);
		// 二级菜单
		title.initSecondly();
		LinkedHashMap<Integer, Integer> itemmapSecondly = new LinkedHashMap<Integer, Integer>();
		itemmapSecondly.put(R.string.all, R.drawable.common_code_grey);
		itemmapSecondly.put(R.string.star, R.drawable.common_star_grey);
		itemmapSecondly.put(R.string.own, R.drawable.common_own_people_grey);
		itemmapSecondly.put(R.string.name, R.drawable.common_name_grey);
		itemmapSecondly.put(R.string.user, R.drawable.circle_user_grey);
		itemmapSecondly.put(R.string.time, R.drawable.common_time_grey);
		title.setSecondlyStatusItem(context, itemmapSecondly);
		title.setStatusItem(context, itemmap,
				new StatusPopUpWindow.StatusPopUpWindowItemClickListener() {

					@Override
					public void onitemclick(int titleId) {
						// TODO Auto-generated method stub
						GLog.sysout(context.getResources().getString(titleId)
								+ "");
						boolean dismiss = true;
						switch (titleId) {
						case R.string.refresh:
							getRepository();
							break;
						case R.string.sort:
							dismiss = false;
							title.showSecondly();
							break;
						case R.string.all:
							break;
						case R.string.star:
							break;
						case R.string.own:
							break;
						}
						if (dismiss)
							title.dissmissStatus();
					}
				});
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initData() {
		viewRepository = new ArrayList();

		repositoryAdapter = new RepositoryAdapter(context, viewRepository);
		repositoryList.setAdapter(repositoryAdapter);

		initAlaphtSort();
		getRepository();
	}

	private void initAlaphtSort() {
		final ImageView az = (ImageView) this.currentView
				.findViewById(R.id.a_z_alphabet);
		final TextView azToast = (TextView) this.currentView
				.findViewById(R.id.alphabet_toast);
		az.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int totalHeight = az.getHeight();
				float y = event.getY();
				int position = (int) ((y / totalHeight) / (1f / 27f));

				char ch = 0;
				if (position == 0)
					ch = '#';
				else if (position <= 26)
					ch = (char) (position - 1 + 'A');
				else if (position > 26)
					ch = 'z';

				if (currAlphbet != position) {
					azToast.setText(ch + "");
					currAlphbet = position;
					GLog.sysout(ch + "");
					if (alphaIndex.containsKey(ch))
						repositoryList.setSelection(alphaIndex.get(ch));
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

	private void getRepository() {
		new GitHubTask<Boolean>(new TaskListener<Boolean>() {

			@Override
			public void onPrev() {
				showProgress();
			}

			@Override
			public Boolean onExcute(GitHubClient client) {
				try {
					myRepository = new RepositoryService(
							GitHubApplication.getClient()).getRepositories();
					starRepository = new WatcherService(
							GitHubApplication.getClient()).getWatched();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
				if (myRepository != null && starRepository != null)
					return true;
				return null;
			}

			@Override
			public void onSuccess(Boolean result) {
				sort(curSort);
				repositoryAdapter.notifyDataSetChanged();
				dismissProgress();
			}

			@Override
			public void onError() {
				dismissProgress();
				ToastUtils.show(context,
						getResources().getString(R.string.network_error));

			}
		}).execute(GitHubApplication.getClient());
	}

	@SuppressWarnings({ "unchecked" })
	private void sort(Sort sort) {
		long temp = System.currentTimeMillis();
		GLog.sysout("sort begin:" + temp);
		if (!viewRepository.isEmpty())
			viewRepository.clear();
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
		GLog.sysout("" + TestUtils.printListRepository(viewRepository));
		// 增加 字母
		if (!alphaIndex.isEmpty())
			alphaIndex.clear();
		for (int i = 0; i < viewRepository.size(); i++) {
			Repository repo = (Repository) viewRepository.get(i);
			char alpha = repo.getName().charAt(0);
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
	}

	@Override
	public void onRefreshStarted(View view) {
		getRepository();
	}

}
