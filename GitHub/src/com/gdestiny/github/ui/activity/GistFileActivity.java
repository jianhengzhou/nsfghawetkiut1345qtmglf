package com.gdestiny.github.ui.activity;

import java.util.Map;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.GistService;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.view.FileIndicatorView;
import com.gdestiny.github.utils.Constants;

public class GistFileActivity extends
		BaseLoadFragmentActivity<GitHubClient, Gist> {

	private Map<String, GistFile> files;

	private Gist gist;
	private String fileName;
	private TextView tv;

	private FileIndicatorView indicator;

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
		tv = (TextView) findViewById(R.id.text);
		tv.setHorizontallyScrolling(true);

		ScrollView scroll = (ScrollView) findViewById(R.id.scrollview);
		scroll.setFillViewport(true);
		scroll.setHorizontalScrollBarEnabled(true);
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
		execute(GitHubApplication.getClient());
	}

	private void onFileContent(final String fileName) {
		// tv.post(new Runnable() {
		//
		// @Override
		// public void run() {
		String content = files.get(fileName).getContent();
		tv.setText(content);
		// }
		// });
	}

	@Override
	public Gist onBackground(GitHubClient params) throws Exception {
		// TODO Auto-generated method stub
		GistService service = new GistService(params);
		return service.getGist(gist.getId());
	}

	@Override
	public void onSuccess(Gist result) {
		super.onSuccess(result);
		gist = result;
		files = gist.getFiles();
		onFileContent(fileName);
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onleftLayout() {
		// TODO Auto-generated method stub
		finish();
	}

}
