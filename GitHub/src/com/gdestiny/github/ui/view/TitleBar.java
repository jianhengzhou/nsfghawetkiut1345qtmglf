package com.gdestiny.github.ui.view;

import com.gdestiny.github.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class TitleBar extends LinearLayout {

	public TitleBar(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.layout_titlebar, this);
		setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
	}

}
