package com.gdestiny.github.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.CommonUtils;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class IndicatorView extends LinearLayout implements OnPageChangeListener {

	private int currentPosition = 0;
	private List<View> indecators;

	private OnPageChangeListener listener;
	private ViewPager viewpager;

	private float positionOffsetPixels;
	private int scrollPosition;

	private Paint paint;

	public IndicatorView(Context context) {
		super(context);
		init();
	}

	public IndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setOrientation(LinearLayout.HORIZONTAL);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(getContext().getResources().getColor(
				R.color.common_indicator_pressed));
	}

	private View newView(String name, int drawableId) {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.item_indicator, null);

		TextView textView = ((TextView) view.findViewById(R.id.name));
		textView.setText(CommonUtils.pathToName(name));
		Drawable drawable = getResources().getDrawable(drawableId);
		// drawable.setBounds(0, 0, drawable.getMinimumWidth(),
		// drawable.getMinimumHeight());
		drawable.setBounds(0, 0, AndroidUtils.dpToPxInt(getContext(), 20),
				AndroidUtils.dpToPxInt(getContext(), 20));
		textView.setCompoundDrawables(null, drawable, null, null);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.width = 0;
		params.weight = 1.0f;
		params.gravity = Gravity.CENTER;
		view.setLayoutParams(params);

		final int _positionForClick = indecators.size();
		// if (_positionForClick == currentPosition) {
		// changeState(view, true);
		// }
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// onState(_positionForClick);
				if (viewpager != null) {
					viewpager.setCurrentItem(_positionForClick, true);
				}
			}
		});
		return view;
	}

	public IndicatorView add(String name, int drawableId) {
		if (indecators == null)
			indecators = new ArrayList<View>();
		View view = newView(name, drawableId);
		indecators.add(view);
		addView(view);
		return this;
	}

	public IndicatorView add(int id, int drawableId) {
		if (indecators == null)
			indecators = new ArrayList<View>();
		View view = newView(getContext().getResources().getString(id),
				drawableId);
		indecators.add(view);
		addView(view);
		return this;
	}

	// public void changeState(View view, boolean pressed) {
	// if (pressed) {
	// // view.setBackgroundResource(R.drawable.selector_indicator_pressed);
	// } else {
	// // view.setBackgroundResource(R.drawable.selector_indicator_normal);
	// }
	// }

	// public void onState(int position) {
	// if (position != currentPosition) {
	// changeState(indecators.get(currentPosition), false);
	// changeState(indecators.get(position), true);
	// currentPosition = position;
	// }
	// }

	public int getCurrentPosition() {
		return currentPosition;
	}

	public OnPageChangeListener getListener() {
		return listener;
	}

	public void setOnPageChangeListener(OnPageChangeListener listener) {
		this.listener = listener;
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		if (listener != null) {
			listener.onPageScrollStateChanged(state);
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		if (listener != null) {
			listener.onPageScrolled(position, positionOffset,
					positionOffsetPixels);
		}
		this.positionOffsetPixels = positionOffsetPixels;
		scrollPosition = position;
		invalidate();
	}

	@Override
	public void onPageSelected(int position) {
		if (listener != null) {
			listener.onPageSelected(position);
		}
		currentPosition = position;
		// onState(position);
	}

	public void bind(ViewPager viewpager) {
		this.viewpager = viewpager;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (indecators == null)
			return;
		float left = scrollPosition * getWidth() / indecators.size()
				+ positionOffsetPixels / indecators.size();
		canvas.drawRect(left, 0, left + getWidth() / indecators.size(),
				getHeight(), paint);
	}

}
