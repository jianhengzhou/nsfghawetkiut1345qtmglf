package com.gdestiny.github.ui.activity;

import java.util.Collections;
import java.util.List;

import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.CommitStats;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CollaboratorService;
import org.eclipse.egit.github.core.service.CommitService;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.CommitExpandAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.bean.CommitLine;
import com.gdestiny.github.bean.CommitTree;
import com.gdestiny.github.bean.comparator.CommitCommentComparator;
import com.gdestiny.github.ui.dialog.CommitLineDialog;
import com.gdestiny.github.utils.CommitUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.TimeUtils;

public class CommitDetailActivity extends
		BaseLoadFragmentActivity<GitHubClient, CommitTree> {

	private Repository repository;
	private RepositoryCommit commit;

	private View detailLayout;
	private ExpandableListView commitExpandList;
	private CommitExpandAdapter commitAdapter;

	private boolean isCollaborator;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_commit_detail, R.id.pull_refresh_layout);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		commitExpandList = (ExpandableListView) findViewById(R.id.list);

		commitAdapter = new CommitExpandAdapter(context);
		commitExpandList.setAdapter(commitAdapter);

		detailLayout = LayoutInflater.from(context).inflate(
				R.layout.layout_commit_detail, null);
		commitExpandList.addHeaderView(detailLayout);
		commitExpandList.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				final CommitFile commitfile = commitAdapter.getCommitTree()
						.getCommitFile(groupPosition);
				new CommitLineDialog(context, commitfile.getFilename(),
						(CommitLine) commitAdapter.getCommitTree().getChild(
								groupPosition, childPosition)) {

					@Override
					protected void onItemClick(int position) {
						if (position == 1) {
							IntentUtils
									.create(context, CodeFileActivity.class)
									.putExtra(Constants.Extra.REPOSITORY,
											repository)
									.putExtra(Constants.Extra.COMMIT_FILE,
											commitfile).start();
						} else {
							IntentUtils
									.create(context,
											NewCommitCommentActivity.class)
									.putExtra(Constants.Extra.REPOSITORY,
											repository).start();
						}
					}

				}.show();
				return true;
			}
		});
		commitExpandList
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						if (position == 0)
							return false;
						int groupPosition = (Integer) view
								.getTag(R.id.tag_group); // 参数值是在setTag时使用的对应资源id号
						int childPosition = (Integer) view
								.getTag(R.id.tag_child);

						CommitFile commitfile = commitAdapter.getCommitTree()
								.getCommitFile(groupPosition);
						if (childPosition == -1) {
							IntentUtils
									.create(context, CodeFileActivity.class)
									.putExtra(Constants.Extra.REPOSITORY,
											repository)
									.putExtra(Constants.Extra.COMMIT_FILE,
											commitfile).start();
						} else {

						}
						return true;
					}
				});
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		repository = (Repository) getIntent().getSerializableExtra(
				Constants.Extra.REPOSITORY);
		commit = (RepositoryCommit) getIntent().getSerializableExtra(
				Constants.Extra.REPOSITORY_COMMIT);
		execute(GitHubApplication.getClient());
		updataDetail(commit);
	}

	private void updataDetail(RepositoryCommit commit) {
		ImageView icon = (ImageView) detailLayout
				.findViewById(R.id.detail_icon);

		ImageLoaderUtils.displayImage(CommitUtils.getAuthorAvatarUrl(commit),
				icon, R.drawable.default_avatar, R.drawable.default_avatar,
				true);

		TextView name = (TextView) findViewById(R.id.detail_name);
		name.setText(CommitUtils.getAuthor(commit));

		TextView date = (TextView) findViewById(R.id.date);
		date.setText(TimeUtils.getTime(CommitUtils.getAuthorDate(commit)
				.getTime()));

		TextView comment = (TextView) findViewById(R.id.comment);
		comment.setText(commit.getCommit().getCommentCount() + "");

		TextView content = (TextView) findViewById(R.id.content);
		content.setText(commit.getCommit().getMessage());

	}

	private void updateStats() {
		TextView comment = (TextView) findViewById(R.id.comment);
		comment.setText(commit.getCommit().getCommentCount() + "");

		CommitStats stats = commit.getStats();
		TextView detail = (TextView) findViewById(R.id.detail);
		detail.setText(String.format(
				"Total %d changed files, %d additions and %d deletions.",
				commit.getFiles().size(), stats.getAdditions(),
				stats.getDeletions()));
	}

	@Override
	public CommitTree onBackground(GitHubClient params) throws Exception {
		// TODO Auto-generated method stub
		try {
			CollaboratorService collaboratorService = new CollaboratorService(
					params);
			isCollaborator = collaboratorService.isCollaborator(repository,
					GitHubApplication.getUser().getLogin());
		} catch (Exception ex) {
			isCollaborator = false;
			ex.printStackTrace();
		}

		CommitService service = new CommitService(params);
		commit = service.getCommit(repository, commit.getSha());
		List<CommitComment> comments = service.getComments(repository,
				commit.getSha());
		Collections.sort(comments, new CommitCommentComparator());
		CommitTree commitTree = new CommitTree(commit, comments);
		return commitTree;
	}

	@Override
	public void onSuccess(CommitTree result) {
		// TODO Auto-generated method stub
		super.onSuccess(result);
		updateStats();
		commitAdapter.setIsCollaborator(isCollaborator).setCommitTree(result);
	}

	@Override
	protected void onleftLayout() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub

	}

}
