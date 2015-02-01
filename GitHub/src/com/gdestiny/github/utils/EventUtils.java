package com.gdestiny.github.utils;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.event.IssueCommentPayload;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
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
			return "";
		} else if (Event.TYPE_CREATE.equals(type)) {
			return "created repository";
		} else if (Event.TYPE_DELETE.equals(type)) {
			return "";
		} else if (Event.TYPE_DOWNLOAD.equals(type)) {
			return "";
		} else if (Event.TYPE_FOLLOW.equals(type)) {
			return "";
		} else if (Event.TYPE_FORK.equals(type)) {
			return "forked";
		} else if (Event.TYPE_FORK_APPLY.equals(type)) {
			return "";
		} else if (Event.TYPE_GIST.equals(type)) {
			return "";
		} else if (Event.TYPE_GOLLUM.equals(type)) {
			return "edited the wiki in";
		} else if (Event.TYPE_ISSUE_COMMENT.equals(type)) {
			return "";
		} else if (Event.TYPE_ISSUES.equals(type)) {
			return "";
		} else if (Event.TYPE_MEMBER.equals(type)) {
			return "";
		} else if (Event.TYPE_PUBLIC.equals(type)) {
			return "open sourced";
		} else if (Event.TYPE_PULL_REQUEST.equals(type)) {
			return "opened pull request";
		} else if (Event.TYPE_PULL_REQUEST_REVIEW_COMMENT.equals(type)) {
			return "";
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

	public static final ForegroundColorSpan nameSpan = new ForegroundColorSpan(
			0xff56abe4);
	public static final ForegroundColorSpan typeSpan = new ForegroundColorSpan(
			0xff2a2a2a);
	public static final ForegroundColorSpan repositorySpan = new ForegroundColorSpan(
			0xff56abe4);

	public static SpannableStringBuilder toSpannableString(Event event) {

		SpannableString author = new SpannableString(getAuthor(event));
		author.setSpan(nameSpan, 0, author.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		SpannableString type = new SpannableString(getEventType(event).equals(
				"") ? event.getType() : getEventType(event));
		type.setSpan(typeSpan, 0, type.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		SpannableString repository = new SpannableString(event.getRepo()
				.getName());
		repository.setSpan(repositorySpan, 0, repository.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		SpannableStringBuilder builder = new SpannableStringBuilder();
		builder.append(author);
		builder.append('\n');
		builder.append(type);
		builder.append('\n');
		builder.append(repository);
		return builder;
	}
}
