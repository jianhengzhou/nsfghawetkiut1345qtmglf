/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gdestiny.github.ui.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * A custom ScrollView that can accept a scroll listener.
 */
public class ObservableScrollView extends ScrollView {
	private boolean mDisableEdgeEffects = true;

	// pull
	private boolean canDrag = false;
	private final int mTouchSlop;

	private float mInitialMotionY;
	private float mInitialMotionX;

	public boolean isCanDrag() {
		return canDrag;
	}

	public void setCanDrag(boolean canDrag) {
		this.canDrag = canDrag;
	}

	public interface OnScrollChangedListener {
		void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt);

		void onDragDown(float percent);
	}

	private OnScrollChangedListener mOnScrollChangedListener;

	public ObservableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFadingEdgeLength(0);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (mOnScrollChangedListener != null) {
			mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	} // Return false if we're scrolling in the x direction

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!canDrag) {
			return super.onTouchEvent(event);
		}
		final float x = event.getX(), y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (getScrollY() <= 0) {
				mInitialMotionX = x;
				mInitialMotionY = y;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (mInitialMotionY > 0f) {
				final float yDiff = y - mInitialMotionY;
				final float xDiff = x - mInitialMotionX;
				//
				// if (Math.abs(yDiff) > Math.abs(xDiff) && yDiff > mTouchSlop)
				// {
				// mIsBeingDragged = true;
				// // onPullStarted(y);
				// } else if (yDiff < -mTouchSlop) {
				// resetTouch();
				// }
				// }
				if (getScrollY() <= 0) {
					if (Math.abs(yDiff) > Math.abs(xDiff) && yDiff > mTouchSlop) {
						if (mOnScrollChangedListener != null) {
							mOnScrollChangedListener.onDragDown(yDiff
									/ getHeight() * 2.5f);
							return true;
						} else if (yDiff < -mTouchSlop) {
							resetTouch();
						}
					}
				} else {
					mOnScrollChangedListener.onDragDown(0f);
					mInitialMotionX = x;
					mInitialMotionY = y;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			resetTouch();
			break;
		}
		return super.onTouchEvent(event);
	}

	private void resetTouch() {
		if (mOnScrollChangedListener != null) {
			mOnScrollChangedListener.onDragDown(0);
		}
		mInitialMotionY = -1f;
	}

	@Override
	public int computeVerticalScrollRange() {
		return super.computeVerticalScrollRange();
	}

	public void setOnScrollChangedListener(OnScrollChangedListener listener) {
		mOnScrollChangedListener = listener;
	}

	@Override
	protected float getTopFadingEdgeStrength() {
		// http://stackoverflow.com/a/6894270/244576
		if (mDisableEdgeEffects
				&& Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			return 0.0f;
		}
		return super.getTopFadingEdgeStrength();
	}

	@Override
	protected float getBottomFadingEdgeStrength() {
		// http://stackoverflow.com/a/6894270/244576
		if (mDisableEdgeEffects
				&& Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			return 0.0f;
		}
		return super.getBottomFadingEdgeStrength();
	}
}
