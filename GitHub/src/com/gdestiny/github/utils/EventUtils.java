package com.gdestiny.github.utils;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.event.DeletePayload;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.event.IssueCommentPayload;
import org.eclipse.egit.github.core.event.IssuesPayload;
import org.eclipse.egit.github.core.event.MemberPayload;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

public class EventUtils {

	private EventUtils() {
		throw new AssertionError();
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

	public static String getEventType(Event event) {
		String type = event.getType();
		if (Event.TYPE_COMMIT_COMMENT.equals(type)) {
			return "commented commit on";
		} else if (Event.TYPE_CREATE.equals(type)) {
			return "created repository";
		} else if (Event.TYPE_DELETE.equals(type)) {
			return "deleted repository";
		} else if (Event.TYPE_DOWNLOAD.equals(type)) {
			return "uploaded a file to";
		} else if (Event.TYPE_FOLLOW.equals(type)) {
			return "started following";
		} else if (Event.TYPE_FORK.equals(type)) {
			return "forked";
		} else if (Event.TYPE_FORK_APPLY.equals(type)) {
			return "";
		} else if (Event.TYPE_GIST.equals(type)) {
			return "";
		} else if (Event.TYPE_GOLLUM.equals(type)) {
			return "edited the wiki in";
		} else if (Event.TYPE_ISSUE_COMMENT.equals(type)) {
			IssueCommentPayload payload = (IssueCommentPayload) event
					.getPayload();
			Issue issue = payload.getIssue();
			String number;
			if (issue != null && issue.getPullRequest() != null
					&& !TextUtils.isEmpty(issue.getPullRequest().getHtmlUrl())) {
				number = "pull request #" + issue.getNumber();
			} else {
				number = "issue #" + issue.getNumber();
			}
			return "commented on " + number;
		} else if (Event.TYPE_ISSUES.equals(type)) {
			IssuesPayload payload = (IssuesPayload) event.getPayload();
			String action = payload.getAction();
			Issue issue = payload.getIssue();
			return action + " " + "issue #" + issue.getNumber() + " on";
		} else if (Event.TYPE_MEMBER.equals(type)) {
			User member = ((MemberPayload) event.getPayload()).getMember();
			String mem = "";
			if (member != null)
				mem = member.getLogin();
			mem = "as a collaborator to ";
			return "added " + mem;
		} else if (Event.TYPE_PUBLIC.equals(type)) {
			return "open sourced";
		} else if (Event.TYPE_PULL_REQUEST.equals(type)) {
			return "opened pull request";
		} else if (Event.TYPE_PULL_REQUEST_REVIEW_COMMENT.equals(type)) {
			return "commented on";
		} else if (Event.TYPE_PUSH.equals(type)) {
			return "";
		} else if (Event.TYPE_TEAM_ADD.equals(type)) {
			return "";
		} else if (Event.TYPE_WATCH.equals(type)) {
			return "starred";
		}
		return null;
	}

	public static String getEventPayload(Event event) {
		String type = event.getType();
		if (Event.TYPE_COMMIT_COMMENT.equals(type)) {
			return "";
		} else if (Event.TYPE_CREATE.equals(type)) {
			return "";
		} else if (Event.TYPE_DELETE.equals(type)) {
			return "";
		} else if (Event.TYPE_DOWNLOAD.equals(type)) {
			return "";
		} else if (Event.TYPE_FOLLOW.equals(type)) {
			return "";
		} else if (Event.TYPE_FORK.equals(type)) {
			return "";
		} else if (Event.TYPE_FORK_APPLY.equals(type)) {
			return "";
		} else if (Event.TYPE_GIST.equals(type)) {
			return "";
		} else if (Event.TYPE_GOLLUM.equals(type)) {
			return "";
		} else if (Event.TYPE_ISSUE_COMMENT.equals(type)) {
			IssueCommentPayload payload = (IssueCommentPayload) event
					.getPayload();
			return payload.getComment().getBody();
		} else if (Event.TYPE_ISSUES.equals(type)) {
			return "";
		} else if (Event.TYPE_MEMBER.equals(type)) {
			return "";
		} else if (Event.TYPE_PUBLIC.equals(type)) {
			return "";
		} else if (Event.TYPE_PULL_REQUEST.equals(type)) {
			return "";
		} else if (Event.TYPE_PULL_REQUEST_REVIEW_COMMENT.equals(type)) {
			return "";
		} else if (Event.TYPE_PUSH.equals(type)) {
			return "";
		} else if (Event.TYPE_TEAM_ADD.equals(type)) {
			return "";
		} else if (Event.TYPE_WATCH.equals(type)) {
			return "";
		}
		return null;
	}

	public static final int blueColor = 0xff56abe4;
	public static final int blackColor = 0xff2a2a2a;

	public static SpannableString toColorString(int color, String str) {
		SpannableString string = new SpannableString(str);
		string.setSpan(new ForegroundColorSpan(color), 0, str.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return string;
	}

	public static SpannableString authorSpanableString(Event event) {
		return toColorString(blueColor, getAuthor(event));
	}

	public static SpannableString repositorySpanableString(Event event) {
		return toColorString(blueColor, event.getRepo().getName());
	}

	public static SpannableString typeSpanableString(Event event) {
		return toColorString(blackColor,
				getEventType(event).equals("") ? event.getType()
						: getEventType(event));
	}

	public static SpannableString typeSpanableString(String type) {
		return toColorString(blackColor, type);
	}

	public static SpannableString toBlackString(String str) {
		return toColorString(blackColor, str);
	}

	public static SpannableString toBlueString(String str) {
		return toColorString(blueColor, str);
	}

	public static SpannableStringBuilder toSpannableString(Event event) {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		builder.append(authorSpanableString(event));
		builder.append(' ');

		String type = event.getType();
		if (Event.TYPE_COMMIT_COMMENT.equals(type)) {
			builder.append(toBlackString("commented commit on"));
		} else if (Event.TYPE_CREATE.equals(type)) {
			builder.append(toBlackString("created repository"));
		} else if (Event.TYPE_DELETE.equals(type)) {
			DeletePayload payload = (DeletePayload) event.getPayload();
			builder.append(toBlackString("deleted " + payload.getRefType()
					+ payload.getRef() + " at"));
		} else if (Event.TYPE_DOWNLOAD.equals(type)) {
			builder.append(toBlackString("uploaded a file to"));
		} else if (Event.TYPE_FOLLOW.equals(type)) {
			builder.append(toBlackString("started following"));
		} else if (Event.TYPE_FORK.equals(type)) {
			builder.append(toBlackString("forked"));
		} else if (Event.TYPE_FORK_APPLY.equals(type)) {
			builder.append(toBlackString("TYPE_FORK_APPLY"));
		} else if (Event.TYPE_GIST.equals(type)) {
			builder.append(toBlackString("TYPE_GIST"));
		} else if (Event.TYPE_GOLLUM.equals(type)) {
			builder.append(toBlackString("edited the wiki in"));
		} else if (Event.TYPE_ISSUE_COMMENT.equals(type)) {
			IssueCommentPayload payload = (IssueCommentPayload) event
					.getPayload();
			Issue issue = payload.getIssue();
			String number;
			if (issue != null && issue.getPullRequest() != null
					&& !TextUtils.isEmpty(issue.getPullRequest().getHtmlUrl())) {
				number = "pull request #" + issue.getNumber();
			} else {
				number = "issue #" + issue.getNumber();
			}
			builder.append(toBlackString("commented on " + number));
		} else if (Event.TYPE_ISSUES.equals(type)) {
			IssuesPayload payload = (IssuesPayload) event.getPayload();
			String action = payload.getAction();
			Issue issue = payload.getIssue();
			builder.append(toBlackString(action + " " + "issue #"
					+ issue.getNumber() + " on"));
		} else if (Event.TYPE_MEMBER.equals(type)) {
			User member = ((MemberPayload) event.getPayload()).getMember();
			String mem = "";
			if (member != null)
				mem = member.getLogin();
			mem = "as a collaborator to ";
			builder.append(toBlackString("added "));
			builder.append(toBlueString(mem));
			return builder;
		} else if (Event.TYPE_PUBLIC.equals(type)) {
			builder.append(toBlackString("open sourced"));
		} else if (Event.TYPE_PULL_REQUEST.equals(type)) {
			builder.append(toBlackString("opened pull request"));
		} else if (Event.TYPE_PULL_REQUEST_REVIEW_COMMENT.equals(type)) {
			builder.append(toBlackString("commented on"));
		} else if (Event.TYPE_PUSH.equals(type)) {
			builder.append(toBlackString("TYPE_PUSH"));
		} else if (Event.TYPE_TEAM_ADD.equals(type)) {
			builder.append(toBlackString("TYPE_TEAM_ADD"));
		} else if (Event.TYPE_WATCH.equals(type)) {
			builder.append(toBlackString("starred"));
		}

		// builder.append(typeSpanableString(event));
		builder.append(' ');
		builder.append(repositorySpanableString(event));
		return builder;
	}
}
