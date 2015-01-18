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
			return lhs.getOwner().getLogin()
					.compareToIgnoreCase(rhs.getOwner().getLogin());
		default:
			return lhs.getName().compareToIgnoreCase(rhs.getName());
		}
	}
}
