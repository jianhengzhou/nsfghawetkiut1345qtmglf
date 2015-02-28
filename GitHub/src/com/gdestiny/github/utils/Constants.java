package com.gdestiny.github.utils;

public class Constants {

	public final static class Extra {
		public static final String REPOSITORY = "repository";
		public static final String ISSUE = "issue";
		public static final String CODE_ENTRY = "code_entry";
		public static final String ISSUE_FILTER = "issuefilter";
		public static final String URL = "url";
	}

	public final static class Request {
		public static final int FILTER = 0;
		public static final int NEW_ISSUE = 1;
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
