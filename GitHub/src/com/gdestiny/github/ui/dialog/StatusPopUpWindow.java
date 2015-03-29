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
import android.view.animation.AnimationUtils;
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

	// 二级菜单
	private ListView mListViewSecondly;
	private StatusPopUpAdapter mSecondlyAdapter;
	private ArrayList<StatusPopWindowItem> mSecondlyActionItems;

	public interface StatusPopUpWindowItemClickListener {
		public void onitemclick(int titleId);
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

	public void initSecondlyStatus() {
		getSecondlyListView();
		mSecondlyActionItems = new ArrayList<StatusPopWindowItem>();
		mSecondlyAdapter = new StatusPopUpAdapter(mContext,
				mSecondlyActionItems);
		mListViewSecondly.setAdapter(mSecondlyAdapter);
		mListViewSecondly.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == mSecondlyAdapter.getCount() - 1) {
					backUpperLever();
					return;
				}
				if (mOnitemclicklistener != null) {
					mOnitemclicklistener.onitemclick(mSecondlyActionItems
							.get(arg2).titleid);
				}
			}
		});
	}

	public void setSecondlyItem(Context context,
			LinkedHashMap<Integer, Integer> itemmap) {
		mSecondlyActionItems.clear();
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
				mSecondlyActionItems.add(action);
			}
		}
		// 增加返回
		mSecondlyActionItems.add(new StatusPopWindowItem(context,
				"Upper Level", R.drawable.common_secondly_back));
		mSecondlyAdapter.notifyDataSetChanged();

	}

	public void showSecondlyStatus() {
		if (mListViewSecondly != null) {
			mListView.setAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.right_to_left_out));
			mListView.setVisibility(View.GONE);
			mListViewSecondly.setAnimation(AnimationUtils.loadAnimation(
					mContext, R.anim.right_to_left_in));
			mListViewSecondly.setVisibility(View.VISIBLE);
		}
	}

	public void backUpperLever() {
		if (mListViewSecondly != null) {
			mListViewSecondly.setAnimation(AnimationUtils.loadAnimation(
					mContext, R.anim.left_to_right_out));
			mListViewSecondly.setVisibility(View.GONE);
			mListView.setAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.left_to_right_in));
			mListView.setVisibility(View.VISIBLE);
		}
	}

	public void resetSecondly() {
		if (mListViewSecondly != null) {
			if (mListViewSecondly.getVisibility() == View.VISIBLE) {
				mListView.setVisibility(View.VISIBLE);
				mListViewSecondly.setVisibility(View.GONE);
			}
		}
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
	 * 添加子类项
	 */
	public void addItem(Context context, int titleId, int drawableId) {
		StatusPopWindowItem action = null;
		if (drawableId == 0)
			action = new StatusPopWindowItem(context, titleId);
		else
			action = new StatusPopWindowItem(context, titleId, drawableId);
		if (action != null && !mActionItems.contains(action)) {
			mActionItems.add(action);
		}
		mAdapter.notifyDataSetChanged();

	}

	/**
	 * 添加子类项
	 */
	public void addItem(Context context, int position, int titleId,
			int drawableId) {
		StatusPopWindowItem action = null;
		if (drawableId == 0)
			action = new StatusPopWindowItem(context, titleId);
		else
			action = new StatusPopWindowItem(context, titleId, drawableId);
		if (action != null && !mActionItems.contains(action)) {
			mActionItems.add(position, action);
		}
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

	public ListView getSecondlyListView() {
		if (mListViewSecondly == null)
			mListViewSecondly = (ListView) getContentView().findViewById(
					R.id.popwindow_list_secondly);
		return mListViewSecondly;
	}

}
