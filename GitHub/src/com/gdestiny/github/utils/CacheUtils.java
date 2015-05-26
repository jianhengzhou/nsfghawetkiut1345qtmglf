package com.gdestiny.github.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.eclipse.egit.github.core.client.GsonUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

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

	public static com.gdestiny.github.cache.disk.impl.BaseDiskCache diskCache;

	public static final String SD_PATH = android.os.Environment
			.getExternalStorageDirectory().getPath();
	public static final String DATA_PATH = SD_PATH + File.separator + "GitHub";
	public static final String IMAGE_PATH = DATA_PATH + File.separator
			+ "ImageCache/";

	static {
		GLog.d(TAG, "SD_PATH:" + SD_PATH + "\nDATA_PATH:" + DATA_PATH
				+ "\nIMAGE_PATH:" + IMAGE_PATH);
	}

	public static final String SnappyDB_Internal = "CACHE_SNAPPYDB";
	public static final String SnappyDB_External = DATA_PATH + File.separator
			+ "SNAPPYDB_EXTERNAL";

	public static FileNameGenerator nameGenerator = new Md5FileNameGenerator();

	public static final class NAME {
		public static final String LIST_REPOSITORY = "repository.json";
		public static final String LIST_OWN_REPOSITORY = "repositoryOwn.json";
		public static final String LIST_STAR_REPOSITORY = "repositoryStar.json";
		public static final String LIST_EVENTS = "eventsList.json";
		public static final String LIST_FOLLOWER = "followerList.json";
		public static final String LIST_FOLLOWING = "followingList.json";
		public static final String LIST_GIST = "gistList.json";
		public static final String LIST_ISSUE = "issueList.json";
		public static final String LIST_COMMIT = "commitList.json";
		public static final String CODE_TREE = "codeTree.json";
		public static final String CONTRIBUTION = "contribution.html";
		public static final String SEARCH_REPO = "searchRepo.json";
		public static final String SEARCH_USER = "searchUser.json";
		public static final String SEARCH_HISTORY = "searchHistory";
	}

	public static final class DIR {
		public static final String USER = "user/";
		public static final String GIST = "gist/";
		public static final String ISSUE = "issue/";
		public static final String COMMIT = "commit/";
		public static final String EVENT = "event/";
		public static final String CODE_TREE = "code tree/";
	}

	public static void init(Context context) {
		ImageLoaderUtils.initImageLoader(context);
		diskCache = new com.gdestiny.github.cache.disk.impl.BaseDiskCache(
				DATA_PATH, new FileNameGenerator() {

					@Override
					public String generate(String imageUri) {
						return imageUri;
					}
				});
	}

	public static final boolean isBitmapExistInDisk(String url) {
		File file = DiskCacheUtils.findInCache(url,
				ImageLoaderUtils.getCurrenyDiskCache());
		return file == null ? false : file.exists();
	}

	public static final String cacheBitmap(String url, Bitmap bm) {
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

	public static final String cacheBitmap(String url, byte[] data) {
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

	public static final Bitmap getCacheBitmap(String url) {
		if (isBitmapExistInDisk(url)) {
			File file = ImageLoaderUtils.getCurrenyDiskCache().get(url);
			GLog.d(TAG, "getBitmapFromDisk " + url);
			return BitmapFactory.decodeFile(file.getPath());
		}
		return null;
	}

	public static final String getBitmapPath(String url) {
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

	public static final Bitmap getBitmapFromMemory(String url) {

		List<Bitmap> bitmaps = MemoryCacheUtils.findCachedBitmapsForImageUri(
				url, ImageLoaderUtils.getMemoryCache());

		if (bitmaps != null && !bitmaps.isEmpty()) {
			GLog.d(TAG, "getBitmapFromMemory " + url);
			return bitmaps.get(0);
		}

		return null;
	}

	public static boolean contain(String name) {
		return diskCache.contain(name);
	}

	public static void cacheObject(String name, final Object object) {
		String json = GsonUtils.toJson(object);
		try {
			diskCache.save(name, json);
		} catch (IOException e) {
			e.printStackTrace();
			GLog.d(TAG, "cacheObject error: " + name);
		}
	}

	public static void cacheObject(String subDir, String name,
			final Object object) {
		String json = GsonUtils.toJson(object);
		try {
			diskCache.save(subDir, name, json);
		} catch (IOException e) {
			e.printStackTrace();
			GLog.d(TAG, "cacheObject error: " + name);
		}
	}

	public static final <V> V getCacheObject(String name, Class<V> type) {
		if (TextUtils.isEmpty(name))
			return null;
		GLog.d(TAG, "getCacheObject: " + name);
		String json = diskCache.getContent(name);
		return GsonUtils.fromJson(json, type);
	}

	public static final <V> V getCacheObject(String name, Type type) {
		if (TextUtils.isEmpty(name))
			return null;
		GLog.d(TAG, "getCacheObject: " + name);
		String json = diskCache.getContent(name);
		return GsonUtils.fromJson(json, type);
	}

	public static final <V> V getCacheObject(String subDir, String name,
			Type type) {
		if (TextUtils.isEmpty(name))
			return null;
		GLog.d(TAG, "getCacheObject: " + name);
		String json = diskCache.getContent(subDir, name);
		return GsonUtils.fromJson(json, type);
	}

	public static void cacheString(String name, final String content) {
		GLog.d(TAG, "cacheString: " + name);
		try {
			diskCache.save(name, content);
		} catch (IOException e) {
			e.printStackTrace();
			GLog.d(TAG, "cacheString error: " + name);
		}
	}

	public static String getContent(String name) {
		GLog.d(TAG, "getContent: " + name);
		try {
			return diskCache.getContent(name);
		} catch (Exception e) {
			e.printStackTrace();
			GLog.d(TAG, "getContent error: " + name);
			return null;
		}
	}

}
