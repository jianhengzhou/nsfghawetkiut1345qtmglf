package com.gdestiny.github.ui.activity;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.User;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseLoadFragmentActivity;
import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.view.FileIndicatorView;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.SourceEditor;

public class GistFileActivity extends BaseLoadFragmentActivity<Void, Gist> {

	private Map<String, GistFile> files;

	private Gist gist;
	private String fileName;

	private FileIndicatorView indicator;

	private SourceEditor sourceEditor;
	private WebView webview;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.act_gist_file, R.id.pull_refresh_layout);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		indicator = (FileIndicatorView) findViewById(R.id.tab);
		indicator.setOnTabListener(new FileIndicatorView.TabClickListener() {

			@Override
			public void onTabClick(String file) {
				// TODO Auto-generated method stub
				System.out.println(file);
				onFileContent(file);
			}
		});

		webview = (WebView) findViewById(R.id.webview);
		WebSettings websetting = webview.getSettings();
		websetting.setSupportZoom(true);
		websetting.setBuiltInZoomControls(true);
		sourceEditor = new SourceEditor(webview);
		webview.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				dismissProgress();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (SourceEditor.SOURCE_EDITOR.equals(url)) {
					view.loadUrl(url);
					return false;
				} else {
					return true;
				}
			}
		});
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		gist = (Gist) getIntent().getSerializableExtra(Constants.Extra.GIST);
		fileName = getIntent().getStringExtra(Constants.Extra.FILE_NAME);
		User user = gist.getUser();
		if (user != null) {
			getTitlebar().setLeftLayout(user.getAvatarUrl(), gist.getId(),
					user.getLogin());
		} else {
			getTitlebar().setTitleText(gist.getId())
					.setTitleTextSecondly(R.string.anonymous)
					.setTitleIcon(R.drawable.common_anonymous_round);
		}

		indicator.add(fileName, gist.getFiles().keySet());
		execute();
	}

	private void onFileContent(final String fileName) {
		String content = files.get(fileName).getContent();
		sourceEditor.setSource(fileName, content);
	}

	@Override
	public Gist onBackground(Void params) throws Exception {
		// TODO Auto-generated method stub
		return GitHubConsole.getInstance().getGist(gist.getId());
	}

	@Override
	public void onSuccess(Gist result) {
		super.onSuccess(result);
		gist = result;
		files = gist.getFiles();
		onFileContent(fileName);
	}

	@Override
	public void onRefresh() {
		execute();
	}

	@Override
	protected void initActionBar(TitleBar titleBar) {
		super.initActionBar(titleBar);
		LinkedHashMap<Integer, Integer> itemmap = new LinkedHashMap<Integer, Integer>();
		itemmap.put(R.string.copy, R.drawable.common_icon_save);
		itemmap.put(R.string.wrap, R.drawable.common_wrap);
		itemmap.put(R.string.refresh, R.drawable.common_status_refresh);

		StatusPopUpWindow.StatusPopUpWindowItemClickListener menuListener = new StatusPopUpWindow.StatusPopUpWindowItemClickListener() {

			@Override
			public void onitemclick(int titleId) {
				switch (titleId) {
				case R.string.refresh:
					if (!isLoading())
						onRefresh();
					break;
				case R.string.wrap:
					showProgress();
					sourceEditor.toggleWrap();
					if (sourceEditor.getWrap()) {
						getTitlebar().getStatusPopup().getAction(1).mTitle = "Unwrap";
					} else {
						getTitlebar().getStatusPopup().getAction(1).mTitle = "Wrap";
					}
					break;
				case R.string.copy:
					AndroidUtils.toClipboard(context, files.get(fileName)
							.getContent());
					break;
				}
				titlebar.dissmissStatus();
			}
		};
		titlebar.setStatusItem(context, itemmap, menuListener);
	}

	@Override
	protected void onleftLayout() {
		// TODO Auto-generated method stub
		finish();
	}

}
