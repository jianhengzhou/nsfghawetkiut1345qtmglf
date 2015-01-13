package com.gdestiny.github.ui.dialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.StatusPopUpAdapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

/**
 * 
 * 功能描述：弹窗
 */
public class StatusPopUpWindow extends PopupWindow {
	private Context mContext;
	// 定义列表对象
	private ListView mListView;
	private StatusPopUpAdapter mAdapter;
	// 定义弹窗子类项列表
	private ArrayList<StatusPopWindowItem> mActionItems = new ArrayList<StatusPopWindowItem>();
	private StatusPopUpWindowItemClickListener mOnitemclicklistener = null;

	public interface StatusPopUpWindowItemClickListener {
		public void onitemclick(int drawableid);
	}

	@SuppressWarnings("deprecation")
	public StatusPopUpWindow(Context context, int width, int height, int style) {
		super(context);
		this.mContext = context;
		setFocusable(true);
		setTouchable(true);
		setOutsideTouchable(true);
		setWidth(width);
		setHeight(height);
		// 解决弹窗外触控事件焦点获得问题
		setBackgroundDrawable(new BitmapDrawable());
		// 设置弹窗的布局界面和动画
		setContentView(LayoutInflater.from(mContext).inflate(
				R.layout.layout_status_pop, null));
		setAnimationStyle(style);
		getListView();
		mAdapter = new StatusPopUpAdapter(mContext, mActionItems);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (mOnitemclicklistener != null) {
					mOnitemclicklistener.onitemclick(mActionItems.get(arg2).titleid);
				}
			}
		});
	}

	/**
	 * 显示弹窗列表界面
	 */
	public void showwindow(View view) {
		showAsDropDown(view);
	}

	/**
	 * 添加子类项
	 */
	public void setItem(Context context,
			LinkedHashMap<Integer, Integer> itemmap,
			StatusPopUpWindowItemClickListener mOnitemclicklistener) {
		mActionItems.clear();
		Iterator<Integer> iterator = itemmap.keySet().iterator();
		while (iterator.hasNext()) {
			int titleid = iterator.next().intValue();
			StatusPopWindowItem action = null;
			if (itemmap.get(titleid) == null) {
				action = new StatusPopWindowItem(context, titleid);
			} else {
				action = new StatusPopWindowItem(context, titleid,
						itemmap.get(titleid));
			}
			if (action != null) {
				mActionItems.add(action);
			}
		}
		this.mOnitemclicklistener = mOnitemclicklistener;
		mAdapter.notifyDataSetChanged();

	}

	/**
	 * 根据位置得到子类项
	 */
	public StatusPopWindowItem getAction(int position) {
		if (position < 0 || position > mActionItems.size())
			return null;
		return mActionItems.get(position);
	}

	public ListView getListView() {
		if (mListView == null)
			mListView = (ListView) getContentView().findViewById(
					R.id.popwindow_list);
		return mListView;
	}

}
