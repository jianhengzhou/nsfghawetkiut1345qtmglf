package com.gdestiny.github.async;

import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_COMMENTS;
import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_GISTS;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.Contributor;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.Milestone;
import org.eclipse.egit.github.core.Reference;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryBranch;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryCommitCompare;
import org.eclipse.egit.github.core.RepositoryIssue;
import org.eclipse.egit.github.core.SearchRepository;
import org.eclipse.egit.github.core.Tree;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.service.CollaboratorService;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.DataService;
import org.eclipse.egit.github.core.service.EventService;
import org.eclipse.egit.github.core.service.GistService;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.LabelService;
import org.eclipse.egit.github.core.service.MilestoneService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;
import org.eclipse.egit.github.core.service.WatcherService;

import android.text.TextUtils;

import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.bean.CodeTree;
import com.gdestiny.github.bean.IssueFilter;
import com.gdestiny.github.bean.SearchUser;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;

public class GitHubConsole {

	private static GitHubConsole instance;

	public RepositoryService repositoryService;
	public WatcherService watchService;
	public IssueService issueService;
	public CommitService commitService;
	public DataService dataService;
	public EventService eventService;
	public UserService userService;
	public GistService gistService;
	public CollaboratorService collaboratorService;
	public LabelService labelService;
	public MilestoneService milestoneService;

	/**
	 * 初始化
	 */
	private GitHubConsole() {
		GitHubClient client = GitHubApplication.getClient();

		repositoryService = new RepositoryService(client);
		watchService = new WatcherService(client);
		issueService = new IssueService(client);
		commitService = new CommitService(client);
		dataService = new DataService(client);
		eventService = new EventService(client);
		userService = new UserService(client);
		gistService = new GistService(client);
		collaboratorService = new CollaboratorService(client);
		labelService = new LabelService(client);
		milestoneService = new MilestoneService(client);
	}

	/**
	 * singleton
	 * 
	 * @return
	 */
	public static GitHubConsole getInstance() {
		if (instance == null) {
			instance = new GitHubConsole();
		}
		return instance;
	}

	public User getUser(String login) throws IOException {
		return userService.getUser(login);
	}

	/**
	 * get page repository according to user
	 * 
	 * @param user
	 * @return
	 * @throws IOException
	 */
	public PageIterator<Repository> pageRepositories(String user) {
		return repositoryService.pageRepositories(user,
				Constants.DEFAULT_PAGE_SIZE);
	}

	/**
	 * get own repository according to current user
	 * 
	 * @return
	 * @throws IOException
	 */
	public Repository getRepository(IRepositoryIdProvider provider)
			throws IOException {
		return repositoryService.getRepository(provider);
	}

	public List<Repository> getRepositories() throws IOException {
		return repositoryService.getRepositories();
	}

	public void fork(Repository repository) throws IOException {
		repositoryService.forkRepository(repository);
	}

	public List<SearchRepository> searchRepositories(String query)
			throws IOException {
		return repositoryService.searchRepositories(query);
	}

	public List<SearchUser> searchUsers(String query) throws IOException {
		SearchUserService searchUserService = new SearchUserService(
				GitHubApplication.getClient());
		return searchUserService.searchUsers(query);
	}

	/**
	 * get 的watch repository according to current user
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<Repository> getWatchRepositories() throws IOException {
		return watchService.getWatched();
	}

	/**
	 * is watch a repository
	 * 
	 * @param repository
	 * @return
	 * @throws IOException
	 */
	public boolean isWatching(IRepositoryIdProvider repository)
			throws IOException {
		return watchService.isWatching(repository);
	}

	/**
	 * to watch a repository
	 * 
	 * @param repository
	 * @throws IOException
	 */
	public void watch(IRepositoryIdProvider repository) throws IOException {
		watchService.watch(repository);
	}

	/**
	 * to unwatch a repository
	 * 
	 * @param repository
	 * @throws IOException
	 */
	public void unwatch(IRepositoryIdProvider repository) throws IOException {
		watchService.unwatch(repository);
	}

	public void follow(String user) throws IOException {
		userService.follow(user);
	}

	public void unfollow(String user) throws IOException {
		userService.unfollow(user);
	}

	/**
	 * is repository forked?
	 * 
	 * @param repository
	 * @return
	 * @throws IOException
	 */
	public boolean isFork(Repository repository) throws IOException {
		return repository.isFork();
	}

	/**
	 * get the code data tree the of the repository of the current branch
	 * 
	 * @param repository
	 * @param curBranch
	 * @return
	 * @throws IOException
	 */
	public Tree getTree(Repository repository, String curBranch)
			throws IOException {
		GLog.sysout(repository.getMasterBranch());
		Reference ref = dataService.getReference(repository, "heads/"
				+ curBranch);
		Commit commit = dataService.getCommit(repository, ref.getObject()
				.getSha());
		Tree tree = dataService.getTree(repository, commit.getTree().getSha(),
				true);
		return tree;
	}

	/**
	 * get codeTree
	 * 
	 * @param repository
	 * @param curBranch
	 * @return
	 * @throws IOException
	 */
	public CodeTree getCodeTree(Repository repository, String curBranch)
			throws IOException {
		return CodeTree.toCodeTree(getTree(repository, curBranch));
	}

	/**
	 * get commit data of the repository in the current branch
	 * 
	 * @param repository
	 * @param curBranch
	 * @return
	 */
	public PageIterator<RepositoryCommit> pageCommits(Repository repository,
			String curBranch) {
		if (TextUtils.isEmpty(curBranch)) {
			try {
				curBranch = repositoryService.getRepository(repository)
						.getMasterBranch();
			} catch (IOException e) {
				e.printStackTrace();
				curBranch = "master";
			}
			if (TextUtils.isEmpty(curBranch))
				curBranch = "master";
		}
		return commitService.pageCommits(repository, curBranch, null,
				Constants.DEFAULT_PAGE_SIZE);
	}

	public Issue getIssue(IRepositoryIdProvider repository, int issueNumber)
			throws IOException {
		return issueService.getIssue(repository, issueNumber);
	}

	public RepositoryCommit getCommit(IRepositoryIdProvider repository,
			String sha) throws IOException {
		return commitService.getCommit(repository, sha);
	}

	public RepositoryCommitCompare getCommitCompare(
			IRepositoryIdProvider repository, String base, String head)
			throws IOException {
		return commitService.compare(repository, base, head);
	}

	public List<CommitComment> getCommitComment(
			IRepositoryIdProvider repository, String sha) throws IOException {
		return commitService.getComments(repository, sha);
	}

	public boolean isCollaborator(IRepositoryIdProvider repository, String user) {
		try {
			return collaboratorService.isCollaborator(repository, user);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<User> getCollaborator(IRepositoryIdProvider repository)
			throws IOException {
		return collaboratorService.getCollaborators(repository);
	}

	public List<Contributor> getContributor(IRepositoryIdProvider repository)
			throws IOException {
		return repositoryService.getContributors(repository, false);
	}

	/**
	 * get the branch of the repository
	 * 
	 * @param repository
	 * @return
	 */
	public List<RepositoryBranch> getBranch(IRepositoryIdProvider repository) {
		List<RepositoryBranch> branch = null;
		try {
			branch = repositoryService.getBranches(repository);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return branch;
	}

	/**
	 * 
	 * @param repository
	 * @return
	 */
	public PageIterator<Event> pageEvents(IRepositoryIdProvider repository) {
		return eventService.pageEvents(repository, Constants.DEFAULT_PAGE_SIZE);
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	public PageIterator<Event> pageUserEvents(String user) {
		return eventService.pageUserEvents(user, false,
				Constants.DEFAULT_PAGE_SIZE);
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	public PageIterator<Event> pageUserReceivedEvents(String user) {
		return eventService.pageUserReceivedEvents(user, false,
				Constants.DEFAULT_PAGE_SIZE);
	}

	public PageIterator<Issue> pageIssues(IRepositoryIdProvider repository,
			IssueFilter issueFilter) {
		return issueService.pageIssues(repository, issueFilter == null ? null
				: issueFilter.toHashMap(), Constants.DEFAULT_PAGE_SIZE);
	}

	public PageIterator<RepositoryIssue> pageIssues(
			Map<String, String> filterData) {
		return issueService.pageIssues(filterData, Constants.DEFAULT_PAGE_SIZE);
	}

	public List<Comment> getIssueComments(IRepositoryIdProvider repository,
			int issueNumber) throws IOException {
		return issueService.getComments(repository, issueNumber);
	}

	public PageIterator<User> pageFollowers() {
		return userService.pageFollowers(Constants.DEFAULT_PAGE_SIZE);
	}

	public PageIterator<User> pageFollowers(String user) {
		return userService.pageFollowers(user, Constants.DEFAULT_PAGE_SIZE);
	}

	public PageIterator<User> pageFollowing() {
		return userService.pageFollowing(Constants.DEFAULT_PAGE_SIZE);
	}

	public PageIterator<User> pageFollowing(String user) {
		return userService.pageFollowing(user, Constants.DEFAULT_PAGE_SIZE);
	}

	public PageIterator<Gist> pageGists(String user) {
		return gistService.pageGists(user, Constants.DEFAULT_PAGE_SIZE);
	}

	public PageIterator<Gist> pagePublicGists() {
		return gistService.pagePublicGists(Constants.DEFAULT_PAGE_SIZE);
	}

	public PageIterator<Gist> pageStarredGists() {
		return gistService.pageStarredGists(Constants.DEFAULT_PAGE_SIZE);
	}

	public boolean isStarred(String gistId) throws IOException {
		return gistService.isStarred(gistId);
	}

	public List<Comment> getGistComment(String gistId) throws IOException {
		return gistService.getComments(gistId);
	}

	public Gist getGist(String gistId) throws IOException {
		return gistService.getGist(gistId);
	}

	public Comment editIssueComment(IRepositoryIdProvider repository,
			Comment comment) throws IOException {
		return issueService.editComment(repository, comment);
	}

	public Comment createIssueComment(IRepositoryIdProvider repository,
			int issueNumber, String comment) throws IOException {
		return issueService.createComment(repository, issueNumber, comment);
	}

	public void deleteIssueComment(IRepositoryIdProvider repository,
			long commentId) throws IOException {
		issueService.deleteComment(repository, commentId);
	}

	public CommitComment editCommitComment(IRepositoryIdProvider repository,
			CommitComment comment) throws IOException {
		return commitService.editComment(repository, comment);
	}

	public CommitComment createCommitComment(IRepositoryIdProvider repository,
			String sha, CommitComment comment) throws IOException {
		return commitService.addComment(repository, sha, comment);
	}

	public void deleteCommitComment(IRepositoryIdProvider repository,
			long commentId) throws IOException {
		commitService.deleteComment(repository, commentId);
	}

	public void deleteGistComment(String gistId, Comment comment)
			throws IOException {// 醉了
		// service.deleteComment(comment.getId());
		StringBuilder uri = new StringBuilder(SEGMENT_GISTS);
		uri.append('/').append(gistId);
		uri.append(SEGMENT_COMMENTS);
		uri.append('/').append(comment.getId());
		gistService.getClient().delete(uri.toString());
	}

	public void deleteGist(String gistId) throws IOException {
		gistService.deleteGist(gistId);
	}

	public User editUser(User user) throws IOException {
		return userService.editUser(user);
	}

	public void forkGist(String gistId) throws IOException {
		gistService.forkGist(gistId);
	}

	/**
	 * Edit gist comment.
	 * 
	 * TODO: Remove this method once egit GistService.java Gist Comment APIs are
	 * fixed. https://github.com/eclipse/egit-github/pull/7
	 * 
	 * @param comment
	 * @return edited comment
	 * @throws IOException
	 */
	public Comment editGistComment(String gistId, Comment comment)
			throws IOException {
		StringBuilder uri = new StringBuilder(SEGMENT_GISTS);
		uri.append('/').append(gistId);
		uri.append(SEGMENT_COMMENTS);
		uri.append('/').append(comment.getId());
		return gistService.getClient().post(uri.toString(), comment,
				Comment.class);
	}

	public Comment createGistComment(String gistId, String comment)
			throws IOException {
		return gistService.createComment(gistId, comment);
	}

	public List<Label> getLabels(IRepositoryIdProvider repository)
			throws IOException {
		return labelService.getLabels(repository);
	}

	public List<Milestone> getMilestones(IRepositoryIdProvider repository,
			String state) throws IOException {
		return milestoneService.getMilestones(repository, state);
	}

	public Issue editIssue(IRepositoryIdProvider repository, Issue issue)
			throws IOException {
		return issueService.editIssue(repository, issue);
	}

	public Issue createIssue(IRepositoryIdProvider repository, Issue issue)
			throws IOException {
		return issueService.createIssue(repository, issue);
	}

	public Gist createGist(Gist gist) throws IOException {
		return gistService.createGist(gist);
	}

	public void starGist(String gistId) throws IOException {
		gistService.starGist(gistId);
	}

	public void unstarGist(String gistId) throws IOException {
		gistService.unstarGist(gistId);
	}
}
