package com.gdestiny.github.async;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.BaseAsyncTask;
import com.gdestiny.github.utils.CacheUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class AsyncImageGetter implements ImageGetter {

	private Context context;
	private TextView textview;

	public AsyncImageGetter(Context context, TextView textview) {
		this.context = context;
		this.textview = textview;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Drawable getDrawable(String source) {
		GLog.sysout("ImageGetter:" + source);

		ExtraDrawable result = new ExtraDrawable(context);

		Bitmap bm = CacheUtils.getBitmapFromMemory(source);
		if (bm == null || bm.isRecycled()) {
			new GetterTask(result).execute(source);
		} else {
			result.setDrawable(new BitmapDrawable(bm));
		}

		return result;
	}

	private class ExtraDrawable extends BitmapDrawable {
		private Drawable drawable;

		@SuppressWarnings("unused")
		public Drawable getDrawable() {
			return drawable;
		}

		public void setDrawable(Drawable drawable) {
			this.drawable = drawable;

			int width = 0;
			int height = 0;
			if (width == 0) {
				GLog.sysout("width == 0");
				width = drawable.getIntrinsicWidth();
				height = drawable.getIntrinsicHeight();
			}
			if (height == 0) {
				height = width * drawable.getIntrinsicHeight()
						/ drawable.getIntrinsicWidth();
			}
			drawable.setBounds(0, 0, width, height);
			this.setBounds(0, 0, width, height);
		}

		@SuppressWarnings("deprecation")
		public ExtraDrawable(Context context) {

			drawable = context.getResources().getDrawable(
					R.drawable.default_png);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			this.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());

		}

		@Override
		public void draw(Canvas canvas) {
			if (drawable != null) {
				drawable.draw(canvas);
			}
		}

	}

	private class GetterTask extends BaseAsyncTask<String, Void, Drawable> {

		ExtraDrawable extra;

		public GetterTask(ExtraDrawable drawable) {
			this.extra = drawable;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected Drawable doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				String url = params[0];

				Bitmap bm = null;

				if (CacheUtils.isBitmapExistInDisk(url)) {
					bm = CacheUtils.getCacheBitmap(url);
				} else {
					GLog.d(CacheUtils.TAG, "getImageFromNet " + url);
					BaseImageDownloader downloader = new BaseImageDownloader(
							context, Constants.CONNECT_TIMEOUT,
							Constants.READ_TIMEOUT);

					InputStream is = downloader.getStream(url, null);

					bm = BitmapFactory.decodeStream(is);

					CacheUtils.cacheBitmap(url, bm);
				}

				CacheUtils.cacheMemory(url, bm);

				return new BitmapDrawable(bm);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Drawable result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result == null) {
				GLog.sysout("getter null");
				extra.setDrawable(context.getResources().getDrawable(
						R.drawable.fail_png));
			} else {
				extra.setDrawable(result);
			}
			GLog.sysout("requestLayout");
			textview.setText(textview.getText());
		}
	}

}
