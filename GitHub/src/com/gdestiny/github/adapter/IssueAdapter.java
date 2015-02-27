package com.gdestiny.github.adapter;

import java.util.List;

import org.eclipse.egit.github.core.Issue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.ui.view.LabelColorView;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.TimeUtils;

public class IssueAdapter extends BaseAdapter {

	private Context context;
	private List<Issue> datas;

	private boolean isOpen = true;

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public IssueAdapter(Context context) {
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
					R.layout.item_issue, null);
			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		Issue issue = datas.get(position);

		ImageLoaderUtils.displayImage(issue.getUser().getAvatarUrl(),
				holder.icon, R.drawable.default_avatar,
				R.drawable.default_avatar, true);

		holder.title.setText(issue.getTitle());
		holder.date.setText(TimeUtils.getTime(issue.getCreatedAt().getTime()));
		holder.number.setText(" " + issue.getNumber() + " ");
		if (isOpen) {
			holder.number.getPaint().setFlags(
					android.graphics.Paint.ANTI_ALIAS_FLAG);
		} else {
			holder.number.getPaint().setFlags(
					android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
							| android.graphics.Paint.ANTI_ALIAS_FLAG);
		}

		holder.name.setText(issue.getUser().getLogin());
		holder.comment.setText(issue.getComments() + "");
		// 遇到博客类issue，body过大时加载缓慢
		// String content = issue.getBody();
		// holder.content.setText(content.length() > 50 ? issue.getBody()
		// .substring(0, 50) + "...." : content);
		holder.colorView.setLabels(issue.getLabels());

		return convertView;
	}

	public List<Issue> getDatas() {
		return datas;
	}

	public void setDatas(List<Issue> datas) {
		this.datas = datas;
		notifyDataSetChanged();
	}

	private class Holder {
		TextView number;
		ImageView icon;
		TextView name;
		TextView title;
		TextView date;
		TextView comment;
		LabelColorView colorView;

		// TextView content;

		public Holder(View v) {
			icon = (ImageView) v.findViewById(R.id.icon);
			name = (TextView) v.findViewById(R.id.name);
			number = (TextView) v.findViewById(R.id.number);
			title = (TextView) v.findViewById(R.id.title);
			date = (TextView) v.findViewById(R.id.date);
			comment = (TextView) v.findViewById(R.id.comment);
			colorView = (LabelColorView) v.findViewById(R.id.color_view);
			// content = (TextView) v.findViewById(R.id.content);
		}
	}
}
