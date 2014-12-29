package com.gdestiny.github.utils;

import org.eclipse.egit.github.core.User;

public class TestUtils {

	public static String printUser(User user) {
		StringBuilder sb = new StringBuilder();

		sb.
		append("getAvatarUrl:").append(user.getAvatarUrl()).
		append("\ngetBlog:").append(user.getBlog()).
		append("\ngetCollaborators:").append(user.getCollaborators()).
		append("\ngetCompany:").append(user.getCompany()).
		append("\ngetCreatedAt:").append(user.getCreatedAt()).
		append("\ngetDiskUsage:").append(user.getDiskUsage()).
		append("\ngetEmail:").append(user.getEmail()).
		append("\ngetFollowers:").append(user.getFollowers()).
		append("\ngetFollowing:").append(user.getFollowing()).
		append("\ngetGravatarId:").append(user.getGravatarId()).
		append("\ngetHtmlUrl:").append(user.getHtmlUrl()).
		append("\ngetId:").append(user.getId())
		.append("\ngetLocation:").append(user.getLocation()).
		append("\ngetLogin:").append(user.getLogin()).
		append("\ngetName:").append(user.getName())
		.append("\ngetOwnedPrivateRepos:").append(user.getOwnedPrivateRepos()).
		append("\ngetPlan:").append(user.getPlan()).
		append("\ngetPrivateGists:").append(user.getPrivateGists()).
		append("\ngetPublicRepos:").append(user.getPublicRepos());
		return sb.toString();
	}
}
