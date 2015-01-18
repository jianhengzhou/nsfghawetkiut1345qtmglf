package com.gdestiny.github.utils;

import java.io.File;

public class Constants {

	public static final boolean isDebug = true;

	public static final String GlobalTag = "GitHub";
	public static final String GIT_JOIN = "https://github.com/join";
	public static final String DATA_PATH = android.os.Environment
			.getExternalStorageDirectory().getPath()
			+ File.separator
			+ "GitHub";
	public static final String IMAGE_PATH = DATA_PATH + File.separator
			+ "ImageCache";

	static {
		GLog.setDebug(isDebug);
	}

	// ≈≈–Ú¿‡–Õ
	public enum Sort {
		All, Star, Own, Name, User, Time
	};
}
