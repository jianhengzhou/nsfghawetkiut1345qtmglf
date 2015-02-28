package com.gdestiny.github.ui.view;

import java.util.List;

import org.eclipse.egit.github.core.Label;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.GLog;

public class LabelViewGroup extends ViewGroup {

	private Context context;
	private final static int MARGIN = 5;

	public LabelViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int maxWidth = MeasureSpec.getSize(widthMeasureSpec)- getPaddingLeft() - getPaddingRight();

		int row = 1;
		int lengthX = 0;
		int lengthY = 0;

		int measureWidth = MeasureSpec.getSize(widthMeasureSpec); 
        int measureHeigth = MeasureSpec.getSize(heightMeasureSpec); 
		for (int index = 0; index < getChildCount(); index++) {
			final View child = getChildAt(index);
			// measure
			int widthSpec = 0; 
            int heightSpec = 0; 
            LayoutParams params = child.getLayoutParams(); 
            if(params.width > 0){ 
                widthSpec = MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.EXACTLY); 
            }else if (params.width == -1) { 
                widthSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.EXACTLY); 
            } else if (params.width == -2) { 
                widthSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.AT_MOST); 
            } 

            if(params.height > 0){ 
                heightSpec = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY); 
            }else if (params.height == -1) { 
                heightSpec = MeasureSpec.makeMeasureSpec(measureHeigth, MeasureSpec.EXACTLY); 
            } else if (params.height == -2) { 
                heightSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.AT_MOST); 
            } 
			child.measure(widthSpec, heightSpec);
			//
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();
			lengthX += width + MARGIN;
			lengthY = row * (height + MARGIN) + MARGIN + height;
			if (lengthX > maxWidth - MARGIN) {
				lengthX = width + MARGIN;
				row++;
				lengthY = row * (height + MARGIN) + MARGIN + height;

			}
		}

		setMeasuredDimension(maxWidth, lengthY);
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int count = getChildCount();
		int row = 0;
		int lengthX = l;
		int lengthY = t;
		for (int i = 0; i < count; i++) {

			final View child = this.getChildAt(i);
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();
			lengthX += width + MARGIN;
			lengthY = row * (height + MARGIN) + MARGIN + height + t;
			// if it can't drawing on a same line , skip to next line
			if (lengthX > r - MARGIN) {
				lengthX = width + MARGIN + l;
				row++;
				lengthY = row * (height + MARGIN) + MARGIN + height + t;

			}

			child.layout(lengthX - width, lengthY - height, lengthX, lengthY);
		}
	}

	public void setLabel(List<Label> labels) {
		removeAllViews();
		if (labels == null || labels.isEmpty())
			return;
		for (int i = 0; i < labels.size(); i++) {
			GLog.sysout("add label:" + labels.get(i).getName());
			addView(newView(labels.get(i)));
		}
	}

	public View newView(Label label) {
		View v = LayoutInflater.from(context).inflate(R.layout.layout_label,
				null);
		TextView tv = (TextView) v.findViewById(R.id.name);
		tv.setText(label.getName());
		tv.setBackgroundColor(Color.parseColor("#" + label.getColor()));
		return v;
	}
}
