package com.gdestiny.github.ui.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.gdestiny.github.R;
import com.gdestiny.github.bean.CodeTree;
import com.gdestiny.github.utils.CommonUtils;
import com.gdestiny.github.utils.GLog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PathView extends LinearLayout {

	private Context context;
	private List<String> pathItems = new ArrayList<String>();

	private HorizontalScrollView hs;
	private LinearLayout linearLayout;

	private PathClickListener listener;

	public interface PathClickListener {
		public void onPathClick(String path);
	}

	public PathView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public PathView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	private void init() {
		setOrientation(LinearLayout.HORIZONTAL);

		linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);

		hs = new HorizontalScrollView(context);
		hs.setHorizontalScrollBarEnabled(false);
		hs.addView(linearLayout);

		addView(newView(CodeTree.ROOT));
		addView(hs);
	}

	public void add(String path) {
		pathItems.add(path);
		linearLayout.addView(newView(path));
	}

	private View newView(final String path) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_path,
				null);
		((TextView) view.findViewById(R.id.path)).setText(CommonUtils
				.pathToName(path));
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					if (resetPath(path))
						listener.onPathClick(path);
				}
			}
		});

		return view;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		int width = 0;
		for (int i = 0; i < hs.getChildCount(); i++)
			width += hs.getChildAt(i).getWidth();
		hs.scrollTo(width, 0);
	}

	public boolean resetAll() {
		if (pathItems.isEmpty()) {
			GLog.sysout("still root no need to reset");
			return false;
		}
		pathItems.removeAll(pathItems);
		linearLayout.removeAllViews();
		return true;
	}

	/**
	 * 点击时重置
	 * 
	 * @param path
	 * @return
	 */
	public boolean resetPath(String path) {
		if (path.equals(CodeTree.ROOT))
			return resetAll();
		int index = pathItems.indexOf(path);
		if (index >= pathItems.size() - 1) {
			GLog.sysout("path on the same,no need to reset");
			return false;
		}
		for (int i = pathItems.size() - 1; i > index; i--) {
			GLog.sysout("remove:" + pathItems.get(i));
			pathItems.remove(i);
		}
		linearLayout.removeAllViews();
		for (int i = 0; i < pathItems.size(); i++) {
			linearLayout.addView(newView(pathItems.get(i)));
		}
		return true;
	}

	public PathClickListener getPathListener() {
		return listener;
	}

	public void setPathListener(PathClickListener listener) {
		this.listener = listener;
	}

	public List<String> getPathItems() {
		return pathItems;
	}

	public void setPathItems(List<String> pathItems) {
		this.pathItems = pathItems;
	}

	/**
	 * 刷新时用
	 */
	public void resetView(String path) {
		GLog.sysout("reset:" + path);
		if (!pathItems.isEmpty()) {
			pathItems.removeAll(pathItems);
			linearLayout.removeAllViews();
		}
		if (!path.contains(File.separator)) {
			add(path);
			return;
		}
		int index = path.indexOf(File.separator);
		int lastIndex = path.lastIndexOf(File.separator);
		while (index > 0 && index <= lastIndex) {
			String temp = path.substring(0, index);
			GLog.sysout("index=" + index + "," + temp);
			add(temp);
			index = path.indexOf(File.separator, index + 1);
		}
		add(path.substring(lastIndex));

	}
}
