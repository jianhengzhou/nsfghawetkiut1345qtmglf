package com.gdestiny.github.adapter;

import java.util.List;

import org.eclipse.egit.github.core.Comment;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.async.AsyncImageGetter;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.TimeUtils;
import com.gdestiny.github.utils.ViewUtils;

public class CommentAdapter extends BaseAdapter {

	private Context context;
	private List<Comment> datas;

	public CommentAdapter(Context context) {
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
					R.layout.item_comment, null);
			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		Comment comment = datas.get(position);
		ImageLoaderUtils.displayImage(comment.getUser().getAvatarUrl(),
				holder.icon, R.drawable.default_avatar,
				R.drawable.default_avatar, true);
		holder.name.setText(comment.getUser().getLogin());
		holder.content.setText(Html.fromHtml(comment.getBodyHtml(),
				new AsyncImageGetter(context), null));
		ViewUtils.handleLink(holder.content);

		holder.date
				.setText(TimeUtils.getTime(comment.getCreatedAt().getTime()));

		holder.edit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		holder.delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		return convertView;
	}

	public List<Comment> getDatas() {
		return datas;
	}

	public void setDatas(List<Comment> datas) {
		this.datas = datas;
		notifyDataSetChanged();
	}

	@Override
	public boolean isEnabled(int position) {
		// // TODO Auto-generated method stub
		// if (position == 0)
		// return false;
		return super.isEnabled(position);
	}

	private class Holder {
		ImageView icon;
		TextView content;
		TextView name;
		TextView date;
		View edit;
		View delete;

		public Holder(View v) {
			icon = (ImageView) v.findViewById(R.id.icon);
			content = (TextView) v.findViewById(R.id.content);
			name = (TextView) v.findViewById(R.id.name);
			date = (TextView) v.findViewById(R.id.date);
			edit = v.findViewById(R.id.edit);
			delete = v.findViewById(R.id.delete);
		}
	}
}
