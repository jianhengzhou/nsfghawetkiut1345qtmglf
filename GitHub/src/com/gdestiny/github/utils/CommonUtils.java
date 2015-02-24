package com.gdestiny.github.utils;

import org.eclipse.egit.github.core.User;

import android.text.TextUtils;

import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.bean.CodeTree;

public class CommonUtils {

	private CommonUtils() {
		throw new AssertionError();
	}

	public static String pathToName(String path) {
		if (TextUtils.isEmpty(path))
			return path;

		int lastSlash = path.lastIndexOf('/');
		if (lastSlash != -1 && lastSlash + 1 < path.length())
			return path.substring(lastSlash + 1);
		else
			return path;
	}

	public static String pathToParentName(String path) {
		if (TextUtils.isEmpty(path))
			return path;
		int lastSlash = path.lastIndexOf('/');
		if (lastSlash != -1 && lastSlash + 1 < path.length())
			return path.substring(0, lastSlash);
		else
			return CodeTree.ROOT;
	}

	/**
	 * 将大小化为带相应单位的String
	 * 
	 * @param size
	 * @return
	 */
	public static String sizeToSuitable(long size) {
		int count = 0;
		float result = size;
		while (result / 1024 > 1) {
			result /= 1024;
			count++;
		}
		switch (count) {
		case 0:
			return String.format("%.0f%c", result, 'B');
		case 1:
			return String.format("%.2f%c", result, 'K');
		case 2:
			return String.format("%.2f%c", result, 'M');
		case 3:
			return String.format("%.2f%c", result, 'G');
		default:
			return String.format("%.2f", result);
		}
	}

	public static boolean isAuthUser(String name) {
		return GitHubApplication.getUser().getLogin().equals(name);
	}

	public static boolean isAuthUser(User user) {
		return GitHubApplication.getUser().getLogin().equals(user.getLogin());
	}
}
