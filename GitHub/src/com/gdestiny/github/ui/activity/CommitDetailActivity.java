package com.gdestiny.github.ui.activity;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.CommitStats;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseLoadFragmentActivity;
import com.gdestiny.github.adapter.CommitExpandAdapter;
import com.gdestiny.github.adapter.CommitExpandAdapter.OnCommitCommentListener;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.async.DeleteCommitCommentTask;
import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.bean.CommitLine;
import com.gdestiny.github.bean.CommitTree;
import com.gdestiny.github.bean.comparator.CommitCommentComparator;
import com.gdestiny.github.ui.dialog.CommitLineDialog;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.CacheUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.TimeUtils;
import com.gdestiny.github.utils.ToastUtils;
import com.gdestiny.github.utils.ViewUtils;
import com.gdestiny.github.utils.client.CommitUtils;

public class CommitDetailActivity extends
		BaseLoadFragmentActivity<Void, CommitTree> {

	private Repository repository;
	private RepositoryCommit commit;

	private View detailLayout;
	private ExpandableListView commitExpandList;
	private CommitExpandAdapter commitAdapter;

	private boolean isCollaborator;

	private View stickComment;

	private int position;
	private boolean hasChange = false;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_commit_detail, R.id.pull_refresh_layout);
	}

	@Override
	protected void initView() {
		View.OnClickListener stickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				openRawComment();
			}
		};

		commitExpandList = (ExpandableListView) findViewById(R.id.list);

		commitAdapter = new CommitExpandAdapter(context);
		commitExpandList.setAdapter(commitAdapter);
		commitAdapter.setOnListener(new OnCommitCommentListener() {

			@Override
			public void onEdit(int groupPosition, int childPosition,
					CommitComment comment) {
				IntentUtils
						.create(context, EditCommitCommentActivity.class)
						.putExtra(Constants.Extra.GRIUP_POSITION, groupPosition)
						.putExtra(Constants.Extra.CHILD_POSITION, childPosition)
						.putExtra(Constants.Extra.COMMIT_COMMENT, comment)
						.putExtra(Constants.Extra.REPOSITORY, repository)
						.startForResult(context, Constants.Request.EDIT_COMMENT);
			}

			@Override
			public void onDelete(final int groupPosition,
					final int childPosition, final CommitComment comment) {
				new DeleteCommitCommentTask(context, repository, comment
						.getId()) {

					@Override
					public void onSuccess(Boolean result) {
						super.onSuccess(result);
						commit.getCommit().setCommentCount(
								commit.getCommit().getCommentCount() - 1);
						if (childPosition < 0) {
							commitAdapter.getCommitTree().getRawComment()
									.remove(comment);
						} else {
							commitAdapter.getCommitTree()
									.getChildList(groupPosition)
									.remove(comment);
							commitAdapter.getCommitTree()
									.commentCountDescrement(groupPosition);
						}
						commitAdapter.notifyDataSetChanged();
						hasChange = true;
						updateComment();
					}
				}.execute();
			}
		});

		detailLayout = LayoutInflater.from(context).inflate(
				R.layout.layout_commit_detail, null);

		stickComment = findViewById(R.id.layout_comment);
		stickComment
				.setBackgroundResource(R.drawable.selector_item_grey_to_blue);
		stickComment.setOnClickListener(stickListener);
		detailLayout.findViewById(R.id.btn_comment).setOnClickListener(
				stickListener);

		commitExpandList.addHeaderView(detailLayout);
		commitExpandList.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				final CommitFile commitfile = commitAdapter.getCommitTree()
						.getCommitFile(groupPosition);
				final CommitLine commitLine = (CommitLine) commitAdapter
						.getCommitTree().getChild(groupPosition, childPosition);
				new CommitLineDialog(context, commitfile.getFilename(),
						commitLine) {

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
									.putExtra(Constants.Extra.COMMIT_LINE,
											commitLine)
									.putExtra(Constants.Extra.SHA,
											commit.getSha())
									.putExtra(Constants.Extra.PATH,
											commitfile.getFilename())
									.putExtra(Constants.Extra.REPOSITORY,
											repository)
									.startForResult(context,
											Constants.Request.COMMIT_COMMENT);
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
						if (position == 0
								|| position >= commitAdapter.getCommitTree()
										.getGroupFileCount())
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
		commitExpandList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (firstVisibleItem == 0) {
					ViewUtils.setVisibility(stickComment, View.GONE);
				} else {
					ViewUtils.setVisibility(stickComment, View.VISIBLE);
				}
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
		position = getIntent().getIntExtra(Constants.Extra.POSITION, -1);

		setLoadCache(CacheUtils.contain(getCacheName()));

		getTitlebar().setLeftLayout(repository.getOwner().getAvatarUrl(),
				"Commit " + CommitUtils.getSubSha(commit),
				repository.generateId());

		execute();
		updataDetail(commit);
	}

	@Override
	protected void initActionBar(TitleBar titleBar) {
		super.initActionBar(titleBar);
		LinkedHashMap<Integer, Integer> itemmap = new LinkedHashMap<Integer, Integer>();
		itemmap.put(R.string.comment, R.drawable.common_comment);
		itemmap.put(R.string.share, R.drawable.common_share_grey);
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
									+ "/commit/" + commit.getSha());
					break;
				case R.string.comment:
					openRawComment();
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

		TextView sha = (TextView) findViewById(R.id.sha);
		sha.setText(CommitUtils.getSubSha(commit));

	}

	private void updateParent(RepositoryCommit commit) {
		List<Commit> parents = commit.getParents();
		if (parents == null)
			return;
		for (Commit c : parents) {
			System.out.println(c.getMessage() + "");
			System.out.println(c.getCommentCount() + "");
			System.out.println(c.getSha() + "");
		}
	}

	private void openRawComment() {
		IntentUtils.create(context, NewCommitCommentActivity.class)
				.putExtra(Constants.Extra.SHA, commit.getSha())
				.putExtra(Constants.Extra.REPOSITORY, repository)
				.startForResult(context, Constants.Request.RAW_COMMIT_COMMENT);
	}

	private void updateStats() {

		updateComment();
		CommitStats stats = commit.getStats();
		TextView detail = (TextView) findViewById(R.id.detail);
		detail.setText(String.format(
				"Total %d changed files, %d additions and %d deletions.",
				commit.getFiles().size(), stats.getAdditions(),
				stats.getDeletions()));
	}

	private void updateComment() {
		TextView comment = (TextView) findViewById(R.id.comment);
		comment.setText(commit.getCommit().getCommentCount() + "");
	}

	@Override
	public String getCacheName() {
		return CacheUtils.DIR.COMMIT + commit.getSha();
	}

	@Override
	public CommitTree onBackground(Void params) throws Exception {
		isCollaborator = GitHubConsole.getInstance().isCollaborator(repository,
				GitHubApplication.getUser().getLogin());

		if (isLoadCache()) {
			commit = CacheUtils.getCacheObject(getCacheName(),
					RepositoryCommit.class);
		} else {
			commit = GitHubConsole.getInstance().getCommit(repository,
					commit.getSha());
			CacheUtils.cacheObject(getCacheName(), commit);
		}
		List<CommitComment> comments = GitHubConsole.getInstance()
				.getCommitComment(repository, commit.getSha());

		// for (CommitComment c : comments) {
		// System.out.println("----------------------------------------");
		// System.out.println("getBody:" + c.getBody());
		// System.out.println("getLine:" + c.getLine());
		// System.out.println("getPath:" + c.getPath());
		// System.out.println("getPosition:" + c.getPosition());
		// }

		Collections.sort(comments, new CommitCommentComparator());
		CommitTree commitTree = new CommitTree(commit, comments);
		return commitTree;
	}

	@Override
	public void onSuccess(CommitTree result) {
		// TODO Auto-generated method stub
		super.onSuccess(result);
		updateStats();
		updateParent(commit);
		commitAdapter.setIsCollaborator(isCollaborator).setCommitTree(result);
	}

	@Override
	public void onResultOk(int requestCode, Intent data) {
		super.onResultOk(requestCode, data);
		if (requestCode == Constants.Request.COMMIT_COMMENT) {
			CommitComment cc = (CommitComment) data
					.getSerializableExtra(Constants.Extra.COMMIT_COMMENT);
			List<Serializable> list = commitAdapter.getCommitTree()
					.getChildMap().get(cc.getPath());
			int position = cc.getPosition() - 1;
			int insert = -1;
			for (int i = 0; i < list.size(); i++) {
				Serializable obj = list.get(i);
				if (obj instanceof CommitLine) {
					if (insert > 0) {
						break;
					}
					CommitLine line = (CommitLine) obj;
					if (line.getPosition() == position) {
						insert = i;
					}
				} else {
					if (insert > 0) {
						insert++;
						continue;
					}
				}
			}
			list.add(insert + 1, cc);

			commitAdapter.getCommitTree().commentCountIncrement(cc.getPath());
			commitAdapter.notifyDataSetChanged();

			commit.getCommit().setCommentCount(
					commit.getCommit().getCommentCount() + 1);
			hasChange = true;
			updateComment();
		} else if (requestCode == Constants.Request.EDIT_COMMENT) {
			int groupPosition = data.getIntExtra(
					Constants.Extra.GRIUP_POSITION, -1);
			int childPosition = data.getIntExtra(
					Constants.Extra.CHILD_POSITION, -1);
			CommitComment comment = (CommitComment) data
					.getSerializableExtra(Constants.Extra.COMMIT_COMMENT);

			if (childPosition < 0) {
				commitAdapter
						.getCommitTree()
						.getRawComment()
						.get(groupPosition
								- commitAdapter.getCommitTree()
										.getGroupFileCount())
						.setBodyHtml(comment.getBodyHtml());
			} else {
				((CommitComment) commitAdapter.getCommitTree().getChild(
						groupPosition, childPosition)).setBodyHtml(comment
						.getBodyHtml());
			}
			commitAdapter.notifyDataSetChanged();
		} else if (requestCode == Constants.Request.RAW_COMMIT_COMMENT) {
			CommitComment comment = (CommitComment) data
					.getSerializableExtra(Constants.Extra.COMMIT_COMMENT);
			commitAdapter.getCommitTree().getRawComment().add(comment);

			commit.getCommit().setCommentCount(
					commit.getCommit().getCommentCount() + 1);
			hasChange = true;
			updateComment();
			commitAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onleftLayout() {
		finish();
	}

	@Override
	public void finish() {
		if (hasChange) {
			IntentUtils
					.create(context)
					.putExtra(Constants.Extra.POSITION, position)
					.putExtra(Constants.Extra.COMMENT_COUNT,
							commit.getCommit().getCommentCount()).setResultOk();
		}
		super.finish();
	}

	@Override
	public void onRefresh() {
		execute();
	}

}
