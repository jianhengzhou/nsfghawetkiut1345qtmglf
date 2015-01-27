package com.gdestiny.github.ui.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.eclipse.egit.github.core.Blob;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.TreeEntry;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.DataService;
import org.eclipse.egit.github.core.util.EncodingUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.async.GitHubTask;
import com.gdestiny.github.utils.CommonUtils;
import com.gdestiny.github.utils.ToastUtils;

public class CodeFileActivity extends BaseLoadFragmentActivity {

	public static final String EXTRA_CODE_ENTRY = "code_entry";
	public static final String EXTRA_CODE_REPOSITORY = "repository";

	private Repository repository;
	private TreeEntry treeEntry;

	private WebView webview;
	private TextView tv;
	private ImageView image;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_code_file, R.id.pull_refresh_layout);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		tv = (TextView) findViewById(R.id.text);
		image = (ImageView) findViewById(R.id.image);

		webview = (WebView) findViewById(R.id.webview);
		WebSettings websetting = webview.getSettings();
		websetting.setSupportZoom(true);
		websetting.setBuiltInZoomControls(true);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		treeEntry = (TreeEntry) getIntent().getSerializableExtra(
				EXTRA_CODE_ENTRY);
		repository = (Repository) getIntent().getSerializableExtra(
				EXTRA_CODE_REPOSITORY);
		getTitlebar().setLeftLayout(null,
				CommonUtils.pathToName(treeEntry.getPath()));
		getBlob();
	}

	private void getBlob() {
		new GitHubTask<Blob>(new GitHubTask.TaskListener<Blob>() {

			@Override
			public void onPrev() {
				showProgress();
			}

			@Override
			public Blob onExcute(GitHubClient client) {
				// TODO Auto-generated method stub
				DataService dataService = new DataService(client);
				Blob blob = null;
				try {
					blob = dataService.getBlob(repository, treeEntry.getSha());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return blob;
			}

			@Override
			public void onSuccess(Blob result) {
				dismissProgress();
				if (CommonUtils.isImageFromPath(treeEntry.getPath())) {
					byte[] data = EncodingUtils.fromBase64(result.getContent());
					Bitmap bm = BitmapFactory.decodeByteArray(data, 0,
							data.length);
					image.setImageBitmap(bm);
					return;
				}
				String str = null;
				try {
					str = new String(EncodingUtils.fromBase64(result
							.getContent()), "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				// webview.loadDataWithBaseURL(null, str, "text/html", "utf-8",
				// null);
				tv.setText(str);
			}

			@Override
			public void onError() {
				dismissProgress();
				ToastUtils.show(context,
						getResources().getString(R.string.network_error));
			}
		}).execute(GitHubApplication.getClient());
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
