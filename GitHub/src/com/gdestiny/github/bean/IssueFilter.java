package com.gdestiny.github.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.Milestone;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.IssueService;

import com.gdestiny.github.utils.GLog;

public class IssueFilter implements Serializable {

	private static final long serialVersionUID = -3763458067118922195L;

	private String state = IssueService.STATE_OPEN;
	private User assignee;
	private Milestone milestone;
	private ArrayList<Label> labels;

	public void bind(Issue issue) {
		state = null;
		assignee = issue.getAssignee();
		milestone = issue.getMilestone();
		labels = (ArrayList<Label>) issue.getLabels();
	}

	public void assign(Issue issue) {
		if (!equals(assignee, issue.getAssignee())) {
			issue.setAssignee(assignee);
			GLog.sysout("assign assignee");
		}
		if (!equals(milestone, issue.getMilestone())) {
			issue.setMilestone(milestone);
			GLog.sysout("assign milestone");
		}
		if (!equals(labels, issue.getLabels())) {
			issue.setLabels(labels);
			GLog.sysout("assign labels");
		}
	}

	public IssueFilter put(String state) {
		this.state = state;
		return this;
	}

	public IssueFilter put(User assignee) {
		this.assignee = assignee;
		return this;
	}

	public IssueFilter put(Milestone milestone) {
		this.milestone = milestone;
		return this;
	}

	public IssueFilter put(ArrayList<Label> labels) {
		this.labels = labels;
		return this;
	}

	public String getState() {
		return state;
	}

	public User getAssignee() {
		return assignee;
	}

	public Milestone getMilestone() {
		return milestone;
	}

	public ArrayList<Label> getLabels() {
		return labels;
	}

	public IssueFilter DefaultState() {
		state = IssueService.STATE_OPEN;
		return this;
	}

	public boolean isMilestoneValid() {
		return milestone != null;
	}

	public boolean isAssigneeValid() {
		return assignee != null;
	}

	public boolean isLabelsValid() {
		return labels != null && !labels.isEmpty();
	}

	public IssueFilter clearAssignee() {
		if (isAssigneeValid())
			assignee = null;
		return this;
	}

	public IssueFilter clearMilestone() {
		if (isMilestoneValid())
			milestone = null;
		return this;
	}

	public IssueFilter clearLabels() {
		if (labels != null) {
			labels.clear();
			labels = null;
		}
		return this;
	}

	public Map<String, String> toHashMap() {
		Map<String, String> filter = new HashMap<String, String>();

		filter.put(IssueService.FILTER_STATE, state);
		// filter.put(FIELD_SORT, SORT_CREATED);
		// filter.put(FIELD_DIRECTION, DIRECTION_DESCENDING);

		if (isAssigneeValid()) {
			filter.put(IssueService.FILTER_ASSIGNEE, assignee.getLogin());
		}

		if (isMilestoneValid()) {
			filter.put(IssueService.FILTER_MILESTONE, milestone.getNumber()
					+ "");
		}

		if (isLabelsValid()) {
			StringBuilder sb = new StringBuilder();
			for (Label l : labels) {
				sb.append(l.getName()).append(',');
			}
			filter.put(IssueService.FILTER_LABELS, sb.toString());
		}
		return filter;
	}

	// 比较，是否更新
	public static boolean equals(Object a, Object b) {
		if (a == null && b == null)
			return true;
		return a != null && a.equals(b);
	}

	public static boolean equals(Milestone a, Milestone b) {
		if (a == null && b == null)
			return true;
		return a != null && b != null && a.getNumber() == b.getNumber();
	}

	public static boolean equals(User a, User b) {
		if (a == null && b == null)
			return true;
		return a != null && b != null && a.getId() == b.getId();
	}

	public static boolean equals(Repository a, Repository b) {
		return a != null && b != null && a.getId() == b.getId();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof IssueFilter))
			return false;

		IssueFilter other = (IssueFilter) o;
		return state.equals(other.getState())
				&& equals(milestone, other.milestone)
				&& equals(assignee, other.assignee)
				&& equals(labels, other.labels);
	}
}
