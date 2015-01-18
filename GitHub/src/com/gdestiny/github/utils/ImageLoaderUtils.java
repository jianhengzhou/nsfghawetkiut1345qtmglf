package com.gdestiny.github.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;

import com.gdestiny.github.R;
import com.gdestiny.github.app.GitHubApplication;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

public class ImageLoaderUtils {

	// private static long startTime = 0L;
	/** 暂时显示时间控制在一个时间段内，则认定是从内存中读取的，则取消显示图片加载动画 */
	public static final long TIME_DIFF = 100L;

	private static HashMap<Integer, SoftReference<DisplayImageOptions>> mDisplayImageOptions;

	public static DisplayImageOptions getImageOptions(int defaultRid) {

		return getImageOptions(defaultRid, defaultRid);
	}

	public static DisplayImageOptions getImageOptions(int defaultStartRid,
			int defaultFailedRid) {

		if (mDisplayImageOptions == null) {
			mDisplayImageOptions = new HashMap<Integer, SoftReference<DisplayImageOptions>>();
		}
		Integer keyInteger = Integer.valueOf(defaultStartRid);
		SoftReference<DisplayImageOptions> softReference;
		softReference = mDisplayImageOptions.get(keyInteger);
		DisplayImageOptions options = null;
		if (softReference != null) {
			options = softReference.get();
		}

		if (options == null) {
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(defaultStartRid)
					.showImageForEmptyUri(defaultFailedRid)
					// resource or drawable
					.showImageOnFail(defaultFailedRid)
					// resource or drawable
					.cacheInMemory(true).cacheOnDisk(true)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
			softReference = new SoftReference<DisplayImageOptions>(options);
			mDisplayImageOptions.put(keyInteger, softReference);
		}
		return options;
	}

	public static DisplayImageOptions getImageOptionsNoStub(
			int defaultStartRid, int defaultFailedRid) {

		if (mDisplayImageOptions == null) {
			mDisplayImageOptions = new HashMap<Integer, SoftReference<DisplayImageOptions>>();
		}
		Integer keyInteger = Integer
				.valueOf(defaultStartRid + defaultFailedRid);
		SoftReference<DisplayImageOptions> softReference;
		softReference = mDisplayImageOptions.get(keyInteger);
		DisplayImageOptions options = null;
		if (softReference != null) {
			options = softReference.get();
		}

		if (options == null) {
			options = new DisplayImageOptions.Builder().cacheInMemory(true)
					.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
					.build();
			softReference = new SoftReference<DisplayImageOptions>(options);
			mDisplayImageOptions.put(keyInteger, softReference);
		}
		return options;
	}

	/**
	 * 带渐显效果的图片加载
	 * 
	 * @param uri
	 * @param imageView
	 * @param defaultRid
	 */
	public static void displayImage(String uri, ImageView imageView,
			final int defaultRid) {
		displayImage(uri, imageView, defaultRid, true);
	}

	public static void displayImage(String uri, ImageView imageView,
			int defaultStartRid, int defaultFailedRid) {
		displayImage(uri, imageView, defaultStartRid, defaultFailedRid, true);
	}

	public static void displayMiddleImage(final String thumbnail_pic,
			String uri, ImageView imageView, final int defaultStartRid,
			final int defaultFailedRid, final boolean isTran) {
		ImageLoader.getInstance().displayImage(uri, imageView,
				getImageOptionsNoStub(defaultStartRid, defaultFailedRid),
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// startTime = System.currentTimeMillis();
						view.setTag(R.id.tag_imageloader,
								System.currentTimeMillis());

						@SuppressWarnings("deprecation")
						File tmpFile = ImageLoader.getInstance().getDiscCache()
								.get(thumbnail_pic);
						if (tmpFile != null && tmpFile.exists()) {
							((ImageView) view).setImageURI(Uri
									.fromFile(tmpFile));
						} else {
							((ImageView) view)
									.setImageResource(defaultStartRid);
						}

					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						((ImageView) view).setImageResource(defaultFailedRid);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						long endTime = System.currentTimeMillis();
						long startTime = 0;
						Object obj = view.getTag(R.id.tag_imageloader);
						if (obj != null) {
							startTime = (Long) obj;
						}
						long diff = endTime - startTime;

						if (loadedImage == null) {
							((ImageView) view)
									.setImageResource(defaultFailedRid);
						} else {
							if (isTran && diff > TIME_DIFF) {
								@SuppressWarnings("deprecation")
								final TransitionDrawable td = new TransitionDrawable(
										new Drawable[] {
												new ColorDrawable(
														android.R.color.transparent),
												new BitmapDrawable(loadedImage) });
								td.setCrossFadeEnabled(true);
								view.setLayoutParams(new LayoutParams(
										loadedImage.getWidth(), loadedImage
												.getHeight()));
								((ImageView) view).setImageDrawable(td);
								td.startTransition(300);
							} else {
								((ImageView) view).setImageBitmap(loadedImage);
							}
						}
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub

					}
				});
	}

	public static void displayImage(String uri, ImageView imageView,
			final int defaultStartRid, final int defaultFailedRid,
			final boolean isTran) {
		ImageLoader.getInstance().displayImage(uri, imageView,
				getImageOptions(defaultStartRid, defaultFailedRid),
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// startTime = System.currentTimeMillis();
						view.setTag(R.id.tag_imageloader,
								System.currentTimeMillis());
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {

					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						long endTime = System.currentTimeMillis();
						long startTime = 0;
						Object obj = view.getTag(R.id.tag_imageloader);
						if (obj != null) {
							startTime = (Long) obj;
						}
						long diff = endTime - startTime;

						if (loadedImage == null) {
							((ImageView) view)
									.setImageResource(defaultFailedRid);
						} else {
							if (isTran && diff > TIME_DIFF) {
								@SuppressWarnings("deprecation")
								final TransitionDrawable td = new TransitionDrawable(
										new Drawable[] {
												new ColorDrawable(
														android.R.color.transparent),
												new BitmapDrawable(loadedImage) });
								td.setCrossFadeEnabled(true);
								((ImageView) view).setImageDrawable(td);
								td.startTransition(300);
							} else {
								((ImageView) view).setImageBitmap(loadedImage);
							}
						}
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub

					}
				});
	}

	/**
	 * 图片加载
	 * 
	 * @param uri
	 * @param imageView
	 * @param defaultRid
	 * @param isTran
	 *            是否渐显效果
	 */
	public static void displayImage(String uri, ImageView imageView,
			final int defaultRid, final boolean isTran) {
		// DisplayImageOptions options = new DisplayImageOptions.Builder()
		// .showStubImage(defaultRid)
		// .showImageForEmptyUri(defaultRid) // resource or drawable
		// .showImageOnFail(defaultRid) // resource or drawable
		// .cacheInMemory()
		// .cacheOnDisc()
		// .build();
		ImageLoader.getInstance().displayImage(uri, imageView,
				getImageOptions(defaultRid), new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub
						// startTime = System.currentTimeMillis();

						view.setTag(R.id.tag_imageloader,
								System.currentTimeMillis());
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						long endTime = System.currentTimeMillis();
						long startTime = 0;
						Object obj = view.getTag(R.id.tag_imageloader);
						if (obj != null) {
							startTime = (Long) obj;
						}
						long diff = endTime - startTime;

						if (loadedImage == null) {
							((ImageView) view).setImageResource(defaultRid);
						} else {
							if (isTran && diff > TIME_DIFF) {
								@SuppressWarnings("deprecation")
								final TransitionDrawable td = new TransitionDrawable(
										new Drawable[] {
												new ColorDrawable(
														android.R.color.transparent),
												new BitmapDrawable(loadedImage) });
								td.setCrossFadeEnabled(true);
								((ImageView) view).setImageDrawable(td);
								td.startTransition(300);
							} else {
								((ImageView) view).setImageBitmap(loadedImage);
							}
						}
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub

					}
				});
	}

	public static void displayImage(String uri, ImageView imageView,
			int defaultStartRid, int defaultFailedRid,
			ImageLoadingListener listener) {
		ImageLoader.getInstance().displayImage(uri, imageView,
				getImageOptions(defaultStartRid, defaultFailedRid), listener);
	}

	public static void displayImage(String uri, ImageView imageView,
			int defaultStartRid, ImageLoadingListener listener) {
		ImageLoader.getInstance().displayImage(uri, imageView,
				getImageOptions(defaultStartRid), listener);
	}

	public static void displayImage(String uri, ImageView imageView,
			DisplayImageOptions options) {
		ImageLoader.getInstance().displayImage(uri, imageView, options,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub
						// startTime = System.currentTimeMillis();
						view.setTag(R.id.tag_imageloader,
								System.currentTimeMillis());
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						GLog.e("ImageLoaderUtils.displayImage",
								"onLoadingFailed:" + imageUri);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						long endTime = System.currentTimeMillis();
						long startTime = 0;
						Object obj = view.getTag(R.id.tag_imageloader);
						if (obj != null) {
							startTime = (Long) obj;
						}
						long diff = endTime - startTime;
						if (loadedImage != null) {
							if (diff > TIME_DIFF) {
								@SuppressWarnings("deprecation")
								final TransitionDrawable td = new TransitionDrawable(
										new Drawable[] {
												new ColorDrawable(
														android.R.color.transparent),
												new BitmapDrawable(loadedImage) });
								td.setCrossFadeEnabled(true);
								((ImageView) view).setImageDrawable(td);
								td.startTransition(300);
							} else {
								((ImageView) view).setImageBitmap(loadedImage);
							}
						}
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub

					}
				});
	}

	public static void displayImage(String uri, ImageView imageView,
			DisplayImageOptions options, final ScaleType beforeScaleType,
			final ScaleType afterScaleType) {
		ImageLoader.getInstance().displayImage(uri, imageView, options,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub
						// startTime = System.currentTimeMillis();
						view.setTag(R.id.tag_imageloader,
								System.currentTimeMillis());
						((ImageView) view).setScaleType(beforeScaleType);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						long endTime = System.currentTimeMillis();
						long startTime = 0;
						Object obj = view.getTag(R.id.tag_imageloader);
						if (obj != null) {
							startTime = (Long) obj;
						}
						long diff = endTime - startTime;

						if (loadedImage != null) {
							((ImageView) view).setScaleType(afterScaleType);
							if (diff > TIME_DIFF) {
								@SuppressWarnings("deprecation")
								final TransitionDrawable td = new TransitionDrawable(
										new Drawable[] {
												new ColorDrawable(
														android.R.color.transparent),
												new BitmapDrawable(loadedImage) });
								td.setCrossFadeEnabled(true);
								((ImageView) view).setImageDrawable(td);
								td.startTransition(300);
							} else {
								((ImageView) view).setImageBitmap(loadedImage);
							}
						}
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub

					}
				});
	}

	public static void displayImage(String uri, ImageView imageView,
			final int defaultRid, final ScaleType beforeScaleType,
			final ScaleType afterScaleType) {
		displayImage(uri, imageView, getImageOptions(defaultRid),
				beforeScaleType, afterScaleType);

	}

	/**
	 * 
	 * @param uri
	 * @param imageView
	 * @param defaultRid
	 * @param isTran
	 * @param radix
	 *            修剪的圆角
	 */

	public static void displayImage(String uri, ImageView imageView,
			final int defaultRid, final boolean isTran, final int radix) {
		final String key = uri + "_roundcorner_" + radix;
		Bitmap bitmap = ImageLoader.getInstance().getMemoryCache().get(key);
		if (bitmap != null) {
			if (isTran) {
				@SuppressWarnings("deprecation")
				final TransitionDrawable td = new TransitionDrawable(
						new Drawable[] {
								new ColorDrawable(android.R.color.transparent),
								new BitmapDrawable(bitmap) });
				td.setCrossFadeEnabled(true);
				imageView.setImageDrawable(td);
				td.startTransition(300);
			} else {
				imageView.setImageBitmap(bitmap);
			}
		} else {
			@SuppressWarnings("deprecation")
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					// .showStubImage(defaultRid)//此句注掉是去掉设置切圆角的点位图后又被
					// 设置defaultRid
					.showImageForEmptyUri(defaultRid)
					.showImageOnFail(defaultRid).cacheInMemory().cacheOnDisc()
					.build();
			ImageLoader.getInstance().displayImage(uri, imageView, options,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// TODO Auto-generated method stub
							String nullKey = defaultRid + "_roundcorner";
							Bitmap loadedImage = ImageLoader.getInstance()
									.getMemoryCache().get(nullKey);
							if (loadedImage == null) {
								loadedImage = BitmapFactory.decodeResource(
										GitHubApplication.getContext()
												.getResources(), defaultRid);
								loadedImage = toRoundCorner(loadedImage, radix);
							}
							((ImageView) view).setImageBitmap(loadedImage);
							ImageLoader.getInstance().getMemoryCache()
									.put(nullKey, loadedImage);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							String nullKey = defaultRid + "_roundcorner";
							Bitmap loadedImage = ImageLoader.getInstance()
									.getMemoryCache().get(nullKey);
							if (loadedImage == null) {
								loadedImage = BitmapFactory.decodeResource(
										GitHubApplication.getContext()
												.getResources(), defaultRid);
								loadedImage = toRoundCorner(loadedImage, radix);
							}
							((ImageView) view).setImageBitmap(loadedImage);
							ImageLoader.getInstance().getMemoryCache()
									.put(nullKey, loadedImage);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							if (loadedImage == null) {
								loadedImage = BitmapFactory.decodeResource(
										GitHubApplication.getContext()
												.getResources(), defaultRid);
							}
							loadedImage = toRoundCorner(loadedImage, radix);
							if (isTran) {
								@SuppressWarnings("deprecation")
								final TransitionDrawable td = new TransitionDrawable(
										new Drawable[] {
												new ColorDrawable(
														android.R.color.transparent),
												new BitmapDrawable(loadedImage) });
								td.setCrossFadeEnabled(true);
								((ImageView) view).setImageDrawable(td);
								td.startTransition(300);
							} else {
								((ImageView) view).setImageBitmap(loadedImage);
							}
							ImageLoader.getInstance().getMemoryCache()
									.put(key, loadedImage);
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							// TODO Auto-generated method stub

						}
					});
		}
	}

	public static void displayImage(String uri, ImageView imageView,
			DisplayImageOptions options, ImageLoadingListener listener) {
		ImageLoader.getInstance().displayImage(uri, imageView, options,
				listener);
	}

	public static Bitmap findBitmapByUri(String uri) {
		List<Bitmap> datas = MemoryCacheUtils.findCachedBitmapsForImageUri(uri,
				ImageLoader.getInstance().getMemoryCache());
		return datas != null && !datas.isEmpty() ? datas.get(0) : null;
	}

	public static void initImageLoader(Context context) {
		int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);

		MemoryCache memoryCache;
		if (Build.VERSION.SDK_INT >= 9) {
			memoryCache = new LruMemoryCache(memoryCacheSize);
		} else {
			memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
		}

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCache(memoryCache)
				.diskCache(ImageLoaderUtils.initImageLoaderDiskCache())
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(diskCacheFileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				// .imageDownloader(
				// new com.kdweibo.android.image.KdImageDownloader(context))
				.build();
		ImageLoader.getInstance().init(config);
		L.writeDebugLogs(Constants.isDebug);
		L.writeLogs(Constants.isDebug);
	}

	public static FileNameGenerator diskCacheFileNameGenerator() {
		return new Md5FileNameGenerator();
	}

	public static ImageLoader getImageLoader() {
		return ImageLoader.getInstance();
	}

	public static void saveFileInDiskCache(String imageUri, File file) {
		try {
			getImageLoader().getDiskCache().save(imageUri,
					new FileInputStream(file), null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String initImageDir() {
		return Constants.IMAGE_PATH;
	}

	private static DiskCache initImageLoaderDiskCache() {
		File imgDir = new File(initImageDir());// make sure the dir exists
		if (!imgDir.exists()) {
			imgDir.mkdirs();
			GLog.sysout("imgDir.mkdirs() : " +initImageDir() );
		}
		int initSize;
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			initSize = 1024 * 1024 * 300;// SDcard上
		} else {
			initSize = 1024 * 1024 * 10;// 内存
		}
		try {
			return new LruDiskCache(imgDir, diskCacheFileNameGenerator(),
					initSize);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 更新缓存中imageUri对应的bitmap
	 * 
	 * @param imageUri
	 * @param bmp
	 *            新的bitmap
	 */
	public static void putBitmapInMemoryCache(String imageUri, Bitmap bmp) {
		List<String> keys = MemoryCacheUtils.findCacheKeysForImageUri(imageUri,
				ImageLoader.getInstance().getMemoryCache());
		if (keys != null && !keys.isEmpty()) {
			ImageLoader.getInstance().getMemoryCache().put(keys.get(0), bmp);
		}
	}

	/**
	 * 获取bitmap在memoryCache对应的key值
	 * 
	 * @param imageUri
	 * @return
	 */
	public static String getBitmpaKey(String imageUri) {
		List<String> keys = MemoryCacheUtils.findCacheKeysForImageUri(imageUri,
				ImageLoader.getInstance().getMemoryCache());
		if (keys != null && !keys.isEmpty()) {
			return keys.get(0);
		}
		return null;
	}

	public static DiskCache getCurrenyDiskCache() {
		return ImageLoader.getInstance().getDiskCache();
	}

	public static boolean isExistedInDiskCache(String keyUrl) {
		File file = getFileInDiskCache(keyUrl);
		if (file == null) {
			return false;
		}

		return file.exists();
	}

	public static File getFileInDiskCache(String keyUrl) {
		return DiskCacheUtils.findInCache(keyUrl, getCurrenyDiskCache());
	}

	/**
	 * 异步加载drawable目录下的图片
	 * 
	 * @param drawableId
	 * @param imageView
	 * @param defaultRid
	 * @param isTran
	 */
	public static void displayDrawableImage(int drawableId,
			ImageView imageView, int defaultRid, final boolean isTran) {
		displayImage(Scheme.DRAWABLE.wrap(String.valueOf(drawableId)),
				imageView, defaultRid, isTran);
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
