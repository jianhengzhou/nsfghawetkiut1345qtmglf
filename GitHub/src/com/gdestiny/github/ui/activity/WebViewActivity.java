package com.gdestiny.github.ui.activity;

import static org.eclipse.egit.github.core.client.IGitHubConstants.CHARSET_UTF8;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ZoomButtonsController;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseLoadFragmentActivity;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;

public class WebViewActivity extends BaseLoadFragmentActivity<Void, Void> {

	private String url = "";
	private String currUrl;
	private WebView webview;
	private String data;

	@Override
	public void onRefresh() {
		webview.loadUrl(currUrl);
	}

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.act_webview, R.id.pull_refresh_layout);
	}

	@Override
	protected void initActionBar(TitleBar titleBar) {
		super.initActionBar(titleBar);
		titleBar.findViewById(R.id.title_left_layout).setOnLongClickListener(
				new View.OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						if (!TextUtils.isEmpty(currUrl)) {
							AndroidUtils.toClipboard(context, currUrl);
						}
						return true;
					}
				});
		LinkedHashMap<Integer, Integer> itemmap = new LinkedHashMap<Integer, Integer>();
		itemmap.put(R.string.open_with, R.drawable.common_browser);
		itemmap.put(R.string.refresh, R.drawable.common_status_refresh);

		StatusPopUpWindow.StatusPopUpWindowItemClickListener menuListener = new StatusPopUpWindow.StatusPopUpWindowItemClickListener() {

			@Override
			public void onitemclick(int titleId) {
				GLog.sysout(context.getResources().getString(titleId) + "");
				switch (titleId) {
				case R.string.refresh:
					webview.loadUrl(url);
					break;
				case R.string.open_with:
					webview.loadUrl(url);
					AndroidUtils.openUrlWith(context, currUrl);
					break;
				}
				titlebar.dissmissStatus();
			}
		};
		titlebar.setStatusItem(context, itemmap, menuListener);

	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		webview = (WebView) findViewById(R.id.webview);

		webview.setInitialScale(0);

		WebSettings websetting = webview.getSettings();
		websetting.setJavaScriptEnabled(true);
		websetting.setSupportZoom(true);
		websetting.setBuiltInZoomControls(true);
		websetting.setUseWideViewPort(true);// êPæIüc
		websetting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		websetting.setLoadWithOverviewMode(true);

		// È¥µôËõ·Å°´Å¥
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			// Use the API 11+ calls to disable the controls
			websetting.setBuiltInZoomControls(true);
			websetting.setDisplayZoomControls(false);
		} else {
			try {
				@SuppressWarnings("rawtypes")
				Class webview = Class.forName("android.webkit.WebView");
				@SuppressWarnings("unchecked")
				Method method = webview.getMethod("getZoomButtonsController");
				ZoomButtonsController zoom = (ZoomButtonsController) method
						.invoke(this, true);
				zoom.setVisible(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		webview.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				currUrl = url;
				getTitlebar().setTitleText(currUrl);
				showProgress();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				dismissProgress();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

		});
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		url = getIntent().getStringExtra(Constants.Extra.URL);
		data = getIntent().getStringExtra(Constants.Extra.DATA);
		if (TextUtils.isEmpty(url)) {
			if (TextUtils.isEmpty(data))
				finish();
			else {
				webview.loadDataWithBaseURL(null, data, "text/html",
						CHARSET_UTF8, null);
			}
		} else {
			getTitlebar().setTitleIcon(null);
			webview.loadUrl(url);
		}
	}

	@Override
	public void onBackPressed() {
		if (webview.canGoBack()) {
			webview.goBack();
			return;
		}
		super.onBackPressed();
	}

	@Override
	protected void onleftLayout() {
		finish();
	}

}
