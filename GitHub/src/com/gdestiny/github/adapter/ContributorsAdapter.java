package com.gdestiny.github.adapter;

import java.util.List;

import org.eclipse.egit.github.core.Contributor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.ImageLoaderUtils;

public class ContributorsAdapter extends BaseAdapter {

	private Context context;
	private List<Contributor> datas;

	public ContributorsAdapter(Context context) {
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
		return null;
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
					R.layout.item_contributor, null);
			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		Contributor contributor = datas.get(position);
		//
		// System.out.println("getAvatarUrl:" + contributor.getAvatarUrl());
		// System.out
		// .println("getContributions:" + contributor.getContributions());
		// System.out.println("getLogin:" + contributor.getLogin());
		// System.out.println("getName:" + contributor.getName());
		// System.out.println("getType:" + contributor.getType());
		// System.out.println("getUrl:" + contributor.getUrl());

		ImageLoaderUtils.displayImage(contributor.getAvatarUrl(), holder.icon,
				R.drawable.default_avatar, R.drawable.default_avatar, true);
		holder.name.setText(contributor.getLogin());
		holder.commit.setText(contributor.getContributions() + " commits");
		return convertView;
	}

	public List<Contributor> getDatas() {
		return datas;
	}

	public void setDatas(List<Contributor> datas) {
		this.datas = datas;
		notifyDataSetChanged();
	}

	private class Holder {
		ImageView icon;
		TextView name;
		TextView commit;

		public Holder(View v) {
			icon = (ImageView) v.findViewById(R.id.icon);
			name = (TextView) v.findViewById(R.id.name);
			commit = (TextView) v.findViewById(R.id.commit);
		}
	}
}
