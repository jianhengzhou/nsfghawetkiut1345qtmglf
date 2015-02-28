package com.gdestiny.github.utils;

import org.eclipse.egit.github.core.Issue;

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
				&& r.getTitle().equals(l.getTitle())
				&& r.getBody().equals(l.getBody())
				&& IssueFilter.equals(r.getAssignee(), l.getAssignee())
				&& IssueFilter.equals(l.getMilestone(), r.getMilestone())
				&& IssueFilter.equals(l.getLabels(), r.getLabels());
	}

	/**
	 * ������Ҫ����
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
