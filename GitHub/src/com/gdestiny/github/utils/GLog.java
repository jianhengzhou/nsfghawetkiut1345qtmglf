package com.gdestiny.github.utils;

/**
 * 
 * @author gdestiny
 *
 */
public class GLog {
	private static boolean isDebug = true;

	private GLog() {
		throw new AssertionError();
	}

	public static void sysout(String msg) {
		if (isDebug) {
			System.out.println(msg);
		}
	}

	public static void setDebug(boolean debug) {
		isDebug = debug;
	}

	public static int v(String tag, String msg) {
		if (isDebug)
			return android.util.Log.v(tag, msg);
		return 0;
	}

	public static int d(String tag, String msg) {
		if (isDebug)
			return android.util.Log.d(tag, msg);
		return 0;
	}

	public static int i(String tag, String msg) {
		if (isDebug)
			return android.util.Log.i(tag, msg);
		return 0;
	}

	public static int w(String tag, String msg) {
		if (isDebug)
			return android.util.Log.w(tag, msg);
		return 0;
	}

	public static int e(String tag, String msg) {
		if (isDebug)
			return android.util.Log.e(tag, msg);
		return 0;
	}

	public static int e(String tag, String msg, Throwable tr) {
		if (isDebug)
			return android.util.Log.e(tag, msg, tr);
		return 0;
	}
}
