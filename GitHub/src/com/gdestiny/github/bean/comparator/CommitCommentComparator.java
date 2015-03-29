package com.gdestiny.github.bean.comparator;

import java.util.Comparator;

import org.eclipse.egit.github.core.CommitComment;

public class CommitCommentComparator implements Comparator<CommitComment> {

	@Override
	public int compare(CommitComment c1, CommitComment c2) {
		if (c1.getPath() == null && c2.getPath() == null)
			return c1.getCreatedAt().compareTo(c2.getCreatedAt());
		if (c1.getPath() == null)
			return -1;
		if (c2.getPath() == null)
			return 1;
		if (c1.getPath().equals(c2.getPath())) {
			return c1.getPosition() - c2.getPosition();
		}
		return c1.getPath().compareTo(c2.getPath());
	}

}
