package com.gdestiny.github.ui.activity;

import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Milestone;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.IssueService;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseLoadFragmentActivity;
import com.gdestiny.github.adapter.CommentAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.async.AsyncImageGetter;
import com.gdestiny.github.async.DeleteCommentTask;
import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.view.LabelViewGroup;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.TimeUtils;
import com.gdestiny.github.utils.ToastUtils;
import com.gdestiny.github.utils.ViewUtils;

public class IssueDetailActivity extends
		BaseLoadFragmentActivity<Void, List<Comment>> {

	private Issue issue;
	private Repository repository;

	private View detailView;

	private ListView commentList;
	private CommentAdapter commentAdapter;
	private List<Comment> comments;

	private TextView content;
	private boolean fold = true;
	private boolean isCollaborator;

	private int position;
	private boolean hasChange = false;

	private View stickComment;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.act_issue_detail, R.id.pull_refresh_layout);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		commentList = (ListView) findViewById(R.id.list);

		detailView = LayoutInflater.from(context).inflate(
				R.layout.layout_issue_detail, null);
		content = (TextView) detailView.findViewById(R.id.content);

		stickComment = findViewById(R.id.layout_comment);
		stickComment
				.setBackgroundResource(R.drawable.selector_item_grey_to_blue);
		View.OnClickListener stickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				openComment();
			}
		};
		stickComment.setOnClickListener(stickListener);
		detailView.findViewById(R.id.btn_comment).setOnClickListener(
				stickListener);

		commentList.setOnScrollListener(new OnScrollListener() {

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
	protected void initActionBar(TitleBar titleBar) {
		super.initActionBar(titleBar);
		LinkedHashMap<Integer, Integer> itemmap = new LinkedHashMap<Integer, Integer>();
		itemmap.put(R.string.comment, R.drawable.common_comment);
		itemmap.put(R.string.refresh, R.drawable.common_status_refresh);

		StatusPopUpWindow.StatusPopUpWindowItemClickListener menuListener = new StatusPopUpWindow.StatusPopUpWindowItemClickListener() {

			@Override
			public void onitemclick(int titleId) {
				GLog.sysout(context.getResources().getString(titleId) + "");
				switch (titleId) {
				case R.string.refresh:
					if (!isLoading())
						onRefresh();
					break;
				case R.string.edit_issue:
					IntentUtils
							.create(context, NewEditIssueActivity.class)
							.putExtra(Constants.Extra.ISSUE, issue)
							.putExtra(Constants.Extra.REPOSITORY, repository)
							.startForResult(IssueDetailActivity.this,
									Constants.Request.EDIT_ISSUE);
					break;
				case R.string.comment:
					openComment();
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
	protected void initData() {
		// TODO Auto-generated method stub
		issue = (Issue) getIntent().getSerializableExtra(Constants.Extra.ISSUE);
		repository = (Repository) getIntent().getSerializableExtra(
				Constants.Extra.REPOSITORY);
		position = getIntent().getIntExtra(Constants.Extra.POSITION, -1);

		getTitlebar().setLeftLayout(repository.getOwner().getAvatarUrl(),
				"issue #" + issue.getNumber(), repository.generateId());

		final ImageView foldBtn = (ImageView) detailView
				.findViewById(R.id.more);
		foldBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!fold) {
					fold = true;
					content.setMaxLines(4);
					commentList.setSelection(0);
					foldBtn.setImageResource(R.drawable.common_up_more);
				} else {
					fold = false;
					content.setMaxLines(Integer.MAX_VALUE);
					foldBtn.setImageResource(R.drawable.common_down_more);
				}
			}
		});

		commentList.addHeaderView(detailView);
		commentAdapter = new CommentAdapter(context);
		commentAdapter.setOnListener(new CommentAdapter.OnListener() {

			@Override
			public void onEdit(int position, Comment comment) {
				// TODO Auto-generated method stub
				GLog.sysout("onEdit");
				IntentUtils
						.create(context, EditCommentActivity.class)
						.putExtra(Constants.Extra.COMMENT, comment)
						.putExtra(Constants.Extra.POSITION, position)
						.putExtra(Constants.Extra.REPOSITORY, repository)
						.startForResult(IssueDetailActivity.this,
								Constants.Request.EDIT_COMMENT);
			}

			@Override
			public void onDelete(final Comment comment) {
				// TODO Auto-generated method stub
				GLog.sysout("onDelete");
				new DeleteCommentTask(context, repository, comment) {

					@Override
					public void onSuccess(Boolean result) {
						super.onSuccess(result);
						TextView commentText = (TextView) detailView
								.findViewById(R.id.comment);
						issue.setComments(issue.getComments() - 1);
						commentText.setText(issue.getComments() + "");

						hasChange = true;

						comments.remove(comment);
						commentAdapter.notifyDataSetChanged();
					}
				}.execute();
			}
		});
		commentList.setAdapter(commentAdapter);

		execute();

		initDetail(issue);
	}

	private void openComment() {
		IntentUtils.create(context, NewCommentActivity.class)
				.putExtra(Constants.Extra.ISSUE, issue)
				.putExtra(Constants.Extra.REPOSITORY, repository)
				.startForResult(this, Constants.Request.ISSUE_COMMENT);
	}

	private void initDetail(Issue issue) {
		ImageView icon = (ImageView) detailView.findViewById(R.id.detail_icon);
		ImageLoaderUtils.displayImage(issue.getUser().getAvatarUrl(), icon,
				R.drawable.default_avatar, R.drawable.default_avatar, true);

		TextView name = (TextView) detailView.findViewById(R.id.detail_name);
		name.setText(issue.getUser().getLogin());

		TextView date = (TextView) detailView.findViewById(R.id.date);
		date.setText(TimeUtils.getTime(issue.getCreatedAt().getTime()));

		TextView comment = (TextView) detailView.findViewById(R.id.comment);
		comment.setText(issue.getComments() + "");

		refreshPartDetail(issue);
	}

	private void refreshPartDetail(Issue issue) {
		TextView title = (TextView) detailView.findViewById(R.id.title);
		title.setText(issue.getTitle());

		content.setText(Html.fromHtml(issue.getBodyHtml(),
				new AsyncImageGetter(context, content), null));
		ViewUtils.handleLink(content);

		LabelViewGroup labelGroup = (LabelViewGroup) findViewById(R.id.label_viewgroup);
		View labelLayout = findViewById(R.id.label_layout);

		if (issue.getLabels() != null && !issue.getLabels().isEmpty()) {
			ViewUtils.setVisibility(labelLayout, View.VISIBLE);
			labelGroup.setLabel(issue.getLabels());
		} else {
			ViewUtils.setVisibility(labelLayout, View.GONE);
		}
		if (issue.getAssignee() != null) {
			View assignLayout = findViewById(R.id.assign_layout);
			ViewUtils.setVisibility(assignLayout, View.VISIBLE);
			TextView assignName = (TextView) findViewById(R.id.name);
			assignName.setText(issue.getAssignee().getLogin());
			ImageView assignIcon = (ImageView) findViewById(R.id.icon);

			ImageLoaderUtils.displayImage(issue.getUser().getAvatarUrl(),
					assignIcon, R.drawable.default_avatar,
					R.drawable.default_avatar, true);
		}
		if (issue.getMilestone() != null) {
			View mileStoneLayout = findViewById(R.id.milestone_layout);
			ViewUtils.setVisibility(mileStoneLayout, View.VISIBLE);

			Milestone selected = issue.getMilestone();
			TextView milestoneTitle = (TextView) findViewById(R.id.milestone_title);
			TextView createAt = (TextView) findViewById(R.id.milestone_date);
			TextView creator = (TextView) findViewById(R.id.milestone_name);
			ImageView createIcon = (ImageView) findViewById(R.id.milestone_icon);
			TextView open = (TextView) findViewById(R.id.open);
			TextView close = (TextView) findViewById(R.id.close);
			// set data
			milestoneTitle.setText(selected.getTitle());
			if (selected.getState().equals(IssueService.STATE_CLOSED)) {
				milestoneTitle.getPaint().setFlags(
						android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
								| android.graphics.Paint.ANTI_ALIAS_FLAG);
			} else {
				milestoneTitle.getPaint().setFlags(
						android.graphics.Paint.ANTI_ALIAS_FLAG);
			}

			open.setText(selected.getOpenIssues() + "");
			close.setText("  " + selected.getClosedIssues() + "  ");
			close.getPaint().setFlags(
					android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
							| android.graphics.Paint.ANTI_ALIAS_FLAG);

			creator.setText(selected.getCreator().getLogin());
			createAt.setText(TimeUtils.getTime(selected.getCreatedAt()
					.getTime()));

			ImageLoaderUtils.displayImage(selected.getCreator().getAvatarUrl(),
					createIcon, R.drawable.default_avatar,
					R.drawable.default_avatar, true);
		}
	}

	@Override
	public List<Comment> onBackground(Void params) throws Exception {
		isCollaborator = GitHubConsole.getInstance().isCollaborator(repository,
				GitHubApplication.getUser().getLogin());

		return GitHubConsole.getInstance().getIssueComments(repository,
				issue.getNumber());
	}

	@Override
	public void onSuccess(List<Comment> result) {
		// TODO Auto-generated method stub
		super.onSuccess(result);
		comments = result;
		commentAdapter.setIsCollaborator(isCollaborator);
		commentAdapter.setDatas(comments);

		if (isCollaborator) {
			getTitlebar().getStatusPopup().addItem(context, 0,
					R.string.edit_issue, R.drawable.common_edit);
		}
	}

	@Override
	public void onResultOk(int requestCode, Intent data) {
		super.onResultOk(requestCode, data);
		if (requestCode == Constants.Request.EDIT_ISSUE) {
			GLog.sysout("EDIT_ISSUE");
			issue = (Issue) data.getSerializableExtra(Constants.Extra.ISSUE);
			hasChange = true;
			refreshPartDetail(issue);
		} else if (requestCode == Constants.Request.ISSUE_COMMENT) {
			issue.setComments(issue.getComments() + 1);
			TextView commentText = (TextView) detailView
					.findViewById(R.id.comment);
			commentText.setText(issue.getComments() + "");
			hasChange = true;

			Comment comment = (Comment) data
					.getSerializableExtra(Constants.Extra.COMMENT);
			commentAdapter.getDatas().add(comment);
			commentAdapter.notifyDataSetChanged();
			commentList.setSelection(commentAdapter.getCount());
		} else if (requestCode == Constants.Request.EDIT_COMMENT) {
			Comment comment = (Comment) data
					.getSerializableExtra(Constants.Extra.COMMENT);
			int position = data.getIntExtra(Constants.Extra.POSITION, -1);
			if (comment != null && position != -1) {
				commentAdapter.getDatas().get(position)
						.setBodyHtml(comment.getBodyHtml());
				commentAdapter.notifyDataSetChanged();
				// commentAdapter.getDatas().remove(position);
				// commentAdapter.getDatas().add(position, comment);
				// commentAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void finish() {
		if (hasChange) {
			IntentUtils.create(context).putExtra(Constants.Extra.ISSUE, issue)
					.putExtra(Constants.Extra.POSITION, position).setResultOk();
		}
		super.finish();
	}

	@Override
	public void onBackPressed() {
		finish();
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
