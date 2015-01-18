package com.gdestiny.github.utils;

import java.io.Serializable;

import com.gdestiny.github.ui.activity.TestActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class IntentUtils {

	private IntentUtils() {
		throw new AssertionError();
	}

	public static void startTest(Context context, String str) {
		Intent intent = new Intent(context, TestActivity.class);
		intent.putExtra("test", str);
		context.startActivity(intent);
	}

	public static void start(Context context, Intent intent) {
		context.startActivity(intent);
	}

	public static void start(Context context, Intent intent, Bundle bundle) {
		context.startActivity(intent, bundle);
	}

	public static void start(Context context, Class<?> cls) {
		Intent intent = new Intent(context, cls);
		context.startActivity(intent);
	}

	public static void start(Context context, Class<?> cls, String name,
			Serializable data) {
		Intent intent = new Intent(context, cls);
		intent.putExtra(name, data);
		context.startActivity(intent);
	}
}
