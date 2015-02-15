package com.gdestiny.github.ui.activity;

import java.io.File;
import java.util.List;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.CommentAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.TimeUtils;

public class IssueDetailActivity extends
		BaseLoadFragmentActivity<GitHubClient, List<Comment>> {

	public static final String EXTRA_ISSUE = "issue";
	public static final String EXTRA_IREPOSITORY = "irepository";

	private Issue issue;
	private Repository repository;

	private View detailView;

	private ListView commentList;
	private CommentAdapter commentAdapter;

	private TextView content;
	private boolean fold = false;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.act_issue_detail, R.id.pull_refresh_layout);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		commentList = (ListView) findViewById(R.id.list);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		issue = (Issue) getIntent().getSerializableExtra(EXTRA_ISSUE);
		repository = (Repository) getIntent().getSerializableExtra(
				EXTRA_IREPOSITORY);

		getTitlebar().setLeftLayout(
				repository.getOwner().getAvatarUrl(),
				"issue #" + issue.getNumber(),
				repository.getOwner().getLogin() + File.separator
						+ repository.getName());

		detailView = LayoutInflater.from(context).inflate(
				R.layout.layout_issue_detail, null);
		commentList.addHeaderView(detailView);
		commentAdapter = new CommentAdapter(context);
		commentList.setAdapter(commentAdapter);
		
		execute(GitHubApplication.getClient());

		ImageView icon = (ImageView) detailView.findViewById(R.id.icon);
		ImageLoaderUtils.displayImage(issue.getUser().getAvatarUrl(), icon,
				R.drawable.default_avatar, R.drawable.default_avatar, true);

		TextView title = (TextView) detailView.findViewById(R.id.title);
		title.setText(issue.getTitle());

		TextView name = (TextView) detailView.findViewById(R.id.name);
		name.setText(issue.getUser().getLogin());

		TextView date = (TextView) detailView.findViewById(R.id.date);
		date.setText(TimeUtils.getTime(issue.getCreatedAt().getTime()));

		TextView comment = (TextView) detailView.findViewById(R.id.comment);
		comment.setText(issue.getComments() + "");

		content = (TextView) detailView.findViewById(R.id.content);
		content.setText(issue.getBody());
		content.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!fold) {
					fold = true;
					content.setMaxLines(4);
				} else {
					fold = false;
					content.setMaxLines(Integer.MAX_VALUE);
				}
			}
		});
	}

	@Override
	public List<Comment> onBackground(GitHubClient params) throws Exception {
		// TODO Auto-generated method stub
		IssueService service = new IssueService(params);
		return service.getComments(repository, issue.getNumber());
	}

	@Override
	public void onSuccess(List<Comment> result) {
		// TODO Auto-generated method stub
		super.onSuccess(result);
		commentAdapter.setDatas(result);
	}

	@Override
	protected void onleftLayout() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub

	}

}
