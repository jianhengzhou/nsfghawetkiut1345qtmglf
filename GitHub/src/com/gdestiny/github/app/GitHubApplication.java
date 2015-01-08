package com.gdestiny.github.app;

import org.eclipse.egit.github.core.client.GitHubClient;

import com.gdestiny.github.utils.SnappyDBUtils;
import com.snappydb.SnappydbException;

import android.app.Application;

public class GitHubApplication extends Application {

	private static boolean isLogin;
	private static GitHubClient client;

	@Override
	public void onCreate() {
		super.onCreate();
		initLogin();
	}

	public static GitHubClient initClient(String str1, String str2) {
		client = new GitHubClient();
		client.setCredentials(str1, str2);
		return client;
	}

	private void initLogin() {
		try {
			isLogin = SnappyDBUtils.getBoolean(getApplicationContext(),
					"isLogin");
			if (isLogin) {
				client = initClient(SnappyDBUtils.getString(getApplicationContext(),
						"account"), SnappyDBUtils.getString(
						getApplicationContext(), "password"));
			}
		} catch (SnappydbException e) {
			e.printStackTrace();
			isLogin = false;
		}
	}

	public static GitHubClient getClient() {
		return client;
	}

	public static boolean isLogin() {
		return isLogin;
	}

}
