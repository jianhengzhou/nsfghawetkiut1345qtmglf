package com.gdestiny.github.adapter;

import java.util.ArrayList;

import com.gdestiny.github.R;
import com.gdestiny.github.ui.dialog.StatusPopWindowItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StatusPopUpAdapter extends BaseAdapter {
	private ArrayList<StatusPopWindowItem> mActionItems;
	private LayoutInflater mInflater;
	private Context mContext;

	public StatusPopUpAdapter(Context context,
			ArrayList<StatusPopWindowItem> mActionItems) {
		this.mContext = context;
		this.mActionItems = mActionItems;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mActionItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mActionItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_status_pop, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.status_name);
			holder.icon = (ImageView) convertView
					.findViewById(R.id.status_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		StatusPopWindowItem item = mActionItems.get(position);
		holder.icon.setBackgroundDrawable(item.mDrawable);
		holder.name.setText(item.mTitle);

		return convertView;
	}

	public static class ViewHolder {
		public ImageView icon;
		public TextView name;

	}

}
