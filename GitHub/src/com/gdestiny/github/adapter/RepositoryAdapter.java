package com.gdestiny.github.adapter;

import java.util.List;

import org.eclipse.egit.github.core.Repository;

import com.gdestiny.github.R;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RepositoryAdapter extends BaseAdapter {

	private List<Repository> repositoryList;
	private Context context;

	public RepositoryAdapter(Context context, List<Repository> repositoryList) {
		this.repositoryList = repositoryList;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (repositoryList == null)
			return 0;
		return repositoryList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_repository, null);
			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.name.setText(repositoryList.get(position).getName());
		if (TextUtils.isEmpty(repositoryList.get(position).getLanguage())) {
			if (holder.language.getVisibility() == View.VISIBLE)
				holder.language.setVisibility(View.GONE);
		} else {
			holder.language.setText(repositoryList.get(position).getLanguage());
			if (holder.language.getVisibility() == View.GONE)
				holder.language.setVisibility(View.VISIBLE);
		}
		if (TextUtils.isEmpty(repositoryList.get(position).getDescription())) {
			if (holder.description.getVisibility() == View.VISIBLE)
				holder.description.setVisibility(View.GONE);
		} else {
			holder.description.setText(repositoryList.get(position).getDescription());
			if (holder.description.getVisibility() == View.GONE)
				holder.description.setVisibility(View.VISIBLE);
		}
		holder.star.setText(repositoryList.get(position).getWatchers() + "");
		holder.pull.setText(repositoryList.get(position).getForks() + "");
		return convertView;
	}

	private class Holder {

		public Holder(View v) {
			name = (TextView) v.findViewById(R.id.repository_name);
			description = (TextView) v.findViewById(R.id.repository_description);
			language = (TextView) v.findViewById(R.id.repository_language);
			star = (TextView) v.findViewById(R.id.repository_star);
			pull = (TextView) v.findViewById(R.id.repository_pull);
		}

		public TextView name;
		public TextView description;
		public TextView language;
		public TextView star;
		public TextView pull;
	}
}
