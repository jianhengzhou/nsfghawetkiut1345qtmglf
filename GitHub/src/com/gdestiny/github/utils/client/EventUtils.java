package com.gdestiny.github.utils.client;

import java.util.List;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.Download;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.Team;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.event.CommitCommentPayload;
import org.eclipse.egit.github.core.event.CreatePayload;
import org.eclipse.egit.github.core.event.DeletePayload;
import org.eclipse.egit.github.core.event.DownloadPayload;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.event.EventPayload;
import org.eclipse.egit.github.core.event.EventRepository;
import org.eclipse.egit.github.core.event.ForkPayload;
import org.eclipse.egit.github.core.event.GistPayload;
import org.eclipse.egit.github.core.event.IssueCommentPayload;
import org.eclipse.egit.github.core.event.IssuesPayload;
import org.eclipse.egit.github.core.event.MemberPayload;
import org.eclipse.egit.github.core.event.PullRequestPayload;
import org.eclipse.egit.github.core.event.PullRequestReviewCommentPayload;
import org.eclipse.egit.github.core.event.PushPayload;
import org.eclipse.egit.github.core.event.TeamAddPayload;

import com.gdestiny.github.utils.GLog;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

public class EventUtils {

	private EventUtils() {
		throw new AssertionError();
	}

	public static Repository getRepository(Event event) {
		if (event == null)
			return null;

		if (Event.TYPE_FORK.equals(event.getType())) {
			EventPayload payload = event.getPayload();
			if (payload != null) {
				Repository repository = ((ForkPayload) payload).getForkee();
				// Verify repository has valid name and owner
				if (repository != null
						&& !TextUtils.isEmpty(repository.getName())
						&& repository.getOwner() != null
						&& !TextUtils.isEmpty(repository.getOwner().getLogin()))
					return repository;
			}
		}

		EventRepository er = event.getRepo();

		if (er == null)
			return null;

		String id = er.getName();
		int slash = id.indexOf('/');
		if (slash == -1 || slash + 1 >= id.length())
			return null;

		Repository result = new Repository();
		result.setMasterBranch("master");
		result.setId(er.getId());
		result.setName(id.substring(slash + 1));
		String login = id.substring(0, slash);
		// Use actor if it matches login parsed from repository id
		User actor = event.getActor();
		User org = event.getOrg();
		if (actor != null && login.equals(actor.getLogin())) {
			result.setOwner(actor);
			GLog.sysout("setOwner(actor)");
		} else if (org != null && login.equals(org.getLogin())) {
			result.setOwner(org);
			GLog.sysout("setOwner(org)");
		} else {
			result.setOwner(new User().setLogin(id.substring(0, slash)));
			GLog.sysout("setOwner(new User())");
		}
		return result;
	}

	public static User getEventUser(Event event) {
		User author = event.getActor();
		if (author != null)
			return author;

		User org = event.getOrg();

		return org != null ? org : null;
	}

	public static String getAuthor(Event event) {
		User user = getEventUser(event);
		return user != null ? user.getLogin() : null;
	}

	public static String getAuthorAvatarUrl(Event event) {
		User user = getEventUser(event);
		return user != null ? user.getAvatarUrl() : null;
	}

	// public static final String TYPE_COMMIT_COMMENT = "CommitCommentEvent";
	// public static final String TYPE_CREATE = "CreateEvent";
	// public static final String TYPE_DELETE = "DeleteEvent";
	// public static final String TYPE_DOWNLOAD = "DownloadEvent";
	// public static final String TYPE_FOLLOW = "FollowEvent";
	// public static final String TYPE_FORK = "ForkEvent";
	// public static final String TYPE_FORK_APPLY = "ForkApplyEvent";
	// public static final String TYPE_GIST = "GistEvent";
	// public static final String TYPE_GOLLUM = "GollumEvent";
	// public static final String TYPE_ISSUE_COMMENT = "IssueCommentEvent";
	// public static final String TYPE_ISSUES = "IssuesEvent";
	// public static final String TYPE_MEMBER = "MemberEvent";
	// public static final String TYPE_PUBLIC = "PublicEvent";
	// public static final String TYPE_PULL_REQUEST = "PullRequestEvent";
	// public static final String TYPE_PULL_REQUEST_REVIEW_COMMENT =
	// "PullRequestReviewCommentEvent";
	// public static final String TYPE_PUSH = "PushEvent";
	// public static final String TYPE_TEAM_ADD = "TeamAddEvent";
	// public static final String TYPE_WATCH = "WatchEvent";

	public static final int blueColor = 0xff56abe4;
	public static final int blackColor = 0xff2a2a2a;

	public static SpannableString toColorString(int color, String str) {
		SpannableString string = new SpannableString(str);
		string.setSpan(new ForegroundColorSpan(color), 0, str.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return string;
	}

	public static SpannableString toBlackString(String str) {
		return toColorString(blackColor, str);
	}

	public static SpannableString toBlueString(String str) {
		return toColorString(blueColor, str);
	}

	public static SpannableStringBuilder getEventTitle(Event event) {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		builder.append(toBlueString(getAuthor(event)));
		builder.append(' ');

		String type = event.getType();
		if (Event.TYPE_COMMIT_COMMENT.equals(type)) {
			builder.append(toBlackString("commented commit on"));
		} else if (Event.TYPE_WATCH.equals(type)) {
			builder.append(toBlackString("starred"));
		} else if (Event.TYPE_CREATE.equals(type)) {
			builder.append(toBlackString("created "));
			CreatePayload payload = (CreatePayload) event.getPayload();
			String refType = payload.getRefType();
			builder.append(refType);
			builder.append(' ');
			if (!"repository".equals(refType)) {
				builder.append(payload.getRef());
				builder.append(toBlackString(" at "));
			}
		} else if (Event.TYPE_DELETE.equals(type)) {
			DeletePayload payload = (DeletePayload) event.getPayload();
			builder.append("deleted ");
			builder.append(payload.getRefType());
			builder.append(' ');
			builder.append(payload.getRef());
			builder.append(" at ");
		} else if (Event.TYPE_DOWNLOAD.equals(type)) {
			builder.append(toBlackString("uploaded a file to"));
		} else if (Event.TYPE_FOLLOW.equals(type)) {
			builder.append(toBlackString("started following"));
		} else if (Event.TYPE_FORK.equals(type)) {
			builder.append(toBlackString("forked"));
		} else if (Event.TYPE_GOLLUM.equals(type)) {
			builder.append(toBlackString("edited the wiki in"));
		} else if (Event.TYPE_ISSUE_COMMENT.equals(type)) {
			builder.append(toBlackString("commented on "));
			IssueCommentPayload payload = (IssueCommentPayload) event
					.getPayload();
			Issue issue = payload.getIssue();
			if (issue != null && issue.getPullRequest() != null
					&& !TextUtils.isEmpty(issue.getPullRequest().getHtmlUrl())) {
				builder.append(toBlackString("pull request"));
			} else {
				builder.append(toBlackString("issue"));
			}
			builder.append(toBlueString("#" + issue.getNumber()));
			builder.append(toBlackString(" at "));
		} else if (Event.TYPE_ISSUES.equals(type)) {
			IssuesPayload payload = (IssuesPayload) event.getPayload();
			String action = payload.getAction();
			Issue issue = payload.getIssue();
			builder.append(toBlackString(action + " " + "issue"));
			builder.append(toBlueString("#" + issue.getNumber()));
			builder.append(toBlackString(" on"));
		} else if (Event.TYPE_MEMBER.equals(type)) {
			User member = ((MemberPayload) event.getPayload()).getMember();
			String mem = "";
			if (member != null)
				mem = member.getLogin();
			mem = "as a collaborator to ";
			builder.append(toBlackString("added " + mem));
		} else if (Event.TYPE_PUBLIC.equals(type)) {
			builder.append(toBlackString("open sourced"));
		} else if (Event.TYPE_PULL_REQUEST.equals(type)) {
			PullRequestPayload payload = (PullRequestPayload) event
					.getPayload();
			String action = payload.getAction();
			if ("synchronize".equals(action))
				action = "updated";
			builder.append(' ');
			builder.append(action);
			builder.append(' ');
			builder.append("pull request ");
			builder.append(toBlueString(String.valueOf(payload.getNumber())));
			builder.append(" on ");
		} else if (Event.TYPE_PULL_REQUEST_REVIEW_COMMENT.equals(type)) {
			builder.append(toBlackString("commented on"));
		} else if (Event.TYPE_PUSH.equals(type)) {
			builder.append(toBlackString("pushed to "));
			PushPayload payload = (PushPayload) event.getPayload();
			String ref = payload.getRef();
			if (ref.startsWith("refs/heads/"))
				ref = ref.substring(11);
			builder.append(toBlueString(ref));
			builder.append(toBlackString(" at "));
		} else if (Event.TYPE_TEAM_ADD.equals(type)) {
			TeamAddPayload payload = (TeamAddPayload) event.getPayload();

			builder.append("added ");

			User user = payload.getUser();
			if (user != null)
				builder.append(toBlueString(user.getLogin()));

			builder.append(" to team");

			Team team = payload.getTeam();
			String teamName = team != null ? team.getName() : null;
			if (teamName != null)
				builder.append(' ').append(toBlueString(teamName));
		} else if (Event.TYPE_GIST.equals(type)) {
			GistPayload payload = (GistPayload) event.getPayload();
			builder.append(' ');
			String action = payload.getAction();
			if ("create".equals(action))
				builder.append("created");
			else if ("update".equals(action))
				builder.append("updated");
			else
				builder.append(action);
			builder.append(" Gist ");
			builder.append(toBlueString(payload.getGist().getId()));
		}

		// builder.append(typeSpanableString(event));
		builder.append(' ');
		builder.append(toColorString(blueColor, event.getRepo().getName()));
		return builder;
	}

	public static SpannableStringBuilder getEventDetail(Event event) {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		String type = event.getType();
		if (Event.TYPE_COMMIT_COMMENT.equals(type)) {
			CommitCommentPayload payload = (CommitCommentPayload) event
					.getPayload();
			CommitComment comment = payload.getComment();
			if (comment != null) {

				String id = comment.getCommitId();
				if (!TextUtils.isEmpty(id)) {
					if (id.length() > 10)
						id = id.substring(0, 10);
					builder.append(toBlackString("Comment in "));
					builder.append(toBlueString(id));
					builder.append(':').append('\n');
					builder.append(comment.getBody());
				}
			}
		} else if (Event.TYPE_CREATE.equals(type)) {
		} else if (Event.TYPE_DELETE.equals(type)) {
		} else if (Event.TYPE_DOWNLOAD.equals(type)) {
			DownloadPayload payload = (DownloadPayload) event.getPayload();
			Download download = payload.getDownload();
			if (download != null)
				builder.append(download.getName());
		} else if (Event.TYPE_FOLLOW.equals(type)) {
		} else if (Event.TYPE_FORK_APPLY.equals(type)) {
		} else if (Event.TYPE_GIST.equals(type)) {
		} else if (Event.TYPE_GOLLUM.equals(type)) {
		} else if (Event.TYPE_ISSUE_COMMENT.equals(type)) {
			IssueCommentPayload payload = (IssueCommentPayload) event
					.getPayload();
			builder.append(payload.getComment().getBody());
		} else if (Event.TYPE_ISSUES.equals(type)) {
		} else if (Event.TYPE_MEMBER.equals(type)) {
		} else if (Event.TYPE_PUBLIC.equals(type)) {
		} else if (Event.TYPE_PULL_REQUEST.equals(type)) {
		} else if (Event.TYPE_PULL_REQUEST_REVIEW_COMMENT.equals(type)) {
			PullRequestReviewCommentPayload payload = (PullRequestReviewCommentPayload) event
					.getPayload();
			builder.append(payload.getComment().getBody());
		} else if (Event.TYPE_PUSH.equals(type)) {
			PushPayload payload = (PushPayload) event.getPayload();
			final List<Commit> commits = payload.getCommits();
			int size = commits != null ? commits.size() : -1;
			if (size > 0) {
				if (size != 1)
					builder.append(toBlackString(size + " new commits"));
				else
					builder.append(toBlackString("1 new commit"));

				int max = 3;
				int appended = 0;
				for (Commit commit : commits) {
					if (commit == null)
						continue;

					String sha = commit.getSha();
					if (TextUtils.isEmpty(sha))
						continue;

					builder.append('\n');
					if (sha.length() > 7)
						builder.append(toBlueString(sha.substring(0, 7)));
					else
						builder.append(toBlueString(sha));

					String message = commit.getMessage();
					if (!TextUtils.isEmpty(message)) {
						builder.append(' ');
						int newline = message.indexOf('\n');
						if (newline > 0)
							builder.append(message.subSequence(0, newline));
						else
							builder.append(message);
					}

					appended++;
					if (appended == max)
						break;
				}
			}
		} else if (Event.TYPE_TEAM_ADD.equals(type)) {
		} else if (Event.TYPE_WATCH.equals(type)) {
		}
		return builder;
	}

}
