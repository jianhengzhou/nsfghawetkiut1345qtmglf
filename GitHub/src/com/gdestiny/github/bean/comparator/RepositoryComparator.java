package com.gdestiny.github.bean.comparator;

import java.util.Comparator;

import org.eclipse.egit.github.core.Repository;

import com.gdestiny.github.utils.Constants.Sort;

public class RepositoryComparator implements Comparator<Repository> {

	private Sort sort;

	public RepositoryComparator(Sort sort) {
		this.sort = sort;
	}

	@Override
	public int compare(Repository lhs, Repository rhs) {
		switch (sort) {
		case Time:
			return lhs.getCreatedAt().compareTo(rhs.getCreatedAt());
		case User:
			String name1 = lhs.getOwner().getLogin();
			String name2 = rhs.getOwner().getLogin();
			if (name1.equals(name2))
				return lhs.getName().compareToIgnoreCase(rhs.getName());
			return name1.compareToIgnoreCase(name2);
		default:
			return lhs.getName().compareToIgnoreCase(rhs.getName());
		}
	}
}
