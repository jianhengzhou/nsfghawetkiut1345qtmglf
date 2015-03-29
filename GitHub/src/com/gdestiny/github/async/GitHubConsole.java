package com.gdestiny.github.async;

import java.io.IOException;
import java.util.List;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.RepositoryService;

import com.gdestiny.github.app.GitHubApplication;

public class GitHubConsole {

	private static GitHubConsole instance;

	public RepositoryService repositoryService;
	public IssueService issueService;
	public CommitService commitService;

	private GitHubConsole() {
		GitHubClient client = GitHubApplication.getClient();

		repositoryService = new RepositoryService(client);
	}

	public static GitHubConsole getInstance() {
		if (instance == null) {
			instance = new GitHubConsole();

		}
		return instance;
	}

	public List<Repository> getRepositories() throws IOException {
		return repositoryService.getRepositories();
	}
}
