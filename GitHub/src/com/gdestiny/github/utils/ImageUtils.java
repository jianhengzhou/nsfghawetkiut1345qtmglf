package com.gdestiny.github.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;

public class ImageUtils {

	private ImageUtils() {
		throw new AssertionError();
	}

	public static boolean isLight(int color) {
		return Math.sqrt(Color.red(color) * Color.red(color) * .241
				+ Color.green(color) * Color.green(color) * .691
				+ Color.blue(color) * Color.blue(color) * .068) > 130;
	}

	public static String getExt(String name) {
		int index = name.lastIndexOf('.');
		if (index < 0) {
			return null;
		}
		String ex = name.substring(index + 1);
		return ex;
	}

	public static boolean isPng(String name) {
		return "png".equalsIgnoreCase(getExt(name));
	}

	public static boolean isImage(String name) {
		int index = name.lastIndexOf('.');
		if (index < 0) {
			return false;
		}
		String ex = name.substring(index + 1);
		return "png".equalsIgnoreCase(ex) || "jpg".equalsIgnoreCase(ex)
				|| "gif".equalsIgnoreCase(ex) || "bmp".equalsIgnoreCase(ex);
	}

	public static boolean isImageFromPath(String path) {
		String name = CommonUtils.pathToName(path);
		int index = name.lastIndexOf('.');
		if (index < 0) {
			return false;
		}
		String ex = name.substring(index + 1);
		return "png".equalsIgnoreCase(ex) || "jpg".equalsIgnoreCase(ex)
				|| "gif".equalsIgnoreCase(ex) || "bmp".equalsIgnoreCase(ex);
	}

	public static boolean isGifFromPath(String path) {
		String name = CommonUtils.pathToName(path);
		int index = name.lastIndexOf('.');
		if (index < 0) {
			return false;
		}
		String ex = name.substring(index + 1);
		return "gif".equalsIgnoreCase(ex);
	}

	public static boolean isGifFromName(String name) {
		int index = name.lastIndexOf('.');
		if (index < 0) {
			return false;
		}
		String ex = name.substring(index + 1);
		return "gif".equalsIgnoreCase(ex);
	}

	/**
	 * 圆角化图片处理，原图bitmap不会被释放，外部调用注意处理bitmap
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = null;
		try {
			output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
					Config.ARGB_8888);
			android.graphics.Canvas canvas = new android.graphics.Canvas(output);
			final int color = 0xff424242;
			final android.graphics.Paint paint = new android.graphics.Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final android.graphics.RectF rectF = new android.graphics.RectF(
					rect);
			final float roundPx = pixels;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new android.graphics.PorterDuffXfermode(
					Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
		} catch (Exception e) {
			// Log.e("", "", e);
		}
		return output;
	}
}
