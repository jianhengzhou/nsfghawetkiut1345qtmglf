package com.gdestiny.github.ui.view;

import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdestiny.github.R;

public class FileIndicatorView extends HorizontalScrollView {

	private Context context;
	private LinearLayout linearLayout;

	private TabClickListener listener;

	private HashMap<String, TextView> items = new HashMap<String, TextView>();
	private String curTab;

	public interface TabClickListener {
		public void onTabClick(String tab);
	}

	public FileIndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	private void init() {

		linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);

		setHorizontalScrollBarEnabled(false);
		addView(linearLayout);

	}

	public void add(String file) {
		TextView view = newView(file);
		items.put(file, view);

		linearLayout.addView(view);
		if (file.equals(curTab))
			checkTab(view);
	}

	public void add(String selected, Set<String> files) {
		curTab = selected;
		for (String file : files) {
			add(file);
		}
	}

	private TextView newView(String file) {
		TextView view = (TextView) LayoutInflater.from(context).inflate(
				R.layout.item_file_text, null);
		view.setText(file);
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					// if (resetPath(file))
					TextView tv = (TextView) v;
					String tab = tv.getText().toString();
					onTabClick(tv, tab);
					listener.onTabClick(tab);
				}
			}
		});

		return view;
	}

	private void onTabClick(TextView v, String tab) {
		if (tab.equals(curTab))
			return;
		// ÖØÖÃ
		TextView tv = items.get(curTab);
		uncheckTab(tv);
		checkTab(v);
		curTab = tab;
	}

	private void uncheckTab(TextView tv) {
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		tv.getPaint().setFakeBoldText(false);
		tv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		// setCompoundDrawablesWithIntrinsicBounds
	}

	private void checkTab(TextView tv) {
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv.getPaint().setFakeBoldText(true);
		Drawable drawable = getResources().getDrawable(
				R.drawable.common_tab_indicator);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		tv.setCompoundDrawables(null, null, null, drawable);
	}

	// @Override
	// protected void onLayout(boolean changed, int l, int t, int r, int b) {
	// super.onLayout(changed, l, t, r, b);
	// int width = 0;
	// for (int i = 0; i < getChildCount(); i++)
	// width += getChildAt(i).getWidth();
	// hs.scrollTo(width, 0);
	// }

	public void setOnTabListener(TabClickListener listener) {
		this.listener = listener;
	}

}
