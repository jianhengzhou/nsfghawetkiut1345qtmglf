package com.gdestiny.github.utils;

public class Constants {

	public static final boolean isDebug = true;
	
	public static final String GlobalTag = "GitHub";
	public static final String GIT_JOIN = "https://github.com/join";
	public static final String DATA_PATH = "GitHub";
	public static final String IMAGE_PATH = DATA_PATH + "/ImageCache";

	static {
		GLog.setDebug(isDebug);
	}
}
