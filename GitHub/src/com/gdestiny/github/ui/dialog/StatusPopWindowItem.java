package com.gdestiny.github.ui.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * 功能描述：弹窗内部子类项（绘制标题和图标）
 */
public class StatusPopWindowItem implements Comparable<StatusPopWindowItem> {
	// 定义图片对象
	public Drawable mDrawable;
	// 定义文本对象
	public CharSequence mTitle;
	public int drawableId;
	public int titleid;
	public long count = 0;

	public StatusPopWindowItem(Context context, int titleid, int drawableId) {
		this.mTitle = context.getResources().getString(titleid);
		this.mDrawable = context.getResources().getDrawable(drawableId);
		this.drawableId = drawableId;
		this.titleid = titleid;
	}

	public StatusPopWindowItem(Context context, int titleid, Drawable drawableId) {
		this.mTitle = context.getString(titleid);
		this.mDrawable = drawableId;
		this.titleid = titleid;
	}

	public StatusPopWindowItem(Context context, int titleid) {
		this.mTitle = context.getString(titleid);
		this.mDrawable = null;
		this.titleid = titleid;
	}

	public StatusPopWindowItem(Context context, String title, int drawableId) {
		this.mTitle = title;
		this.mDrawable = context.getResources().getDrawable(drawableId);
		this.drawableId = drawableId;
	}

	@Override
	public int compareTo(StatusPopWindowItem another) {
		if (this.titleid == another.titleid || mTitle.equals(another.mTitle)) {
			return 0;
		}
		return 1;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof StatusPopWindowItem))
			return false;
		StatusPopWindowItem another = (StatusPopWindowItem) o;
		if (this.titleid == another.titleid || mTitle.equals(another.mTitle)) {
			return true;
		}
		return super.equals(o);
	}
}
