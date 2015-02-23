package com.gdestiny.github.utils;

public class Constants {

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
