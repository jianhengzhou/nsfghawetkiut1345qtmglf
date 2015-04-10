package com.gdestiny.github.utils.client;

import java.util.Date;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitUser;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.User;

import com.gdestiny.github.utils.gravatar.Gravatar;

public class CommitUtils {

	private CommitUtils() {
		throw new AssertionError();
	}

	public static String getAuthor(RepositoryCommit repositoryCommit) {
		User author = repositoryCommit.getAuthor();
		if (author != null)
			return author.getLogin();

		Commit commit = repositoryCommit.getCommit();
		if (commit == null)
			return null;

		CommitUser commitAuthor = commit.getAuthor();
		return commitAuthor != null ? commitAuthor.getName() : null;
	}

	public static Date getAuthorDate(RepositoryCommit repositoryCommit) {
		Commit commit = repositoryCommit.getCommit();
		if (commit == null)
			return null;

		CommitUser commitAuthor = commit.getAuthor();
		return commitAuthor != null ? commitAuthor.getDate() : null;
	}

	public static String getAuthorAvatarUrl(RepositoryCommit repositoryCommit) {
		User author = repositoryCommit.getAuthor();
		if (author != null)
			return author.getAvatarUrl();
		// TODO EmailÍ·Ïñ
		// https://secure.gravatar.com/avatar/hashid?d=404
		// https://github.com/tkeunebr/gravatar-android
		Commit commit = repositoryCommit.getCommit();
		if (commit == null)
			return null;

		CommitUser commitAuthor = commit.getAuthor();
		if (commitAuthor != null)
			return Gravatar.init().with(commitAuthor.getEmail())
					.size(Gravatar.MAX_IMAGE_SIZE_PIXEL).build();
		return null;
	}

	public static String getSubSha(RepositoryCommit repositoryCommit) {
		String sha = repositoryCommit.getSha();
		if (sha.length() > 10)
			return sha.substring(0, 10);
		return sha;
	}
}
