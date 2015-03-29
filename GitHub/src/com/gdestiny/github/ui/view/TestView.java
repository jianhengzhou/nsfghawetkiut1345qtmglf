package com.gdestiny.github.ui.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("DrawAllocation")
public class TestView extends View {

	float progress, progress2;
	ObjectAnimator animator;
	float x1, x2, y, r;

	float rx, ry;

	// ValueAnimator valueAnimation = ValueAnimator.ofFloat(0f, 1f);

	public TestView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		animator = ObjectAnimator.ofFloat(this, "progress", 0f, 0f);
		animator.setDuration(1000);
	}

	public TestView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

		animator = ObjectAnimator.ofFloat(this, "progress", 0f, 0f);
		animator.setDuration(1000);

		// valueAnimation.setDuration(1000);
		// valueAnimation.addUpdateListener(new AnimatorUpdateListener() {
		// @Override
		// public void onAnimationUpdate(ValueAnimator animation) {
		// setProgress((Float) animation.getAnimatedValue());
		// }
		// });
	}

	@Override
	public void setPressed(boolean pressed) {
		// TODO Auto-generated method stub
		System.out.println(pressed);
		if (animator == null)
			return;
		if (pressed) {
			animator.setFloatValues(progress, 200f);
			animator.setRepeatCount(ObjectAnimator.INFINITE);
			animator.setRepeatMode(ObjectAnimator.REVERSE);
		} else {
			animator.setFloatValues(progress, 0f);
			animator.setRepeatCount(0);
		}
		animator.start();
		// if (valueAnimation == null)
		// return;
		// if (pressed) {
		// valueAnimation.setFloatValues(0f, 200f);
		// } else {
		// valueAnimation.setFloatValues(progress, 0f);
		// }
		// valueAnimation.start();
		super.setPressed(pressed);
	}

	public float getProgress() {
		return progress;
	}

	public void setProgress(float progress) {
		this.progress = progress;
		this.invalidate();
	}

	public float getProgress2() {
		return progress2;
	}

	public void setProgress2(float progress2) {
		this.progress2 = progress2;
		this.invalidate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		x1 = w / 4;
		x2 = w * 3 / 4;
		y = h / 2;
		r = Math.min(w, h) / 8;

		rx = w / 2;
		ry = h / 2;
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		rx = event.getX();
		ry = event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(0xff56abe4);
		canvas.drawCircle(rx, ry, r + progress, paint);
		// canvas.drawCircle(x2, y, r * 2 - progress, paint);
	}
}
