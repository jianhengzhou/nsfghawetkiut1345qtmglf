package com.gdestiny.github.utils;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Browser;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.gdestiny.github.R;

public class AndroidUtils {

	private static boolean isMiuiV6;
	private static String version;
	public static final Pattern HTTP_UTL = Pattern
			.compile("((?:(http|https|Http|Https|rtsp|Rtsp):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?((?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}\\.)+(?:(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])|(?:biz|b[abdefghijmnorstvwyz])|(?:cat|com|coop|c[acdfghiklmnoruvxyz])|d[ejkmoz]|(?:edu|e[cegrstu])|f[ijkmor]|(?:gov|g[abdefghilmnpqrstuwy])|h[kmnrtu]|(?:info|int|i[delmnoqrst])|(?:jobs|j[emop])|k[eghimnprwyz]|l[abcikrstuvy]|(?:mil|mobi|museum|m[acdeghklmnopqrstuvwxyz])|(?:name|net|n[acefgilopruz])|(?:org|om)|(?:pro|p[aefghklmnrstwy])|qa|r[eosuw]|s[abcdeghijklmnortuvyz]|(?:tel|travel|t[cdfghjklmnoprtvwz])|u[agksyz]|v[aceginu]|w[fs]|(?:xn\\-\\-0zwm56d|xn\\-\\-11b5bs3a9aj6g|xn\\-\\-80akhbyknj4f|xn\\-\\-9t4b11yi5a|xn\\-\\-deba0ad|xn\\-\\-g6w251d|xn\\-\\-hgbk6aj7f53bba|xn\\-\\-hlcj6aya9esc7a|xn\\-\\-jxalpdlp|xn\\-\\-kgbechtv|xn\\-\\-zckzah)|y[etu]|z[amw]))|(?:(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])))(?:\\:\\d{1,5})?)(\\/(?:(?:[a-zA-Z0-9 -퟿豈-﷏ﷰ-￯\\;\\/\\?\\:\\@\\&\\=\\#\\~\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?");

	public static final Pattern EMAIL = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

	static {
		try {
			Class<?> sysClass = Class.forName("android.os.SystemProperties");
			Method getStringMethod = sysClass.getDeclaredMethod("get",
					String.class);
			isMiuiV6 = "V6".equals((String) getStringMethod.invoke(sysClass,
					"ro.miui.ui.version.name"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private AndroidUtils() {
		throw new AssertionError();
	}

	public static class Keyboard {

		// 显示或者隐藏输入键盘
		public static void hideKeyboard(Activity context,
				final Runnable afterHide) {

			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			View view = context.getCurrentFocus();
			if (view != null) {
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0,
						new ResultReceiver(new Handler()) {

							protected void onReceiveResult(int resultCode,
									Bundle resultData) {
								afterHide.run();
							}

						});
			}
		}

		// 隐藏软键盘
		public static void hideKeyboard(Activity context) {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			View view = context.getCurrentFocus();
			if (view != null && imm.isActive()) {
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);// 隐藏软键盘

			}
		}

		// 键盘是否已经显示出来
		public static boolean isKeybordShown(Activity context, View focusView) {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			return imm.isActive(focusView);

		}

		// 这个方法是可用的
		public static void showKeyboard(Activity context, View focusView) {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm.showSoftInput(focusView, 0);

		}

		/**
		 * 默认不显示输入面板
		 * 
		 * @param context
		 */
		public static void noExplicityInputMethod(Activity context) {
			context.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		}

	}

	@SuppressWarnings({ "rawtypes" })
	public static void initMiBar(Activity activity) {
		if (!isMiuiV6)
			return;
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

	public static int dpToPxInt(Context context, float dp) {
		return (int) (dpToPx(context, dp) + 0.5f);
	}

	public static int pxToDpCeilInt(Context context, float px) {
		return (int) (pxToDp(context, px) + 0.5f);
	}

	public static String getVersion(Context context) {
		if (version == null) {
			try {
				PackageManager manager = context.getPackageManager();
				PackageInfo info = manager.getPackageInfo(
						context.getPackageName(), 0);
				version = info.versionName;
			} catch (Exception e) {
				e.printStackTrace();
				return "1.0";
			}
		}
		return version;
	}

	public static boolean isHttpUrl(String source) {
		return source.matches(HTTP_UTL.toString());
	}

	public static boolean isEmail(String source) {
		source = source.toLowerCase();
		return EMAIL.matcher(source).matches();
	}

	public static void openUrlWith(Context context, String url) {
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
		context.startActivity(intent);
	}

	public static void openEmail(Context context, String email) {
		Intent data = new Intent(Intent.ACTION_SENDTO);
		data.setData(Uri.parse(email));
		context.startActivity(data);
	}

	@SuppressWarnings("deprecation")
	public static void toClipboard(Context context, String str) {
		if (!TextUtils.isEmpty(str)) {
			ClipboardManager cmb = (ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			cmb.setText(str);
			android.os.Vibrator vibrator = (android.os.Vibrator) context
					.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(100);
			ToastUtils.show(context, R.string.to_clipboard);
		}
	}

	public static void vibrate(Context context, long milliseconds) {
		android.os.Vibrator vibrator = (android.os.Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(milliseconds);
	}

	/**
	 * Finish the given activity and start a home activity class.
	 * <p>
	 * This mirror the behavior of the home action bar button that clears the
	 * current activity and starts or brings another activity to the top.
	 * 
	 * @param activity
	 * @param homeActivityClass
	 */
	public static void goHome(Activity activity, Class<?> homeActivityClass) {
		activity.finish();
		Intent intent = new Intent(activity, homeActivityClass);
		intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP);
		activity.startActivity(intent);
	}

	public static void share(Context context, String title, String content) {
		Intent intent = new Intent(Intent.ACTION_SEND);

		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "share");
		intent.putExtra(Intent.EXTRA_TEXT, content);
		context.startActivity(Intent.createChooser(intent, title));
	}
}
