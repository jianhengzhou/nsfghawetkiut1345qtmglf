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
 * ��������������
 */
public class StatusPopUpWindow extends PopupWindow {
	private Context mContext;
	// �����б����
	private ListView mListView;
	private StatusPopUpAdapter mAdapter;
	// ���嵯���������б�
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
		// ��������ⴥ���¼�����������
		setBackgroundDrawable(new BitmapDrawable());
		// ���õ����Ĳ��ֽ���Ͷ���
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
	 * ��ʾ�����б����
	 */
	public void showwindow(View view) {
		showAsDropDown(view);
	}

	/**
	 * ���������
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
	 * ����λ�õõ�������
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
