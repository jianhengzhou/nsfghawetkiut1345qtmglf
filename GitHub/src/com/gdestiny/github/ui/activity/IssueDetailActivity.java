package com.gdestiny.github.ui.activity;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Milestone;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.CommentAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.async.AsyncImageGetter;
import com.gdestiny.github.async.DeleteTask;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.view.LabelViewGroup;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.TimeUtils;
import com.gdestiny.github.utils.ViewUtils;

public class IssueDetailActivity extends
		BaseLoadFragmentActivity<GitHubClient, List<Comment>> {

	public static final String EXTRA_ISSUE = "issue";
	public static final String EXTRA_IREPOSITORY = "irepository";

	private Issue issue;
	private Repository repository;

	private View detailView;

	private ListView commentList;
	private CommentAdapter commentAdapter;
	private List<Comment> comments;

	private TextView content;
	private boolean fold = true;

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
	protected void initActionBar(TitleBar titleBar) {
		super.initActionBar(titleBar);
		LinkedHashMap<Integer, Integer> itemmap = new LinkedHashMap<Integer, Integer>();
		itemmap.put(R.string.refresh, R.drawable.common_status_refresh);

		StatusPopUpWindow.StatusPopUpWindowItemClickListener menuListener = new StatusPopUpWindow.StatusPopUpWindowItemClickListener() {

			@Override
			public void onitemclick(int titleId) {
				GLog.sysout(context.getResources().getString(titleId) + "");
				switch (titleId) {
				case R.string.refresh:
					if (!isLoading())
						onRefreshStarted(null);
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
		commentAdapter.setOnListener(new CommentAdapter.OnListener() {

			@Override
			public void onEdit(Comment comment) {
				// TODO Auto-generated method stub
				GLog.sysout("onEdit");
			}

			@Override
			public void onDelete(final Comment comment) {
				// TODO Auto-generated method stub
				GLog.sysout("onDelete");
				new DeleteTask(context, repository, comment) {

					@Override
					public void onSuccess(Boolean result) {
						super.onSuccess(result);
						comments.remove(comment);
						commentAdapter.notifyDataSetChanged();
					}
				}.execute(GitHubApplication.getClient());
			}
		});
		commentList.setAdapter(commentAdapter);

		execute(GitHubApplication.getClient());

		ImageView icon = (ImageView) detailView.findViewById(R.id.detail_icon);
		ImageLoaderUtils.displayImage(issue.getUser().getAvatarUrl(), icon,
				R.drawable.default_avatar, R.drawable.default_avatar, true);

		TextView title = (TextView) detailView.findViewById(R.id.title);
		title.setText(issue.getTitle());

		TextView name = (TextView) detailView.findViewById(R.id.detail_name);
		name.setText(issue.getUser().getLogin());

		TextView date = (TextView) detailView.findViewById(R.id.date);
		date.setText(TimeUtils.getTime(issue.getCreatedAt().getTime()));

		TextView comment = (TextView) detailView.findViewById(R.id.comment);
		comment.setText(issue.getComments() + "");

		content = (TextView) detailView.findViewById(R.id.content);
		content.setText(Html.fromHtml(issue.getBodyHtml(),
				new AsyncImageGetter(context, content), null));
		ViewUtils.handleLink(content);

		final ImageView foldBtn = (ImageView) findViewById(R.id.more);
		foldBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
	public List<Comment> onBackground(GitHubClient params) throws Exception {
		// TODO Auto-generated method stub
		IssueService service = new IssueService(params);
		return service.getComments(repository, issue.getNumber());
	}

	@Override
	public void onSuccess(List<Comment> result) {
		// TODO Auto-generated method stub
		super.onSuccess(result);
		comments = result;
		commentAdapter.setDatas(comments);
	}

	@Override
	protected void onleftLayout() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void onRefreshStarted(View view) {
		execute(GitHubApplication.getClient());
	}

}
