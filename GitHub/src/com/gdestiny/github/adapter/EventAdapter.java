package com.gdestiny.github.adapter;

import java.util.List;

import org.eclipse.egit.github.core.event.Event;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.TimeUtils;
import com.gdestiny.github.utils.ViewUtils;
import com.gdestiny.github.utils.client.EventUtils;

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

		holder.title.setText(EventUtils.getEventTitle(event));
		holder.date.setText(TimeUtils.getTime(event.getCreatedAt().getTime()));
		SpannableStringBuilder content = EventUtils.getEventDetail(event);
		if (TextUtils.isEmpty(content)) {
			ViewUtils.setVisibility(holder.content, View.GONE);
		} else {
			ViewUtils.setVisibility(holder.content, View.VISIBLE);
			// holder.content.setText(Html.fromHtml(content, new
			// AsyncImageGetter(
			// context, holder.content), null));
			holder.content.setText(content);
		}

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
		TextView title;
		TextView date;

		public Holder(View v) {
			icon = (ImageView) v.findViewById(R.id.icon);
			content = (TextView) v.findViewById(R.id.content);
			title = (TextView) v.findViewById(R.id.title);
			date = (TextView) v.findViewById(R.id.date);
		}
	}
}
