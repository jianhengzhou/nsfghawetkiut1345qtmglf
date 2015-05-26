package com.gdestiny.github.app;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;

import android.app.Application;
import android.content.Context;

import com.gdestiny.github.utils.CacheUtils;
import com.gdestiny.github.utils.GLog;

public class GitHubApplication extends Application {

	private static Context context;
	private static boolean isLogin;
	private static DefaultClient client;
	private static User user;

	@Override
	public void onCreate() {
		super.onCreate();
		GLog.sysout("--------GitHub------------GitHub-------------GitHub--------");
		context = getApplicationContext();
		// initLogin();
		CacheUtils.init(context);
	}

	public static Context getContext() {
		return context;
	}

	public static DefaultClient initClient(String str1, String str2) {
		client = new DefaultClient();
		client.setCredentials(str1, str2);
		return client;
	}

	public static DefaultClient initClient(String token) {
		client = new DefaultClient();
		client.setOAuth2Token(token);
		return client;
	}

	// private void initLogin() {
	// try {
	// isLogin = PreferencesUtils.getBoolean(context,
	// LoginActivity.IS_LOGIN);
	// if (isLogin) {
	// client = initClient(
	// PreferencesUtils.getString(context,
	// Base64Util.encodeString(LoginActivity.ACCOUNT)),
	// PreferencesUtils.getString(context,
	// Base64Util.encodeString(LoginActivity.PASSWORD)));
	// user = SnappyDBUtils.getSerializable(context, "user",
	// User.class);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// isLogin = false;
	// }
	// }

	public static GitHubClient getClient() {
		return client;
	}

	public static void setClient(DefaultClient client) {
		GitHubApplication.client = client;
	}

	public static User getUser() {
		return user;
	}

	public static void setUser(User user) {
		GitHubApplication.user = user;
	}

	public static boolean isLogin() {
		return isLogin;
	}

	public static void setLogin(boolean isLogin) {
		GitHubApplication.isLogin = isLogin;
	}

}
