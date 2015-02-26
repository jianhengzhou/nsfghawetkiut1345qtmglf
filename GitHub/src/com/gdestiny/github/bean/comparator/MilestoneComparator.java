package com.gdestiny.github.bean.comparator;

import java.util.Comparator;

import org.eclipse.egit.github.core.Milestone;

public class MilestoneComparator implements Comparator<Milestone> {

	public int compare(Milestone m1, Milestone m2) {
		return m2.getCreatedAt().compareTo(m1.getCreatedAt());
	}

}
