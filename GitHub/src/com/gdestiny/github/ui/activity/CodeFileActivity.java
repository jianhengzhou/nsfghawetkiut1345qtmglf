package com.gdestiny.github.ui.activity;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import org.eclipse.egit.github.core.Blob;
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
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.view.touchimageview.TouchImageView;
import com.gdestiny.github.utils.CommonUtils;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.ImageUtils;
import com.gdestiny.github.utils.MarkdownUtils;
import com.gdestiny.github.utils.ViewUtils;

public class CodeFileActivity extends
		BaseLoadFragmentActivity<GitHubClient, Serializable> {

	public static final String EXTRA_CODE_ENTRY = "code_entry";
	public static final String EXTRA_CODE_REPOSITORY = "repository";

	public static enum FILETYPE {
		IMG, GIF, MD, OTHER, PIC_IN_CACHE
	};

	private Repository repository;
	private TreeEntry treeEntry;

	private FILETYPE fileType;

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

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		treeEntry = (TreeEntry) getIntent().getSerializableExtra(
				EXTRA_CODE_ENTRY);
		repository = (Repository) getIntent().getSerializableExtra(
				EXTRA_CODE_REPOSITORY);

		String path = treeEntry.getPath();
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

		getTitlebar().setLeftLayout(null,
				CommonUtils.pathToName(treeEntry.getPath()));

		execute(GitHubApplication.getClient());
	}

	@Override
	public Serializable onBackground(GitHubClient params) throws Exception {
		//TODO Î´Íê³É
		if(fileType == FILETYPE.IMG){
			if(ImageLoaderUtils.isExistedInDiskCache(treeEntry.getUrl())){
				GLog.sysout("Image From Cache");
			}else{
				GLog.sysout("Image From NET");
			}
		}
		DataService dataService = new DataService(params);
		Blob blob = dataService.getBlob(repository, treeEntry.getSha());

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
		}
		return blob;
	}

	@Override
	public void onSuccess(Serializable result) {
		super.onSuccess(result);
		byte[] data = null;
		if (result instanceof Blob)
			data = EncodingUtils.fromBase64(((Blob) result).getContent());
		try {
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

		ImageLoaderUtils.putBitmapInMemoryCache(treeEntry.getUrl(), bm);

		normalImageView.setImageBitmap(bm);
	}

	private void onGifImage(byte[] data) throws IOException {
		GLog.sysout("onGifImage");
		gifImageView = (GifImageView) findViewById(R.id.gif_imageview);
		ViewUtils.setVisibility(gifImageView, View.VISIBLE);

		GifDrawable gifDrawable = new GifDrawable(data);
		gifImageView.setImageDrawable(gifDrawable);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void onMdHtml(String content) throws Exception {
		GLog.sysout("onMdHtml:\n" + content);
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
		ViewUtils.setVisibility(tv, View.VISIBLE);

		String str = new String(data, "utf-8");
		tv.setText(str);
		tv.setHorizontallyScrolling(true);
	}

	@Override
	protected void onleftLayout() {
		finish();
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub

	}

}
