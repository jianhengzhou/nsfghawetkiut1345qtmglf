package com.gdestiny.github.ui.activity;

import com.gdestiny.github.R;
import com.gdestiny.github.async.BaseAsyncTask;
import com.gdestiny.github.async.LoadingTask;
import com.gdestiny.github.utils.ToastUtils;
import com.gdestiny.github.utils.ViewUtils;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public abstract class BaseLoadFragmentActivity<Params, Result> extends
		BaseFragmentActivity implements OnRefreshListener,
		LoadingTask<Params, Result> {

	private PullToRefreshLayout pullToRefreshLayout;
	private boolean isLoading = false;

	private View noDataView;
	private boolean noData;

	private boolean exception = false;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			BaseLoadFragmentActivity.this.onException((Exception) msg.obj);
		}

	};

	public void setContentView(int id, int refreshId) {

		setContentView(R.layout.layout_nodata);

		View content = LayoutInflater.from(context).inflate(id, null);
		FrameLayout container = (FrameLayout) findViewById(R.id.nodata_content);
		container.addView(content);
		noDataView = findViewById(R.id.nodata);

		pullToRefreshLayout = (PullToRefreshLayout) findViewById(refreshId);
		ActionBarPullToRefresh.from(context).allChildrenArePullable()
				.listener(this).setup(pullToRefreshLayout);
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
		if (exception) {
			ToastUtils.show(context, ex.getMessage());
			exception = false;
		}
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
