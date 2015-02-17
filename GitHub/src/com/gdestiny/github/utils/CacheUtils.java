package com.gdestiny.github.utils;

import java.io.File;
import java.io.IOException;

import com.nostra13.universalimageloader.utils.DiskCacheUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 1¡¢Bitmap cacheºÍImageLoadµÄDiskCache
 * 
 * @author gdestiny
 * 
 */
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

	public static boolean isBitmapExistInDisk(String url) {
		File file = DiskCacheUtils.findInCache(url,
				ImageLoaderUtils.getCurrenyDiskCache());
		return file == null ? false : file.exists();
	}

	public static String cache(String url, Bitmap bm) {
		if (isBitmapExistInDisk(url)) {
			GLog.sysout("isBitmapExistInDisk:" + url);
			return null;
		} else {
			try {
				if (ImageLoaderUtils.getCurrenyDiskCache().save(url, bm)) {
					return getBitmapPath(url);
				}
			} catch (IOException e) {
				e.printStackTrace();
				GLog.sysout("cacheBitmap IOException:" + e.getMessage());
				return null;
			}
		}
		return null;
	}

	public static String cache(String url, byte[] data) {
		if (isBitmapExistInDisk(url)) {
			GLog.sysout("isBitmapExistInDisk:" + url);
			return null;
		} else {
			try {
				Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
				if (ImageLoaderUtils.getCurrenyDiskCache().save(url, bm)) {
					return getBitmapPath(url);
				}
			} catch (IOException e) {
				e.printStackTrace();
				GLog.sysout("cacheBitmap IOException:" + e.getMessage());
				return null;
			}
		}
		return null;
	}

	public static Bitmap getBitmap(String url) {
		if (isBitmapExistInDisk(url)) {
			File file = ImageLoaderUtils.getCurrenyDiskCache().get(url);
			return BitmapFactory.decodeFile(file.getPath());
		}
		return null;
	}

	public static String getBitmapPath(String url) {
		if (isBitmapExistInDisk(url)) {
			File file = ImageLoaderUtils.getCurrenyDiskCache().get(url);
			return file.getPath();
		}
		return null;
	}
}
