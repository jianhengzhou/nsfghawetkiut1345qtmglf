package com.gdestiny.github.adapter;

import java.io.File;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gdestiny.github.R;

public class SaveFileAdapter extends BaseAdapter {

	private Context context;
	private File[] datas;

	public SaveFileAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		if (datas == null)
			return 0;
		return datas.length;
	}

	@Override
	public File getItem(int position) {
		return datas[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_save_file, null);
			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		File file = datas[position];
		holder.name.setText(file.getName());

		return convertView;
	}

	public File[] getDatas() {
		return datas;
	}

	public void setDatas(File[] datas) {
		this.datas = datas;
		notifyDataSetChanged();
	}

	private class Holder {
		TextView name;

		public Holder(View v) {
			name = (TextView) v.findViewById(R.id.name);
		}
	}
}
