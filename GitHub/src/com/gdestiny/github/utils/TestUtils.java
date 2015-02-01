package com.gdestiny.github.utils;

import java.util.List;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryBranch;
import org.eclipse.egit.github.core.Tree;
import org.eclipse.egit.github.core.TreeEntry;
import org.eclipse.egit.github.core.User;

public class TestUtils {

	public static void interrupt(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String printUser(User user) {
		StringBuilder sb = new StringBuilder();

		sb.append("getAvatarUrl:").append(user.getAvatarUrl())
				.append("\ngetBlog:").append(user.getBlog())
				.append("\ngetCollaborators:").append(user.getCollaborators())
				.append("\ngetCompany:").append(user.getCompany())
				.append("\ngetCreatedAt:").append(user.getCreatedAt())
				.append("\ngetDiskUsage:").append(user.getDiskUsage())
				.append("\ngetEmail:").append(user.getEmail())
				.append("\ngetFollowers:").append(user.getFollowers())
				.append("\ngetFollowing:").append(user.getFollowing())
				.append("\ngetGravatarId:").append(user.getGravatarId())
				.append("\ngetHtmlUrl:").append(user.getHtmlUrl())
				.append("\ngetId:").append(user.getId())
				.append("\ngetLocation:").append(user.getLocation())
				.append("\ngetLogin:").append(user.getLogin())
				.append("\ngetName:").append(user.getName())
				.append("\ngetOwnedPrivateRepos:")
				.append(user.getOwnedPrivateRepos()).append("\ngetPlan:")
				.append(user.getPlan()).append("\ngetPrivateGists:")
				.append(user.getPrivateGists()).append("\ngetPublicRepos:")
				.append(user.getPublicRepos());
		return sb.toString();
	}

	public static String printListRepository(List<Repository> list) {
		StringBuilder sb = new StringBuilder();

		for (Repository rep : list) {
			sb.append("\n----------------\n");
			sb.append(printRepository(rep));
		}
		return sb.toString();
	}

	public static String printRepository(Repository rep) {
		StringBuilder sb = new StringBuilder();

		sb.append("getCloneUrl:").append(rep.getCloneUrl())
				.append("\ngetDescription:").append(rep.getDescription())
				.append("\ngetForks:").append(rep.getForks())
				.append("\ngetGitUrl:").append(rep.getGitUrl())
				.append("\ngetHomepage:").append(rep.getHomepage())
				.append("\ngetHtmlUrl:").append(rep.getHtmlUrl())
				.append("\ngetId:").append(rep.getId())
				.append("\ngetLanguage:").append(rep.getLanguage())
				.append("\ngetMasterBranch:").append(rep.getMasterBranch())
				.append("\ngetMirrorUrl:").append(rep.getMirrorUrl())
				.append("\ngetName:").append(rep.getName())
				.append("\ngetOpenIssues:").append(rep.getOpenIssues())
				.append("\ngetSize:").append(rep.getSize())
				.append("\ngetSshUrl:").append(rep.getSshUrl())
				.append("\ngetSvnUrl:").append(rep.getSvnUrl())
				.append("\ngetUrl:").append(rep.getUrl())
				.append("\ngetWatchers:").append(rep.getWatchers())
				.append("\ngetCreatedAt:").append(rep.getCreatedAt())
				.append("\ngetOwner:").append(rep.getOwner().getLogin());
		return sb.toString();
	}

	public static String printTree(Tree tree) {
		StringBuilder sb = new StringBuilder();
		List<TreeEntry> list = tree.getTree();
		for (TreeEntry te : list) {
			sb.append("----------------------").append("\ngetMode:")
					.append(te.getMode()).append("\ngetPath:")
					.append(te.getPath()).append("\ngetSha:")
					.append(te.getSha()).append("\ngetSize:")
					.append(te.getSize()).append("\ngetType:")
					.append(te.getType()).append("\ngetUrl:")
					.append(te.getUrl()).append("\n----------------------\n");
		}
		return sb.toString();
	}

	public static String printBranch(List<RepositoryBranch> branch) {
		StringBuilder sb = new StringBuilder();
		for (RepositoryBranch rb : branch) {
			sb.append("----------------------").append("\ngetName:")
					.append(rb.getName());
		}
		return sb.toString();
	}
}
