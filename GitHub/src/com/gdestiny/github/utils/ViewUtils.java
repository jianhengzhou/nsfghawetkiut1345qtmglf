package com.gdestiny.github.utils;

import android.view.View;
import android.view.animation.AnimationUtils;

public class ViewUtils {

	private ViewUtils() {
		throw new AssertionError();
	}

	public static <V extends View> void setVisibility(V view, int visibility) {
		if (view != null && view.getVisibility() != visibility) {
			view.setVisibility(visibility);
		}
	}

	public static <V extends View> void setVisibility(V view, int visibility,
			int animId) {
		if (view != null && view.getVisibility() != visibility) {
			view.setAnimation(AnimationUtils.loadAnimation(view.getContext(),
					animId));
			view.setVisibility(visibility);
		}
	}
}
