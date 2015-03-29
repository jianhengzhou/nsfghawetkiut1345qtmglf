package com.gdestiny.github.utils;

public class Constants {

	public final static class Extra {
		public static final String REPOSITORY = "repository";
		public static final String ISSUE = "issue";
		public static final String CODE_ENTRY = "code_entry";
		public static final String ISSUE_FILTER = "issuefilter";
		public static final String URL = "url";
		public static final String POSITION = "position";
		public static final String GRIUP_POSITION = "group_position";
		public static final String CHILD_POSITION = "child_position";
		public static final String COMMENT = "comment";
		public static final String REPOSITORY_COMMIT = "repository_commit";
		public static final String COMMIT = "commit";
		public static final String COMMIT_FILE = "commitfile";
		public static final String COMMIT_COMMENT = "commitComment";
		public static final String SHA = "sha";
		public static final String PATH = "path";
		public static final String COMMIT_LINE = "commitLine";
		public static final String COMMENT_COUNT = "comment_count";
		public static final String USER = "user";
		public static final String ID = "id";
		public static final String GIST = "gist";
		public static final String FILE_NAME = "file_name";
		public static final String DATA = "data";
		public static final String USER_INFO = "userinfo";
	}

	public final static class Request {
		public static final int FILTER = 0;
		public static final int NEW_ISSUE = 1;
		public static final int EDIT_ISSUE = 2;
		public static final int ISSUE_DETAIL = 3;
		public static final int ISSUE_COMMENT = 4;
		public static final int EDIT_COMMENT = 5;
		public static final int COMMIT_COMMENT = 6;
		public static final int RAW_COMMIT_COMMENT = 7;
		public static final int COMMIT_DETAIL = 8;
		public static final int NEW_GIST = 9;
		public static final int GIST_DETAIL = 10;
		public static final int GIST_COMMENT = 11;
		public static final int USER_INFO = 12;
	}

	public static final boolean isDebug = true;

	public static final String GlobalTag = "GitHub";
	public static final String GIT_JOIN = "https://github.com/join";

	public static final int DEFAULT_PAGE_SIZE = 5;
	static {
		GLog.setDebug(isDebug);
	}

	public static final int CONNECT_TIMEOUT = 15000;
	public static final int READ_TIMEOUT = 15000;

	// ≈≈–Ú¿‡–Õ
	public enum Sort {
		All, Star, Own, Name, User, Time
	};
}
