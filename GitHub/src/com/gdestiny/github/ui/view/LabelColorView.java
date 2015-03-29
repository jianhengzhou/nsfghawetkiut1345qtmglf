package com.gdestiny.github.ui.view;

import java.util.List;

import org.eclipse.egit.github.core.Label;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class LabelColorView extends View {

	private final int COLOR_WIDTH = 7;
	private final int MARGIN = 5;

	private Paint paint;

	private int[] colors;

	// private int[] colors = { 0xff359ff2, 0xffa9b7b7, 0xff56abe4, 0xffaab7b7
	// };

	public LabelColorView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = 0;
		if (colors != null) {
			height = colors.length * COLOR_WIDTH + (colors.length + 1) * MARGIN;
		}
		// height = 100;
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (colors == null)
			return;
		if (paint == null)
			paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		int y = MARGIN;
		for (int color : colors) {
			paint.setColor(color);
			canvas.drawRect(0, y, getWidth(), y + COLOR_WIDTH, paint);
			y = y + COLOR_WIDTH + MARGIN;
		}
	}

	public void setColors(int[] colors) {
		this.colors = colors;
		requestLayout();
		invalidate();
	}

	public void setLabels(List<Label> labels) {
		if (labels == null || labels.isEmpty()) {
			colors = null;
			requestLayout();
			postInvalidate();
			return;
		}
		colors = new int[labels.size()];
		for (int i = 0; i < labels.size(); i++) {
			colors[i] = Color.parseColor("#" + labels.get(i).getColor());
		}
		requestLayout();
		postInvalidate();
	}
}
