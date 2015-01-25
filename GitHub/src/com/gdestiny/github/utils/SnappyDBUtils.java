package com.gdestiny.github.utils;

import java.io.Serializable;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappyDB;
import com.snappydb.SnappydbException;

import android.content.Context;

public class SnappyDBUtils {

	private SnappyDBUtils() {
		throw new AssertionError();
	}

	public static void putString(Context context, String key, String value)
			throws SnappydbException {
		DB snappyDB = DBFactory.open(context, CacheUtils.SnappyDB_Internal);
		snappyDB.put(key, value);
		snappyDB.close();
	}

	public static void putString(Context context, String dbName, String key,
			String value) throws SnappydbException {
		DB snappyDB = new SnappyDB.Builder(context)
				.directory(CacheUtils.SnappyDB_External).name(dbName).build();
		snappyDB.put(key, value);
		snappyDB.close();
	}

	public static void putString(DB snappyDB, String key, String value)
			throws SnappydbException {
		if (snappyDB == null) {
			throw new SnappydbException("snappyDB == null");
		}
		snappyDB.put(key, value);
		snappyDB.close();
	}

	public static String getString(Context context, String key)
			throws SnappydbException {
		DB snappyDB = DBFactory.open(context, CacheUtils.SnappyDB_Internal);
		String temp = snappyDB.get(key);
		snappyDB.close();
		return temp;
	}

	public static String getString(Context context, String dbName, String key)
			throws SnappydbException {
		DB snappyDB = new SnappyDB.Builder(context)
				.directory(CacheUtils.SnappyDB_External) // optional
				.name(dbName)// optional
				.build();
		String temp = snappyDB.get(key);
		snappyDB.close();
		return temp;
	}

	public static void putInt(Context context, String key, int value)
			throws SnappydbException {
		DB snappyDB = DBFactory.open(context, CacheUtils.SnappyDB_Internal);
		snappyDB.putInt(key, value);
		snappyDB.close();
	}

	public static int getInt(Context context, String key)
			throws SnappydbException {
		DB snappyDB = DBFactory.open(context, CacheUtils.SnappyDB_Internal);
		int temp = snappyDB.getInt(key);
		snappyDB.close();
		return temp;
	}

	public static void putBoolean(Context context, String key, boolean value)
			throws SnappydbException {
		DB snappyDB = DBFactory.open(context, CacheUtils.SnappyDB_Internal);
		snappyDB.putBoolean(key, value);
		snappyDB.close();
	}

	public static boolean getBoolean(Context context, String key)
			throws SnappydbException {
		DB snappyDB = DBFactory.open(context, CacheUtils.SnappyDB_Internal);
		boolean temp = snappyDB.getBoolean(key);
		snappyDB.close();
		return temp;
	}

	public static void putSerializable(Context context, String key, Object value)
			throws SnappydbException {
		DB snappyDB = DBFactory.open(context, CacheUtils.SnappyDB_Internal);
		snappyDB.put(key, value);
		snappyDB.close();
	}

	public static <T extends Serializable> T getSerializable(Context context,
			String key, Class<T> type) throws SnappydbException {
		DB snappyDB = DBFactory.open(context, CacheUtils.SnappyDB_Internal);
		T temp = snappyDB.getObject(key, type);
		snappyDB.close();
		return temp;
	}
}
