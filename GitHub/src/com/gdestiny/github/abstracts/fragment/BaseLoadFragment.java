package com.gdestiny.github.abstracts.fragment;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.BaseAsyncTask;
import com.gdestiny.github.abstracts.interfaces.LoadingTask;
import com.gdestiny.github.abstracts.interfaces.OnRefreshListener;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.ToastUtils;
import com.gdestiny.github.utils.ViewUtils;

import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public abstract class BaseLoadFragment<Params, Result> extends BaseFragment
		implements OnRefreshListener, LoadingTask<Params, Result> {

	private SwipeRefreshLayout swipeRefreshLayout;

	private boolean isLoading = false;
	private View noDataView;
	private boolean noData;

	private boolean exception = false;
	private boolean loadCache = false;

	public boolean isLoadCache() {
		return loadCache;
	}

	public void setLoadCache(boolean loadCache) {
		this.loadCache = loadCache;
	}

	@SuppressWarnings("deprecation")
	public void setContentView(LayoutInflater inflater, int id, int refreshId) {
		// nodata 界面
		setContentView(inflater, R.layout.layout_nodata);

		// 添加主界面
		View content = inflater.inflate(id, null);
		FrameLayout container = (FrameLayout) findViewById(R.id.nodata_content);
		container.addView(content);

		// 同步颜色
		// getCurrentView().setBackground(content.getBackground());

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(refreshId);
		swipeRefreshLayout.setColorScheme(R.color.common_icon_blue);
		swipeRefreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

					@Override
					public void onRefresh() {
						BaseLoadFragment.this.onRefresh();
					}
				});

		noDataView = findViewById(R.id.nodata);
	}

	public SwipeRefreshLayout getSwipeRefreshLayout() {
		return swipeRefreshLayout;
	}

	public void showProgress() {
		isLoading = true;

		if (swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing()) {
			swipeRefreshLayout.post(new Runnable() {

				@Override
				public void run() {
					synchronized (swipeRefreshLayout) {
						swipeRefreshLayout.setRefreshing(true);
					}
				}
			});
		}
	}

	public void dismissProgress() {
		isLoading = false;
		if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
			swipeRefreshLayout.post(new Runnable() {

				@Override
				public void run() {
					synchronized (swipeRefreshLayout) {
						swipeRefreshLayout.setRefreshing(false);
					}
				}
			});
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
			ViewUtils.setVisibility(noDataView, View.VISIBLE);
		} else {
			ViewUtils.setVisibility(noDataView, View.GONE);
		}
	}

	@Override
	public void onPrev() {
		// if (!loadCache)
		showProgress();
	}

	public String getCacheName() {
		return "";
	}

	public String getSubDir() {
		return null;
	}

	@Override
	public Result onBackground(Params params) throws Exception {
		GLog.sysout("onBackground");
		return null;
	}

	@Override
	public void onSuccess(Result result) {
		GLog.sysout("onSuccess");
		dismissProgress();
	}

	@Override
	public void onException(Exception ex) {
		ex.printStackTrace();
		GLog.sysout("onException");
		dismissProgress();

		String message = ex.getMessage();
		if (TextUtils.isEmpty(message))
			message = "Unknow Error";
		if (exception) {
			ToastUtils.show(context, message);
			exception = false;
		}
	}

	public void execute() {
		execute(null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Params params) {
		new BaseAsyncTask<Params, Void, Result>() {

			@Override
			protected void onPostExecute(Result result) {
				super.onPostExecute(result);
				if (result != null)
					BaseLoadFragment.this.onSuccess(result);
				else {
					BaseLoadFragment.this.onException(new Exception(
							getResources().getString(R.string.network_error)));
				}
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				BaseLoadFragment.this.onPrev();
			}

			@Override
			protected void onProgressUpdate(Void... values) {
				super.onProgressUpdate(values);
			}

			@Override
			protected Result doInBackground(Params... params) {
				try {
					if (params == null)
						return BaseLoadFragment.this.onBackground(null);
					return BaseLoadFragment.this.onBackground(params[0]);
				} catch (final Exception e) {
					exception = true;
					getCurrentView().post(new Runnable() {

						@Override
						public void run() {
							BaseLoadFragment.this.onException(e);
							GLog.sysout("Exception");
						}
					});
					return null;
				}
			}
		}.execute(params);
	}

}
