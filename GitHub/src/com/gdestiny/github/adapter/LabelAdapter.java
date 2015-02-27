package com.gdestiny.github.adapter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Label;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.GLog;

public class LabelAdapter extends BaseAdapter {

	private Context context;
	private List<Label> labels;

	private ArrayList<Label> selectedLabel;

	public LabelAdapter(Context context) {
		this.context = context;
		selectedLabel = new ArrayList<Label>();
	}

	public LabelAdapter(Context context, ArrayList<Label> selectedLabel) {
		this.context = context;
		this.selectedLabel = selectedLabel;
		if (this.selectedLabel == null)
			this.selectedLabel = new ArrayList<Label>();
	}

	public LabelAdapter(Context context, List<Label> labels) {
		this(context);
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
		} else {
			holder = (Holder) convertView.getTag();
		}

		final Label label = labels.get(position);
		holder.name.setText(label.getName());

		int color = Color.parseColor("#" + label.getColor());
		// System.out.println("blue:"+Color.blue(color));
		// System.out.println("green:"+Color.green(color));
		// System.out.println("red:"+Color.red(color));

		holder.name.setBackgroundColor(color);
		// holder.name.getPaint().setFlags(android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
		convertView.setTag(holder);

		holder.checkBox.setChecked(selectedLabel.contains(label));
		holder.checkBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							if (!selectedLabel.contains(label))
								selectedLabel.add(label);
						} else {
							if (selectedLabel.contains(label))
								selectedLabel.remove(label);
						}
						GLog.sysout(label.getName() + "," + isChecked);
					}
				});

		return convertView;
	}

	public void setDatas(List<Label> labels) {
		this.labels = labels;
	}

	public ArrayList<Label> getSelectedLabel() {
		return selectedLabel;
	}

	public LabelAdapter setSelectedLabel(ArrayList<Label> selectedLabel) {
		this.selectedLabel = selectedLabel;
		if (this.selectedLabel == null)
			this.selectedLabel = new ArrayList<Label>();
		return this;
	}

	private class Holder {
		CheckBox checkBox;
		TextView name;

		public Holder(View v) {
			checkBox = (CheckBox) v.findViewById(R.id.checkbox);
			name = (TextView) v.findViewById(R.id.name);
		}
	}
}
