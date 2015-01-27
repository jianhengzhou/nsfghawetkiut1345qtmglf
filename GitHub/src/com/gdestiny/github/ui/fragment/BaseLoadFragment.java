package com.gdestiny.github.ui.fragment;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.ViewUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public abstract class BaseLoadFragment extends BaseFragment implements
		OnRefreshListener {

	private PullToRefreshLayout pullToRefreshLayout;
	private boolean isLoading = false;
	private View noDataView;
	private boolean noData;

	public void setContentView(LayoutInflater inflater, int id, int refreshId) {
		// nodata 界面
		setContentView(inflater, R.layout.layout_nodata);

		// 添加主界面
		View content = inflater.inflate(id, null);
		FrameLayout container = (FrameLayout) findViewById(R.id.nodata_content);
		container.addView(content);

		// 同步颜色
		getCurrentView().setBackground(content.getBackground());

		pullToRefreshLayout = (PullToRefreshLayout) findViewById(refreshId);
		ActionBarPullToRefresh.from(context).allChildrenArePullable()
				.listener(this).setup(pullToRefreshLayout);

		noDataView = findViewById(R.id.nodata);
	}

	public PullToRefreshLayout getPullToRefreshLayout() {
		return pullToRefreshLayout;
	}

	public void showProgress() {
		isLoading = true;
		if (pullToRefreshLayout != null && !pullToRefreshLayout.isRefreshing()) {
			pullToRefreshLayout.setRefreshing(true);
		}
	}

	public void dismissProgress() {
		isLoading = false;
		if (pullToRefreshLayout != null && pullToRefreshLayout.isRefreshing()) {
			pullToRefreshLayout.setRefreshing(false);
		}
	}

	public boolean isLoading() {
		return isLoading;
	}

	public boolean isNoData() {
		return noData;
	}

	public void noData(boolean noData) {
		this.noData = noData;
		if (noData) {
			ViewUtils.setVisibility(noDataView, View.VISIBLE, R.anim.alpha_in);
		} else {
			ViewUtils.setVisibility(noDataView, View.GONE, R.anim.alpha_in);
		}
	}

}
