package com.gdestiny.github.ui.activity;

import java.util.Map;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.User;

import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseLoadFragmentActivity;
import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.ui.view.FileIndicatorView;
import com.gdestiny.github.utils.Constants;

public class GistFileActivity extends BaseLoadFragmentActivity<Void, Gist> {

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
		execute();
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
		// TODO Auto-generated method stub

	}

	@Override
	protected void onleftLayout() {
		// TODO Auto-generated method stub
		finish();
	}

}
