package com.gdestiny.github.abstracts.activity;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.BaseAsyncTask;
import com.gdestiny.github.abstracts.interfaces.LoadingTask;
import com.gdestiny.github.abstracts.interfaces.OnRefreshListener;
import com.gdestiny.github.utils.ToastUtils;
import com.gdestiny.github.utils.ViewUtils;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public abstract class BaseLoadFragmentActivity<Params, Result> extends
		BaseFragmentActivity implements OnRefreshListener,
		LoadingTask<Params, Result> {

	private SwipeRefreshLayout swipeRefreshLayout;
	private boolean isLoading = false;

	private View noDataView;
	private boolean noData;
	private boolean loadCache = false;

	public boolean isLoadCache() {
		return loadCache;
	}

	public void setLoadCache(boolean loadCache) {
		this.loadCache = loadCache;
	}

	public String getCacheName() {
		return "";
	}

	public String getSubDir() {
		return null;
	}

	private boolean exception = false;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			BaseLoadFragmentActivity.this.onException((Exception) msg.obj);
		}

	};

	@SuppressWarnings("deprecation")
	public void setContentView(int id, int refreshId) {

		setContentView(R.layout.layout_nodata);

		View content = LayoutInflater.from(context).inflate(id, null);
		FrameLayout container = (FrameLayout) findViewById(R.id.nodata_content);
		container.addView(content);
		noDataView = findViewById(R.id.nodata);

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(refreshId);
		swipeRefreshLayout.setColorScheme(R.color.common_icon_blue);
		swipeRefreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

					@Override
					public void onRefresh() {
						BaseLoadFragmentActivity.this.onRefresh();
					}
				});
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
		showProgress();
	}

	@Override
	public Result onBackground(Params params) throws Exception {
		return null;
	}

	@Override
	public void onSuccess(Result result) {
		dismissProgress();
	}

	@Override
	public void onException(Exception ex) {
		dismissProgress();
		ex.printStackTrace();
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
				if (result != null) {
					BaseLoadFragmentActivity.this.onSuccess(result);
				} else {
					noData(true);
					BaseLoadFragmentActivity.this.onException(new Exception(
							getResources().getString(R.string.network_error)));
				}
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				BaseLoadFragmentActivity.this.onPrev();
				exception = false;
			}

			@Override
			protected void onProgressUpdate(Void... values) {
				super.onProgressUpdate(values);
			}

			@Override
			protected Result doInBackground(Params... params) {
				try {
					if (params == null)
						return BaseLoadFragmentActivity.this.onBackground(null);
					return BaseLoadFragmentActivity.this
							.onBackground(params[0]);
				} catch (Exception e) {
					exception = true;
					Message msg = handler.obtainMessage();
					msg.obj = e;
					handler.sendMessage(msg);
				}
				return null;
			}
		}.execute(params);
	}

}
