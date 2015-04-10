package com.gdestiny.github.ui.fragment;

import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.CATEGORY_BROWSABLE;

import java.util.List;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.Download;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.event.CommitCommentPayload;
import org.eclipse.egit.github.core.event.DownloadPayload;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.event.EventPayload;
import org.eclipse.egit.github.core.event.GistPayload;
import org.eclipse.egit.github.core.event.IssueCommentPayload;
import org.eclipse.egit.github.core.event.IssuesPayload;
import org.eclipse.egit.github.core.event.PullRequestPayload;
import org.eclipse.egit.github.core.event.PushPayload;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.fragment.BaseLoadPageFragment;
import com.gdestiny.github.adapter.EventAdapter;
import com.gdestiny.github.async.CommitListTask;
import com.gdestiny.github.async.refresh.RefreshCommitTask;
import com.gdestiny.github.async.refresh.RefreshIssueTask;
import com.gdestiny.github.ui.activity.CommitDetailActivity;
import com.gdestiny.github.ui.activity.GistDetailActivity;
import com.gdestiny.github.ui.activity.IssueDetailActivity;
import com.gdestiny.github.ui.activity.RepositoryDetailActivity;
import com.gdestiny.github.ui.activity.UserNavigationActivity;
import com.gdestiny.github.ui.dialog.MaterialDialog;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.client.EventUtils;
import com.gdestiny.github.utils.client.IssueUtils;

public abstract class AbstractEventFragment extends
		BaseLoadPageFragment<Event, Void> {

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_events,
				R.id.pull_refresh_layout, R.id.list);
	}

	@Override
	public void newListAdapter() {
		EventAdapter eventAdapter = new EventAdapter(context);
		eventAdapter.setDatas(getDatas());
		setBaseAdapter(eventAdapter);
	}

	@Override
	public void onRefresh() {
		super.onRefresh();
		execute();
	}

	@Override
	protected void initData() {
		execute();
	}

	@Override
	protected void initView() {
	}

	@Override
	public void newPageData(Void params) {

	}

	@Override
	public void initStatusPopup(TitleBar title) {

	}

	private void openDownload(Event event) {
		Download download = ((DownloadPayload) event.getPayload())
				.getDownload();
		if (download == null)
			return;

		String url = download.getHtmlUrl();
		if (TextUtils.isEmpty(url))
			return;

		Intent intent = new Intent(ACTION_VIEW, Uri.parse(url));
		intent.addCategory(CATEGORY_BROWSABLE);
		startActivity(intent);
	}

	private void openCommit(final Repository repository, String sha) {
		new RefreshCommitTask(context, repository, sha) {

			@Override
			public void onSuccess(RepositoryCommit result) {
				IntentUtils.create(context, CommitDetailActivity.class)
						.putExtra(Constants.Extra.REPOSITORY_COMMIT, result)
						.putExtra(Constants.Extra.REPOSITORY, repository)
						.start();
			}
		}.execute();
	}

	private void openPush(Event event) {
		final Repository repository = EventUtils.getRepository(event);
		if (repository == null)
			return;
		PushPayload payload = (PushPayload) event.getPayload();

		List<Commit> commits = payload.getCommits();
		if (commits == null || commits.isEmpty())
			return;
		if (commits.size() > 1) {
			String base = payload.getBefore();
			String head = payload.getHead();
			new CommitListTask(context, repository, base, head) {

				@Override
				public void onRepositoryCommit(RepositoryCommit commit) {
					IntentUtils
							.create(context, CommitDetailActivity.class)
							.putExtra(Constants.Extra.REPOSITORY_COMMIT, commit)
							.putExtra(Constants.Extra.REPOSITORY, repository)
							.start();
				}
			}.execute();
		} else {
			Commit commit = commits.get(0);
			String sha = commit != null ? commit.getSha() : null;
			openCommit(repository, sha);
		}
	}

	private void openCommitComment(Event event) {
		final Repository repository = EventUtils.getRepository(event);
		if (repository == null)
			return;

		CommitCommentPayload payload = (CommitCommentPayload) event
				.getPayload();
		CommitComment comment = payload.getComment();
		if (comment == null)
			return;

		String sha = comment.getCommitId();
		if (!TextUtils.isEmpty(sha))
			openCommit(repository, sha);
	}

	private void openIssue(Event event) {
		if (event == null)
			return;
		final Repository repository = EventUtils.getRepository(event);
		EventPayload payload = event.getPayload();
		if (payload == null || repository == null)
			return;
		Issue issue = null;
		String type = event.getType();
		if (Event.TYPE_ISSUES.equals(type))
			issue = ((IssuesPayload) payload).getIssue();
		else if (Event.TYPE_ISSUE_COMMENT.equals(type))
			issue = ((IssueCommentPayload) payload).getIssue();
		else if (Event.TYPE_PULL_REQUEST.equals(type))
			issue = IssueUtils.toIssue(((PullRequestPayload) payload)
					.getPullRequest());

		if (issue != null) {
			new RefreshIssueTask(context, repository, issue.getNumber()) {

				@Override
				public void onSuccess(Issue result) {
					IntentUtils.create(context, IssueDetailActivity.class)
							.putExtra(Constants.Extra.ISSUE, result)
							.putExtra(Constants.Extra.REPOSITORY, repository)
							.start();
				}
			}.execute();
		}

	}

	private void openGist(Event event) {
		if (event == null)
			return;
		EventPayload payload = event.getPayload();
		if (payload != null) {
			IntentUtils
					.create(context, GistDetailActivity.class)
					.putExtra(Constants.Extra.GIST,
							((GistPayload) payload).getGist()).start();
		}
	}

	private boolean openRepository(Event event) {
		Repository repository = EventUtils.getRepository(event);
		if (repository != null) {
			IntentUtils.create(context, RepositoryDetailActivity.class)
					.putExtra(Constants.Extra.REPOSITORY, repository).start();
			return true;
		}
		return false;
	}

	private boolean openUser(Event event) {
		User user = EventUtils.getEventUser(event);
		if (user != null) {
			IntentUtils.create(context, UserNavigationActivity.class)
					.putExtra(Constants.Extra.USER, user).start();
			return true;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final Event event = getDatas().get(position);
		String type = event.getType();
		if (Event.TYPE_DOWNLOAD.equals(type)) {
			openDownload(event);
		} else if (Event.TYPE_PUSH.equals(type)) {
			openPush(event);
		} else if (Event.TYPE_COMMIT_COMMENT.equals(type)) {
			openCommitComment(event);
		} else if (Event.TYPE_GIST.equals(type)) {
			openGist(event);
		} else if (Event.TYPE_ISSUES.equals(type)
				|| Event.TYPE_ISSUE_COMMENT.equals(type)
				|| Event.TYPE_PULL_REQUEST.equals(type)) {
			openIssue(event);
		} else if (!openRepository(event))
			openUser(event);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		final Event event = getDatas().get(position);
		MaterialDialog mMaterialDialog = new MaterialDialog(context);
		mMaterialDialog
				.setTitle("Go To")
				// .inProgress("loading")
				.addItem(EventUtils.getAuthorAvatarUrl(event),
						EventUtils.getAuthor(event))
				.addItem(R.drawable.common_repository_item,
						event.getRepo().getName(), false)
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						switch (position) {
						case 0:
							openUser(event);
							break;
						case 1:
							openRepository(event);
							break;
						}
					}
				}).setCanceledOnTouchOutside(true);
		mMaterialDialog.show();
		return true;
	}
}
