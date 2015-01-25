package com.gdestiny.github.utils;

import java.io.File;

public class CacheUtils {

	public static final String SD_PATH = android.os.Environment
			.getExternalStorageDirectory().getPath();
	public static final String DATA_PATH = SD_PATH + File.separator + "GitHub";
	public static final String IMAGE_PATH = DATA_PATH + File.separator
			+ "ImageCache";

	static {
		GLog.sysout("SD_PATH:" + SD_PATH + "\nDATA_PATH:" + DATA_PATH
				+ "\nIMAGE_PATH:" + IMAGE_PATH);
	}

	public static final String SnappyDB_Internal = "CACHE_SNAPPYDB";
	public static final String SnappyDB_External = DATA_PATH + File.separator
			+ "SNAPPYDB_EXTERNAL";
}
