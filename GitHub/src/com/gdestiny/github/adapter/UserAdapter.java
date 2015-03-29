package com.gdestiny.github.adapter;

import java.util.List;

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

public class UserAdapter extends BaseAdapter {

	private Context context;
	private List<User> datas;

	public UserAdapter(Context context) {
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
					R.layout.item_user, null);
			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		User user = datas.get(position);

		ImageLoaderUtils.displayImage(user.getAvatarUrl(), holder.icon,
				R.drawable.default_avatar, R.drawable.default_avatar, true);

		holder.name.setText(user.getLogin());
		return convertView;
	}

	public List<User> getDatas() {
		return datas;
	}

	public void setDatas(List<User> datas) {
		this.datas = datas;
		notifyDataSetChanged();
	}

	private class Holder {
		ImageView icon;
		TextView name;

		public Holder(View v) {
			icon = (ImageView) v.findViewById(R.id.icon);
			name = (TextView) v.findViewById(R.id.name);
		}
	}
}
