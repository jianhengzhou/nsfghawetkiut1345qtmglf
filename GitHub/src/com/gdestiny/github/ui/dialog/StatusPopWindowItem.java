package com.gdestiny.github.ui.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * ���������������ڲ���������Ʊ����ͼ�꣩
 */
public class StatusPopWindowItem implements Comparable<StatusPopWindowItem> {
	// ����ͼƬ����
	public Drawable mDrawable;
	// �����ı�����
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
