package com.gdestiny.github.adapter;

import java.util.List;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.TimeUtils;

public class GistAdapter extends BaseAdapter {

	private Context context;
	private List<Gist> datas;

	public GistAdapter(Context context) {
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
					R.layout.item_gist, null);
			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		Gist gist = datas.get(position);

		User user = gist.getUser();
		if (user != null) {
			ImageLoaderUtils.displayImage(gist.getUser().getAvatarUrl(),
					holder.icon, R.drawable.default_avatar,
					R.drawable.default_avatar, true);

			holder.name.setText(gist.getUser().getLogin());
		} else {
			ImageLoaderUtils.displayImage("",
					holder.icon, R.drawable.common_anonymous,
					R.drawable.common_anonymous, true);
			holder.name.setText("Anonymous");
		}
		holder.date.setText(TimeUtils.getTime(gist.getCreatedAt().getTime()));
		holder.content.setText(gist.getDescription());
		holder.comment.setText(gist.getComments() + "");
		holder.sha.setText(gist.getId());
		holder.file.setText(gist.getFiles().size() + "");

		return convertView;
	}

	public List<Gist> getDatas() {
		return datas;
	}

	public void setDatas(List<Gist> datas) {
		this.datas = datas;
		notifyDataSetChanged();
	}

	private class Holder {
		ImageView icon;
		TextView content;
		TextView name;
		TextView date;
		TextView comment;
		TextView file;
		TextView sha;

		public Holder(View v) {
			icon = (ImageView) v.findViewById(R.id.icon);
			content = (TextView) v.findViewById(R.id.content);
			name = (TextView) v.findViewById(R.id.name);
			date = (TextView) v.findViewById(R.id.date);
			comment = (TextView) v.findViewById(R.id.comment);
			file = (TextView) v.findViewById(R.id.file);
			sha = (TextView) v.findViewById(R.id.sha);
		}
	}
}
