package com.gdestiny.github.utils.client;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.PullRequest;

import com.gdestiny.github.bean.IssueFilter;

public class IssueUtils {

	private IssueUtils() {
		throw new AssertionError();
	}

	public static boolean equal(Issue l, Issue r) {
		if (l == null && r == null)
			return true;
		if (l == null || r == null)
			return false;
		return l.getNumber() == r.getNumber()
				&& l.getTitle().equals(r.getTitle())
				&& l.getBody().equals(r.getBody())
				&& IssueFilter.equals(l.getAssignee(), r.getAssignee())
				&& IssueFilter.equals(l.getMilestone(), r.getMilestone())
				&& IssueFilter.equals(l.getLabels(), r.getLabels());
	}

	public static Issue toIssue(final PullRequest pullRequest) {
		if (pullRequest == null)
			return null;

		Issue issue = new Issue();
		issue.setAssignee(pullRequest.getAssignee());
		issue.setBody(pullRequest.getBody());
		issue.setBodyHtml(pullRequest.getBodyHtml());
		issue.setBodyText(pullRequest.getBodyText());
		issue.setClosedAt(pullRequest.getClosedAt());
		issue.setComments(pullRequest.getComments());
		issue.setCreatedAt(pullRequest.getCreatedAt());
		issue.setHtmlUrl(pullRequest.getHtmlUrl());
		issue.setId(pullRequest.getId());
		issue.setMilestone(pullRequest.getMilestone());
		issue.setNumber(pullRequest.getNumber());
		issue.setPullRequest(pullRequest);
		issue.setState(pullRequest.getState());
		issue.setTitle(pullRequest.getTitle());
		issue.setUpdatedAt(pullRequest.getUpdatedAt());
		issue.setUrl(pullRequest.getUrl());
		issue.setUser(pullRequest.getUser());
		return issue;
	}

	/**
	 * 更新主要属性
	 * 
	 * @param to
	 * @param from
	 */
	public static void assignMain(Issue to, Issue from) {
		if (to == null || from == null) {
			throw new IllegalArgumentException("issue from to can not be null");
		}
		to.setBody(from.getBody());
		to.setBodyHtml(from.getBodyHtml());
		to.setBodyText(from.getBodyText());
		to.setComments(from.getComments());

		if (!IssueFilter.equals(to.getAssignee(), from.getAssignee()))
			to.setAssignee(from.getAssignee());
		if (!IssueFilter.equals(to.getMilestone(), from.getMilestone()))
			to.setMilestone(from.getMilestone());
		if (!IssueFilter.equals(to.getLabels(), from.getLabels()))
			to.setLabels(from.getLabels());
	}
}
