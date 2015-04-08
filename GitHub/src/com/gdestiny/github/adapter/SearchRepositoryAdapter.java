package com.gdestiny.github.adapter;

import java.util.List;

import org.eclipse.egit.github.core.SearchRepository;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.ViewUtils;

public class SearchRepositoryAdapter extends BaseAdapter {

	private List<SearchRepository> datas;
	private Context context;

	public SearchRepositoryAdapter(Context context,
			List<SearchRepository> repositoryList) {
		this.datas = repositoryList;
		this.context = context;
	}

	public SearchRepositoryAdapter(Context context) {
		this.context = context;
	}

	public void setData(List<SearchRepository> repositoryList) {
		this.datas = repositoryList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (datas == null)
			return 0;
		return datas.size();
	}

	@Override
	public SearchRepository getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Holder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_search_repository, null);
			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		SearchRepository repo = datas.get(position);
		holder.name.setText(repo.getName());
		if (TextUtils.isEmpty(repo.getLanguage())) {
			ViewUtils.setVisibility(holder.language, View.GONE);
		} else {
			holder.language.setText(repo.getLanguage());
			ViewUtils.setVisibility(holder.language, View.VISIBLE);
		}
		if (TextUtils.isEmpty(repo.getDescription())) {
			ViewUtils.setVisibility(holder.description, View.GONE);
		} else {
			holder.description.setText(repo.getDescription());
			ViewUtils.setVisibility(holder.description, View.VISIBLE);
		}
		holder.star.setText(repo.getWatchers() + "");
		holder.pull.setText(repo.getForks() + "");
		holder.owner.setText(repo.getOwner());
		return convertView;
	}

	private class Holder {

		public Holder(View v) {
			name = (TextView) v.findViewById(R.id.repository_name);
			description = (TextView) v
					.findViewById(R.id.repository_description);
			owner = (TextView) v.findViewById(R.id.repository_owner);
			language = (TextView) v.findViewById(R.id.repository_language);
			star = (TextView) v.findViewById(R.id.repository_star);
			pull = (TextView) v.findViewById(R.id.repository_pull);

		}

		public TextView name;
		public TextView description;
		public TextView owner;
		public TextView language;
		public TextView star;
		public TextView pull;

	}
}
