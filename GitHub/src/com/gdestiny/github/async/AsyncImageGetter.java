package com.gdestiny.github.async;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;

import com.gdestiny.github.R;

public class AsyncImageGetter implements ImageGetter {

	private Context context;

	public AsyncImageGetter(Context context) {
		this.context = context;
	}

	@Override
	public Drawable getDrawable(String source) {
		// TODO Auto-generated method stub
		System.out.println(source);
		Drawable drawable = context.getResources().getDrawable(
				R.drawable.test);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		return drawable;
	}

}
