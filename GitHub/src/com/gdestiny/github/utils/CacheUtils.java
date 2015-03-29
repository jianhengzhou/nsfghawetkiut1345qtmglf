package com.gdestiny.github.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

/**
 * 1¡¢Bitmap cacheºÍImageLoadµÄDiskCache
 * 
 * @author gdestiny
 * 
 */
public class CacheUtils {

	public static final String TAG = "CacheUtils";

	public static final String SD_PATH = android.os.Environment
			.getExternalStorageDirectory().getPath();
	public static final String DATA_PATH = SD_PATH + File.separator + "GitHub";
	public static final String IMAGE_PATH = DATA_PATH + File.separator
			+ "ImageCache";

	static {
		GLog.d(TAG, "SD_PATH:" + SD_PATH + "\nDATA_PATH:" + DATA_PATH
				+ "\nIMAGE_PATH:" + IMAGE_PATH);
	}

	public static final String SnappyDB_Internal = "CACHE_SNAPPYDB";
	public static final String SnappyDB_External = DATA_PATH + File.separator
			+ "SNAPPYDB_EXTERNAL";

	public static FileNameGenerator nameGenerator = new Md5FileNameGenerator();

	public static void init(Context context) {
		ImageLoaderUtils.initImageLoader(context);
	}

	public static boolean isBitmapExistInDisk(String url) {
		File file = DiskCacheUtils.findInCache(url,
				ImageLoaderUtils.getCurrenyDiskCache());
		return file == null ? false : file.exists();
	}

	public static String cache(String url, Bitmap bm) {
		if (isBitmapExistInDisk(url)) {
			GLog.d(TAG, "isBitmapExistInDisk:" + url);
			return null;
		} else {
			try {
				if (ImageLoaderUtils.getCurrenyDiskCache().save(url, bm)) {
					return getBitmapPath(url);
				}
			} catch (IOException e) {
				e.printStackTrace();
				GLog.d(TAG, "cacheBitmap IOException:" + e.getMessage());
				return null;
			}
		}
		return null;
	}

	public static String cache(String url, byte[] data) {
		if (isBitmapExistInDisk(url)) {
			GLog.d(TAG, "isBitmapExistInDisk:" + url);
			return null;
		} else {
			try {
				Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
				if (ImageLoaderUtils.getCurrenyDiskCache().save(url, bm)) {
					return getBitmapPath(url);
				}
			} catch (IOException e) {
				e.printStackTrace();
				GLog.d(TAG, "cacheBitmap IOException:" + e.getMessage());
				return null;
			}
		}
		return null;
	}

	public static Bitmap getBitmap(String url) {
		if (isBitmapExistInDisk(url)) {
			File file = ImageLoaderUtils.getCurrenyDiskCache().get(url);
			GLog.d(TAG, "getBitmapFromDisk " + url);
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

	public static void cacheMemory(String url, Bitmap bitmap) {

		String key = MemoryCacheUtils.generateKey(url,
				new ImageSize(bitmap.getWidth(), bitmap.getHeight()));

		if (!ImageLoaderUtils.getMemoryCache().keys().contains(bitmap)) {
			ImageLoaderUtils.getMemoryCache().put(key, bitmap);
			GLog.d(TAG, "cacheMemory " + url);
		} else {
			GLog.d(TAG, "exist in cacheMemory " + url);
		}
	}

	public static Bitmap getBitmapFromMemory(String url) {

		List<Bitmap> bitmaps = MemoryCacheUtils.findCachedBitmapsForImageUri(
				url, ImageLoaderUtils.getMemoryCache());

		if (bitmaps != null && !bitmaps.isEmpty()) {
			GLog.d(TAG, "getBitmapFromMemory " + url);
			return bitmaps.get(0);
		}

		return null;
	}
}
