package com.gdestiny.github.ui.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.eclipse.egit.github.core.Blob;
import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.TreeEntry;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.DataService;
import org.eclipse.egit.github.core.service.MarkdownService;
import org.eclipse.egit.github.core.util.EncodingUtils;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseLoadFragmentActivity;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.ui.view.touchimageview.TouchImageView;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.CacheUtils;
import com.gdestiny.github.utils.CommonUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.ImageUtils;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.SourceEditor;
import com.gdestiny.github.utils.ToastUtils;
import com.gdestiny.github.utils.client.MarkdownUtils;

public class CodeFileActivity extends
		BaseLoadFragmentActivity<GitHubClient, Serializable> {

	public static enum FILETYPE {
		IMG, GIF, MD, OTHER, PIC_IN_CACHE
	};

	private Repository repository;
	private TreeEntry treeEntry;
	private CommitFile commitFile;
	private String sha;

	private FILETYPE fileType;

	private SourceEditor sourceEditor;
	private String path;
	private WebView webview;
	private TouchImageView normalImageView;
	private GifImageView gifImageView;

	private String markdownRaw = null;
	private String markdown = null;
	private Blob blob;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_code_file, R.id.pull_refresh_layout);
	}

	@Override
	protected void initActionBar(TitleBar titleBar) {
		super.initActionBar(titleBar);
		LinkedHashMap<Integer, Integer> itemmap = new LinkedHashMap<Integer, Integer>();
		itemmap.put(R.string.save, R.drawable.common_icon_save);
		itemmap.put(R.string.refresh, R.drawable.common_status_refresh);

		StatusPopUpWindow.StatusPopUpWindowItemClickListener menuListener = new StatusPopUpWindow.StatusPopUpWindowItemClickListener() {

			@Override
			public void onitemclick(int titleId) {
				switch (titleId) {
				case R.string.refresh:
					if (!isLoading())
						onRefresh();
					break;
				case R.string.share:
					AndroidUtils.share(context, repository.getName(),
							"https://github.com/" + repository.generateId()
									+ "/commit/");
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
				case R.string.raw:
					showProgress();
					sourceEditor.toggleMarkdown();
					if (!sourceEditor.isMarkdown()) {
						getTitlebar().getStatusPopup().getAction(2).mTitle = "Markdown";
						sourceEditor.setContent(markdownRaw);
					} else {
						getTitlebar().getStatusPopup().getAction(2).mTitle = "Raw";
						sourceEditor.setContent(markdown);
					}
					break;
				case R.string.save:
					IntentUtils
							.create(context, SaveAsActivity.class)
							.putExtra(Constants.Extra.NAME,
									CommonUtils.pathToName(path))
							.putExtra(Constants.Extra.BLOB, blob).start();
					break;
				default:
					ToastUtils.show(context, "TODO "
							+ context.getResources().getString(titleId));
					break;
				}
				titlebar.dissmissStatus();
			}
		};
		titlebar.setStatusItem(context, itemmap, menuListener);
	}

	@Override
	protected void initView() {
	}

	public String testPost(String name, String code) {
		String url = "http://prettify.bsdn.org/";
		HttpPost httpRequest = new HttpPost(url);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("lang", "java"));
		try {
			// 发出HTTP request
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// 取得HTTP response
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			return EntityUtils.toString(httpResponse.getEntity());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		treeEntry = (TreeEntry) getIntent().getSerializableExtra(
				Constants.Extra.CODE_ENTRY);
		repository = (Repository) getIntent().getSerializableExtra(
				Constants.Extra.REPOSITORY);
		commitFile = (CommitFile) getIntent().getSerializableExtra(
				Constants.Extra.COMMIT_FILE);

		if (treeEntry != null) {
			path = treeEntry.getPath();
			sha = treeEntry.getSha();
		} else if (commitFile != null) {
			path = commitFile.getFilename();
			sha = commitFile.getSha();
		} else {
			ToastUtils.show(context, "not found(404)");
			finish();
		}

		if (ImageUtils.isImageFromPath(path)) {
			if (ImageUtils.isGifFromPath(path)) {
				fileType = FILETYPE.GIF;
			} else {
				fileType = FILETYPE.IMG;
			}
		} else if (MarkdownUtils.isMarkdown(path)) {
			fileType = FILETYPE.MD;
		} else {
			fileType = FILETYPE.OTHER;
		}

		GLog.sysout(path);
		GLog.sysout(sha);
		GLog.sysout(fileType.toString());
		getTitlebar().setLeftLayout(null, CommonUtils.pathToName(path));
		addComponent();

		execute(GitHubApplication.getClient());
	}

	private void addComponent() {
		if (fileType == FILETYPE.IMG) {
			normalImageView = (TouchImageView) LayoutInflater.from(context)
					.inflate(R.layout.layout_touchimageview, null);
			getSwipeRefreshLayout().addView(normalImageView);
		} else if (fileType == FILETYPE.GIF) {
			gifImageView = (GifImageView) LayoutInflater.from(context).inflate(
					R.layout.layout_gifimageview, null);
			getSwipeRefreshLayout().addView(gifImageView);
		} else {
			webview = (WebView) LayoutInflater.from(context).inflate(
					R.layout.layout_webview, null);
			getSwipeRefreshLayout().addView(webview);
			sourceEditor = new SourceEditor(webview);
			getTitlebar().getStatusPopup().addItem(context, 1, R.string.wrap,
					R.drawable.common_wrap);
			if (fileType == FILETYPE.MD) {
				getTitlebar().getStatusPopup().addItem(context, 2,
						R.string.raw, R.drawable.common_markdown);
				sourceEditor.setMarkdown(true);
			}

			WebSettings websetting = webview.getSettings();
			websetting.setSupportZoom(true);
			websetting.setBuiltInZoomControls(true);
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
	}

	@Override
	public Serializable onBackground(GitHubClient params) throws Exception {
		// TODO 未完成
		if (fileType == FILETYPE.IMG) {
			if (CacheUtils.isBitmapExistInDisk(sha)) {
				GLog.sysout("Image From Cache");
				String path = CacheUtils.getBitmapPath(sha);
				if (!TextUtils.isEmpty(path))
					return path;
			}
			GLog.sysout("Image From NET");
		}
		// from net
		DataService dataService = new DataService(params);
		blob = dataService.getBlob(repository, sha);

		if (fileType == FILETYPE.MD) {
			MarkdownService mdSerview = new MarkdownService(params);
			markdownRaw = new String(
					EncodingUtils.fromBase64(blob.getContent()), "utf-8");
			if (markdownRaw != null) {
				if (repository != null) {
					markdown = mdSerview.getRepositoryHtml(repository,
							markdownRaw);
					return markdown;
				}
				markdown = mdSerview.getHtml(markdownRaw,
						MarkdownService.MODE_GFM);
				return markdown;

			}
		} else if (fileType == FILETYPE.IMG) {
			String result = CacheUtils.cacheBitmap(sha,
					EncodingUtils.fromBase64((blob.getContent())));
			if (!TextUtils.isEmpty(result)) {
				return result;
			}
		}
		// fileType = FILETYPE.MD;
		// String str = testPost(CommonUtils.pathToName(treeEntry.getPath()),
		// new String(
		// EncodingUtils.fromBase64((blob).getContent()), "utf-8"));
		// System.out.println("-----------------HTML:--------------\n"+str);
		// return str;
		return blob;
	}

	@Override
	public void onSuccess(Serializable result) {
		try {
			// cache
			byte[] data = null;
			if (result instanceof Blob) {
				data = EncodingUtils.fromBase64(((Blob) result).getContent());
			} else if (result instanceof String) {
				if (fileType == FILETYPE.IMG) {
					super.onSuccess(result);
					onNormalImage((String) result);
				} else if (fileType == FILETYPE.MD) {
					onMdHtml();
				} else {
					super.onSuccess(result);
					onGifImage((String) result);
				}
				return;
			}
			// net
			switch (fileType) {
			case GIF:
				onGifImage(data);
				break;
			case IMG:
				onNormalImage(data);
				break;
			case MD:
				onMdHtml();
				break;
			case PIC_IN_CACHE:
				break;
			default:
				// onOther(data);
				onWebview(new String(data, "utf-8"));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			noData(true);
		}
	}

	private void onNormalImage(byte[] data) {
		GLog.sysout("onNormalImage");
		Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
		normalImageView.setImageBitmap(bm);
	}

	private void onNormalImage(String path) {
		GLog.sysout("onNormalImage file");
		ImageLoaderUtils.displayImage("file://" + path, normalImageView,
				R.color.transparent, false);
	}

	private void onGifImage(byte[] data) throws IOException {
		GLog.sysout("onGifImage");
		GifDrawable gifDrawable = new GifDrawable(data);
		// ImageLoaderUtils.cacheBitmap(treeEntry.getUrl(), gifDrawable);
		gifImageView.setImageDrawable(gifDrawable);
	}

	private void onGifImage(String path) throws IOException {
		GLog.sysout("onGifImage");

		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
		byte[] b = new byte[1000];
		int n;
		while ((n = fis.read(b)) != -1) {
			bos.write(b, 0, n);
		}
		fis.close();
		bos.close();
		byte[] data = bos.toByteArray();
		GifDrawable gifDrawable = new GifDrawable(data);
		gifImageView.setImageDrawable(gifDrawable);
	}

	private void onWebview(String content) {
		sourceEditor.setSource(path, content);
	}

	private void onMdHtml() throws Exception {
		GLog.sysout("onMdHtml:\n");
		if (sourceEditor.isMarkdown()) {
			onWebview(markdown);
		} else {
			onWebview(markdownRaw);
		}
	}

	@Override
	protected void onleftLayout() {
		finish();
	}

	@Override
	public void onRefresh() {
		execute(GitHubApplication.getClient());
	}

}
