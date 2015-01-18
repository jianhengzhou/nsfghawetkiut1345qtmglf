package com.gdestiny.github.adapter;

import java.util.List;

import org.eclipse.egit.github.core.Repository;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.ViewUtils;

public class RepositoryAdapter extends BaseAdapter {

	private List<?> repositoryList;
	private Context context;

	public RepositoryAdapter(Context context, List<?> repositoryList) {
		this.repositoryList = repositoryList;
		this.context = context;
	}

	public RepositoryAdapter(Context context) {
		this.context = context;
	}

	public void setData(List<?> repositoryList) {
		this.repositoryList = repositoryList;
		notifyDataSetChanged();
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
		if (repositoryList.get(position) instanceof Repository) {
			ViewUtils.setVisibility(holder.normoalItem, View.VISIBLE);
			ViewUtils.setVisibility(holder.tag, View.GONE);

			Repository repo = (Repository) repositoryList.get(position);
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
			holder.owner.setText(repo.getOwner().getLogin());
			ImageLoaderUtils.displayImage(repo.getOwner().getAvatarUrl(),
					holder.icon, R.drawable.common_repository_item,
					R.drawable.common_repository_item);
		} else if (repositoryList.get(position) instanceof Character) {
			holder.tag.setText((Character) repositoryList.get(position) + "");
			ViewUtils.setVisibility(holder.normoalItem, View.GONE);
			ViewUtils.setVisibility(holder.tag, View.VISIBLE);
			// return tag;
		}
		return convertView;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		if (repositoryList.get(position) instanceof Character)
			return false;
		return super.isEnabled(position);
	}

	private class Holder {

		public Holder(View v) {
			icon = (ImageView) v.findViewById(R.id.repository_icon);
			name = (TextView) v.findViewById(R.id.repository_name);
			description = (TextView) v
					.findViewById(R.id.repository_description);
			owner = (TextView) v.findViewById(R.id.repository_owner);
			language = (TextView) v.findViewById(R.id.repository_language);
			star = (TextView) v.findViewById(R.id.repository_star);
			pull = (TextView) v.findViewById(R.id.repository_pull);

			tag = (TextView) v.findViewById(R.id.item_repo_tag);
			normoalItem = v.findViewById(R.id.item_repo_normal);
		}

		public ImageView icon;
		public TextView name;
		public TextView description;
		public TextView owner;
		public TextView language;
		public TextView star;
		public TextView pull;

		public View normoalItem;
		public TextView tag;
	}
}
