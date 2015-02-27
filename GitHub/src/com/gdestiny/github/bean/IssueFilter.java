package com.gdestiny.github.bean;

import java.io.Serializable;
import java.util.ArrayList;

import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.Milestone;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.IssueService;

public class IssueFilter implements Serializable {

	private static final long serialVersionUID = -3763458067118922195L;

	private String state = IssueService.STATE_OPEN;
	private User assignee;
	private Milestone milestone;
	private ArrayList<Label> labels;

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

	public void toMap() {

	}
}
