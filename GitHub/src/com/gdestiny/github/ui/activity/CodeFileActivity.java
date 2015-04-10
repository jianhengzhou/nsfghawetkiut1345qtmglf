package com.gdestiny.github.ui.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseLoadFragmentActivity;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.view.touchimageview.TouchImageView;
import com.gdestiny.github.utils.CacheUtils;
import com.gdestiny.github.utils.CommonUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.ImageUtils;
import com.gdestiny.github.utils.MarkdownUtils;
import com.gdestiny.github.utils.ToastUtils;
import com.gdestiny.github.utils.ViewUtils;

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

	private ScrollView scroll;
	private TextView tv;
	private WebView webview;
	private TouchImageView normalImageView;
	private GifImageView gifImageView;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_code_file, R.id.pull_refresh_layout);
	}

	@Override
	protected void initView() {
		scroll = (ScrollView) findViewById(R.id.scrollview);
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

		String path = null;
		if (treeEntry != null) {
			path = treeEntry.getPath();
			sha = treeEntry.getSha();
		} else if (commitFile != null) {
			path = commitFile.getFilename();
			sha = commitFile.getSha();
		} else {
			ToastUtils.show(context, "not fount(404)");
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

		execute(GitHubApplication.getClient());
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
		Blob blob = dataService.getBlob(repository, sha);

		if (fileType == FILETYPE.MD) {
			MarkdownService mdSerview = new MarkdownService(params);
			String mdText = new String(EncodingUtils.fromBase64(blob
					.getContent()), "utf-8");
			if (mdText != null) {
				if (repository != null) {
					return mdSerview.getRepositoryHtml(repository, mdText);
				}
				return mdSerview.getHtml(mdText, MarkdownService.MODE_GFM);

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
		super.onSuccess(result);
		try {
			// cache
			byte[] data = null;
			if (result instanceof Blob)
				data = EncodingUtils.fromBase64(((Blob) result).getContent());
			else if (result instanceof String) {
				if (fileType == FILETYPE.IMG)
					onNormalImage((String) result);
				else if (fileType == FILETYPE.MD)
					onMdHtml((String) result);
				else
					onGifImage((String) result);
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
				onMdHtml((String) result);
				break;
			case PIC_IN_CACHE:
				break;
			default:
				onOther(data);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			noData(true);
		}
	}

	private void onNormalImage(byte[] data) {
		GLog.sysout("onNormalImage");
		normalImageView = (TouchImageView) findViewById(R.id.imageview);
		ViewUtils.setVisibility(normalImageView, View.VISIBLE);

		Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);

		normalImageView.setImageBitmap(bm);
	}

	private void onNormalImage(String path) {
		GLog.sysout("onNormalImage file");
		normalImageView = (TouchImageView) findViewById(R.id.imageview);
		ViewUtils.setVisibility(normalImageView, View.VISIBLE);

		ImageLoaderUtils.displayImage("file://" + path, normalImageView,
				R.color.transparent, false);
	}

	private void onGifImage(byte[] data) throws IOException {
		GLog.sysout("onGifImage");
		gifImageView = (GifImageView) findViewById(R.id.gif_imageview);
		ViewUtils.setVisibility(gifImageView, View.VISIBLE);

		GifDrawable gifDrawable = new GifDrawable(data);

		// ImageLoaderUtils.cacheBitmap(treeEntry.getUrl(), gifDrawable);

		gifImageView.setImageDrawable(gifDrawable);
	}

	private void onGifImage(String path) throws IOException {
		GLog.sysout("onGifImage");
		gifImageView = (GifImageView) findViewById(R.id.gif_imageview);
		ViewUtils.setVisibility(gifImageView, View.VISIBLE);

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

	@SuppressLint("SetJavaScriptEnabled")
	private void onMdHtml(String content) throws Exception {
		GLog.sysout("onMdHtml:\n");
		webview = (WebView) findViewById(R.id.webview);
		ViewUtils.setVisibility(webview, View.VISIBLE);

		WebSettings websetting = webview.getSettings();
		websetting.setJavaScriptEnabled(true);
		websetting.setSupportZoom(true);
		websetting.setBuiltInZoomControls(true);

		webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
	}

	private void onOther(byte[] data) throws UnsupportedEncodingException {
		GLog.sysout("onOther");
		tv = (TextView) findViewById(R.id.text);
		ViewUtils.setVisibility(scroll, View.VISIBLE);

		String str = new String(data, "utf-8");
		tv.setText(str);
		tv.setHorizontallyScrolling(true);

		ScrollView scroll = (ScrollView) findViewById(R.id.scrollview);
		scroll.setFillViewport(true);
		scroll.setHorizontalScrollBarEnabled(true);
	}

	@Override
	protected void onleftLayout() {
		finish();
	}

	@Override
	public void onRefresh() {
		execute();
	}

}
