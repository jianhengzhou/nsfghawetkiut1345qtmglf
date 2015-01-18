package com.gdestiny.github.utils;

import android.view.View;

public class ViewUtils {

	private ViewUtils() {
		throw new AssertionError();
	}

	public static <V extends View> void setVisibility(V view, int visibility) {
		if (view.getVisibility() != visibility) {
			view.setVisibility(visibility);
		}
	}
}
