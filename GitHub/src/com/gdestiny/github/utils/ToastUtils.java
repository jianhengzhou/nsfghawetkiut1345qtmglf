package com.gdestiny.github.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtils {

	private static Handler handler = new Handler(Looper.getMainLooper());

	private ToastUtils() {
		throw new AssertionError();
	}

	public static void showAsync(final Context context, final int resId) {
		showAsync(context, context.getResources().getText(resId),
				Toast.LENGTH_SHORT);
	}

	public static void showAsync(final Context context, final CharSequence text) {
		showAsync(context, text, Toast.LENGTH_SHORT);
	}

	public static void showAsync(final Context context, final int resId,
			final int duration) {
		showAsync(context, context.getResources().getText(resId), duration);
	}

	public static void showAsync(final Context context,
			final CharSequence text, final int duration) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				synchronized (this) {
					try {
						show(context, text, duration);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		});
	}

	public static void show(Context context, int resId) {
		show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
	}

	public static void show(Context context, int resId, int duration) {
		show(context, context.getResources().getText(resId), duration);
	}

	public static void show(Context context, CharSequence text) {
		show(context, text, Toast.LENGTH_SHORT);
	}

	public static void show(Context context, CharSequence text, int duration) {
		Toast.makeText(context, text, duration).show();
	}

	public static void show(Context context, int resId, Object... args) {
		show(context,
				String.format(context.getResources().getString(resId), args),
				Toast.LENGTH_SHORT);
	}

	public static void show(Context context, String format, Object... args) {
		show(context, String.format(format, args), Toast.LENGTH_SHORT);
	}

	public static void show(Context context, int resId, int duration,
			Object... args) {
		show(context,
				String.format(context.getResources().getString(resId), args),
				duration);
	}

	public static void show(Context context, String format, int duration,
			Object... args) {
		show(context, String.format(format, args), duration);
	}
}
