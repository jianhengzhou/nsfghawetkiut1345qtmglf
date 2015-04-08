package com.gdestiny.github.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.bean.SearchUser;
import com.gdestiny.github.utils.ImageLoaderUtils;

public class SearchUserAdapter extends BaseAdapter {

	private Context context;
	private List<SearchUser> datas;

	public SearchUserAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		if (datas == null)
			return 0;
		return datas.size();
	}

	@Override
	public SearchUser getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
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

		SearchUser user = datas.get(position);

		String id = user.getGravatarId();
		String url = "";
		if (!TextUtils.isEmpty(id)) {
			url = "https://secure.gravatar.com/avatar/" + id + "?d=404";
		}
		ImageLoaderUtils.displayImage(url, holder.icon,
				R.drawable.default_avatar, R.drawable.default_avatar, true);
		holder.name.setText(user.getLogin());
		return convertView;
	}

	public List<SearchUser> getDatas() {
		return datas;
	}

	public void setDatas(List<SearchUser> datas) {
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
