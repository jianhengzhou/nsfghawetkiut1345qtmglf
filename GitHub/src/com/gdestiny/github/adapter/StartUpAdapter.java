package com.gdestiny.github.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.ViewUtils;

public class StartUpAdapter extends BaseAdapter {

	private Context context;
	private String[] array;
	private int curStartup = 0;

	public StartUpAdapter(Context context) {
		this.context = context;
		array = context.getResources().getStringArray(R.array.startup_list);
	}

	@Override
	public int getCount() {
		if (array == null)
			return 0;
		return array.length;
	}

	public String[] getArray() {
		return array;
	}

	public void setArray(String[] array) {
		this.array = array;
	}

	public int getCurStartup() {
		return curStartup;
	}

	public void setCurStartup(int curStartup) {
		this.curStartup = curStartup;
		notifyDataSetChanged();
	}

	@Override
	public String getItem(int position) {
		return array[position];
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
					R.layout.item_age, null);
			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.title.setText(array[position]);
		if (position == curStartup) {
			ViewUtils.setVisibility(holder.selected, View.VISIBLE);
		} else
			ViewUtils.setVisibility(holder.selected, View.INVISIBLE);
		return convertView;
	}

	class Holder {
		ImageView selected;
		TextView title;

		public Holder(View v) {
			selected = (ImageView) v.findViewById(R.id.selected);
			title = (TextView) v.findViewById(R.id.title);
		}
	}
}