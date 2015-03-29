package com.gdestiny.github.ui.view;

import com.gdestiny.github.utils.GLog;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * TODO
 * 
 * @author gdestiny
 * 
 */
public class ListPopupView extends LinearLayout implements OnScrollListener {

	private OnScrollListener onScrollListener;

	public static final int TOP = 0;
	public static final int BOTTOM = 1;

	public static final int TO_TOP = 2;
	public static final int TO_BOTTOM = 3;
	private int lastScrollPosition;
	private int currDirection = -1;

	private int location = BOTTOM;

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public ListPopupView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private void postTranslation() {
		int translationY = 0;
		GLog.sysout("--postTranslation--");
		if (location == BOTTOM) {
			if (currDirection == TO_TOP)
				translationY = getHeight();
		} else {
			if (currDirection == TO_TOP)
				translationY = -getHeight();
		}
		ViewPropertyAnimator.animate(ListPopupView.this).setDuration(300)
				.translationY(translationY);
		// post(new Runnable() {
		//
		// @Override
		// public void run() {
		// int translationY = 0;
		// if (currDirection == TO_TOP)
		// translationY = getHeight();
		// ViewPropertyAnimator.animate(ListPopupView.this)
		// .setDuration(300).translationY(translationY);
		// }
		// });
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (onScrollListener != null) {
			onScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (onScrollListener != null) {
			onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
		View topChild = view.getChildAt(0);

		int newScrollPosition = 0;
		if (topChild == null) {
			newScrollPosition = 0;
		} else {
			newScrollPosition = -topChild.getTop()
					+ view.getFirstVisiblePosition() * topChild.getHeight();
		}

		if (Math.abs(newScrollPosition - lastScrollPosition) >= 5
				|| (visibleItemCount == totalItemCount && visibleItemCount > 0)) {
			int direction = 0;
			if (newScrollPosition > lastScrollPosition) {
				direction = TO_TOP;
			} else if (newScrollPosition < lastScrollPosition) {
				direction = TO_BOTTOM;
			}
			if (direction != currDirection) {
				currDirection = direction;
				postTranslation();
			}
		}
		lastScrollPosition = newScrollPosition;
	}

	public void bind(ListView listview) {
		listview.setOnScrollListener(this);
	}
}
