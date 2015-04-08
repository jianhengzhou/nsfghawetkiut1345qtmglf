package com.gdestiny.github.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.gdestiny.github.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchHistoryAdapter extends BaseAdapter {
	private List<String> datas;
	private Context context;

	public List<String> getDatas() {
		return datas;
	}

	public void setDatas(ArrayList<String> datas) {
		this.datas = datas;
	}

	public SearchHistoryAdapter(Context context, ArrayList<String> datas) {
		this.context = context;
		this.datas = datas;
	}

	public SearchHistoryAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		if (datas == null)
			return 1;
		return datas.size() + 1;
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public boolean contains(String data) {
		if (datas == null)
			return false;
		return datas.contains(data);
	}

	public void add(String data) {
		if (datas == null)
			datas = new ArrayList<String>();
		datas.add(0, data);
		notifyDataSetChanged();
	}

	public void addAll(Collection<? extends String> collection) {
		if (collection == null)
			return;
		if (datas == null)
			datas = new ArrayList<String>();
		datas.addAll(collection);
		notifyDataSetChanged();
	}

	public void clear() {
		if (datas == null)
			return;
		datas.clear();
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_status_pop, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == getCount() - 1) {
			holder.name.setText("Clear History");
		} else {
			holder.name.setText(datas.get(position));
		}

		return convertView;
	}

	public static class ViewHolder {
		public ImageView icon;
		public TextView name;

		public ViewHolder(View v) {
			this.name = (TextView) v.findViewById(R.id.status_name);
			this.icon = (ImageView) v.findViewById(R.id.status_icon);
			this.icon.setImageResource(R.drawable.common_time);
		}

	}

}
