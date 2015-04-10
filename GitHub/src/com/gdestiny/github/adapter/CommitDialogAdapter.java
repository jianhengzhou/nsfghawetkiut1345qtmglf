package com.gdestiny.github.adapter;

import java.util.List;

import org.eclipse.egit.github.core.RepositoryCommit;

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
import com.gdestiny.github.utils.client.CommitUtils;

public class CommitDialogAdapter extends BaseAdapter {

	private Context context;
	private List<RepositoryCommit> datas;

	public CommitDialogAdapter(Context context) {
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
					R.layout.item_commit_dialog, null);
			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		RepositoryCommit repositoryCommit = datas.get(position);

		String url = CommitUtils.getAuthorAvatarUrl(repositoryCommit);
		ImageLoaderUtils.displayImage(url, holder.icon,
				R.drawable.default_avatar, R.drawable.default_avatar, true);

		holder.name.setText(CommitUtils.getAuthor(repositoryCommit));
		holder.date.setText(TimeUtils.getTime(CommitUtils.getAuthorDate(
				repositoryCommit).getTime()));
		holder.content.setText(repositoryCommit.getCommit().getMessage());
		holder.comment.setText(repositoryCommit.getCommit().getCommentCount()
				+ "");
		holder.sha.setText(CommitUtils.getSubSha(repositoryCommit));
		return convertView;
	}

	public List<RepositoryCommit> getDatas() {
		return datas;
	}

	public void setDatas(List<RepositoryCommit> datas) {
		this.datas = datas;
		notifyDataSetChanged();
	}

	private class Holder {
		ImageView icon;
		TextView content;
		TextView name;
		TextView date;
		TextView comment;
		TextView sha;

		public Holder(View v) {
			icon = (ImageView) v.findViewById(R.id.icon);
			content = (TextView) v.findViewById(R.id.content);
			name = (TextView) v.findViewById(R.id.name);
			date = (TextView) v.findViewById(R.id.date);
			comment = (TextView) v.findViewById(R.id.comment);
			sha = (TextView) v.findViewById(R.id.sha);
		}
	}
}
