package com.gdestiny.github.adapter;

import java.util.List;

import org.eclipse.egit.github.core.Label;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gdestiny.github.R;

public class LabelAdapter extends BaseAdapter {

	private Context context;
	private List<Label> labels;

	public LabelAdapter(Context context) {
		this.context = context;

	}

	public LabelAdapter(Context context, List<Label> labels) {
		this.context = context;
		this.labels = labels;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (labels != null)
			return labels.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_label, null);
			holder = new Holder(convertView);

			Label label = labels.get(position);
			holder.name.setText(label.getName());
			
			int color = Color.parseColor("#" + label.getColor());
			// System.out.println("blue:"+Color.blue(color));
			// System.out.println("green:"+Color.green(color));
			// System.out.println("red:"+Color.red(color));
			
			holder.name.setBackgroundColor(color);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		return convertView;
	}

	public void setDatas(List<Label> labels) {
		this.labels = labels;
	}

	private class Holder {
		@SuppressWarnings("unused")
		CheckBox checkBox;
		TextView name;

		public Holder(View v) {
			checkBox = (CheckBox) v.findViewById(R.id.checkbox);
			name = (TextView) v.findViewById(R.id.name);
		}
	}
}
