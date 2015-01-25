package com.gdestiny.github.utils;

import static android.content.Intent.ACTION_SEND;
import static android.content.Intent.EXTRA_SUBJECT;
import static android.content.Intent.EXTRA_TEXT;

import java.io.Serializable;

import com.gdestiny.github.ui.activity.TestActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

public class IntentUtils {

	private IntentUtils() {
		throw new AssertionError();
	}

	/**
	 * Create intent with subject and body
	 * 
	 * @param subject
	 * @param body
	 * @return intent
	 */
	public static Intent create(final CharSequence subject,
			final CharSequence body) {
		Intent intent = new Intent(ACTION_SEND);
		intent.setType("text/plain");
		if (!TextUtils.isEmpty(subject))
			intent.putExtra(EXTRA_SUBJECT, subject);
		intent.putExtra(EXTRA_TEXT, body);
		return intent;
	}

	/**
	 * Get body from intent
	 * 
	 * @param intent
	 * @return body
	 */
	public static String getBody(final Intent intent) {
		return intent != null ? intent.getStringExtra(EXTRA_TEXT) : null;
	}

	/**
	 * Get subject from intent
	 * 
	 * @param intent
	 * @return subject
	 */
	public static String getSubject(final Intent intent) {
		return intent != null ? intent.getStringExtra(EXTRA_SUBJECT) : null;
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
