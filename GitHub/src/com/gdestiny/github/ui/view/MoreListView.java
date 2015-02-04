package com.gdestiny.github.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.ViewUtils;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class MoreListView extends ListView implements OnScrollListener {

	private View.OnClickListener onFooterClickListener;
	private CircularProgressBar progressBar;
	private TextView loadText;

	private boolean isLoading = false;
	private boolean autoLoad = false;
	private View footerView;

	private OnScrollListener onScrollListener;
	private OnAutoLoadListener onAutoLoadListener;

	public interface OnAutoLoadListener {
		public void onAutoLoad();
	}

	public MoreListView(Context context) {
		super(context);
		init();
	}

	public MoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		footerView = LayoutInflater.from(getContext()).inflate(
				R.layout.layout_loading, null);

		progressBar = (CircularProgressBar) footerView
				.findViewById(R.id.loading_progress);
		loadText = (TextView) footerView.findViewById(R.id.loading_text);

		footerView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isLoading && onFooterClickListener != null) {
					requestLoad();
					onFooterClickListener.onClick(v);
				}
			}
		});
		addFooterView(footerView);
		requestNoMore(true);
		super.setOnScrollListener(this);
	}

	public View.OnClickListener getOnFooterClickListener() {
		return onFooterClickListener;
	}

	public void setOnFooterClickListener(
			View.OnClickListener onFooterClickListener) {
		this.onFooterClickListener = onFooterClickListener;
	}

	public OnScrollListener getOnScrollListener() {
		return onScrollListener;
	}

	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}

	public OnAutoLoadListener getOnAutoLoadListener() {
		return onAutoLoadListener;
	}

	public void setOnAutoLoadListener(OnAutoLoadListener onAutoLoadListener) {
		this.onAutoLoadListener = onAutoLoadListener;
	}

	public boolean isAutoLoad() {
		return autoLoad;
	}

	public void setAutoLoad(boolean autoLoad) {
		this.autoLoad = autoLoad;
	}

	public void requestLoad() {
		if (isLoading)
			return;
		isLoading = true;
		loadText.setText(getContext().getResources()
				.getString(R.string.loading));
//		progressBar.setIndeterminate(true);
		ViewUtils.setVisibility(progressBar, View.VISIBLE);
		footerView.setEnabled(false);
	}

	public void requestLoadingFinish() {
		if (isLoading) {
			isLoading = false;
			loadText.setText(getContext().getResources().getString(
					R.string.press_to_load_more));
			ViewUtils.setVisibility(progressBar, View.GONE);
//			progressBar.setIndeterminate(false);
		}
	}

	public void requestNoMore(boolean noMore) {
		GLog.sysout("requestNoMore:" + noMore);
		if (noMore) {
			loadText.setText(getContext().getResources().getString(
					R.string.no_more_data));
			footerView.setEnabled(false);
		} else if (!footerView.isEnabled()) {
			System.out.println("requestNoMore:false");
			loadText.setText(getContext().getResources().getString(
					R.string.press_to_load_more));
			footerView.setEnabled(true);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (onScrollListener != null)
			onScrollListener.onScrollStateChanged(view, scrollState);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (onScrollListener != null)
			onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		int total = firstVisibleItem + visibleItemCount;
		if (total >= totalItemCount && firstVisibleItem != 0) {
			if (autoLoad && !isLoading && onAutoLoadListener != null) {
				requestLoad();
				onAutoLoadListener.onAutoLoad();
			}
		}
	}
}
