package com.gdestiny.github.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.view.Window;

import com.gdestiny.github.R;

public class AndroidUtils {

	private AndroidUtils() {
		throw new AssertionError();
	}

	@SuppressWarnings({ "rawtypes" })
	public static void initMiBar(Activity activity) {
		Window window = activity.getWindow();
		window.setBackgroundDrawableResource(R.color.common_icon_blue);
		Class clazz = window.getClass();
		try {
			int tranceFlag = 0;
			int darkModeFlag = 0;
			Class layoutParams = Class
					.forName("android.view.MiuiWindowManager$LayoutParams");

			Field field = layoutParams
					.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
			tranceFlag = field.getInt(layoutParams);

			field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
			darkModeFlag = field.getInt(layoutParams);

			@SuppressWarnings("unchecked")
			Method extraFlagField = clazz.getMethod("setExtraFlags", int.class,
					int.class);
			// 只需要状态栏透明
			extraFlagField.invoke(window, tranceFlag, tranceFlag);
			// 状态栏透明且黑色字体
			// extraFlagField.invoke(window, tranceFlag | darkModeFlag,
			// tranceFlag
			// | darkModeFlag);
			// 清除黑色字体
			extraFlagField.invoke(window, 0, darkModeFlag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static float dpToPx(Context context, float dp) {
		if (context == null) {
			return -1;
		}
		return dp * context.getResources().getDisplayMetrics().density;
	}

	public static float pxToDp(Context context, float px) {
		if (context == null) {
			return -1;
		}
		return px / context.getResources().getDisplayMetrics().density;
	}

	public static float dpToPxInt(Context context, float dp) {
		return (int) (dpToPx(context, dp) + 0.5f);
	}

	public static float pxToDpCeilInt(Context context, float px) {
		return (int) (pxToDp(context, px) + 0.5f);
	}
}
