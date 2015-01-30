package com.gdestiny.github.adapter;

import java.util.List;

import org.eclipse.egit.github.core.event.Event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.EventUtils;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.TimeUtils;

public class EventAdapter extends BaseAdapter {

	private Context context;
	private List<Event> datas;

	public EventAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		if (datas == null)
			return 0;
		return datas.size();
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
		Holder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_event, null);
			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		Event event = datas.get(position);

		ImageLoaderUtils.displayImage(EventUtils.getAuthorAvatarUrl(event),
				holder.icon, R.drawable.default_avatar,
				R.drawable.default_avatar, true);

		holder.name.setText(EventUtils.getAuthor(event));
		holder.date.setText(TimeUtils.getTime(event.getCreatedAt().getTime()));
		holder.content.setText(event.getRepo().getName());
		holder.type.setText(event.getType());
		return convertView;
	}

	public List<Event> getDatas() {
		return datas;
	}

	public void setDatas(List<Event> datas) {
		this.datas = datas;
		notifyDataSetChanged();
	}

	private class Holder {
		ImageView icon;
		TextView content;
		TextView name;
		TextView date;
		TextView type;

		public Holder(View v) {
			icon = (ImageView) v.findViewById(R.id.icon);
			content = (TextView) v.findViewById(R.id.content);
			name = (TextView) v.findViewById(R.id.name);
			date = (TextView) v.findViewById(R.id.date);
			type = (TextView) v.findViewById(R.id.type);
		}
	}
}
