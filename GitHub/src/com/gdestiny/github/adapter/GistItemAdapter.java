package com.gdestiny.github.adapter;

import java.util.List;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.GistFile;

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
import com.gdestiny.github.utils.CommonUtils;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.TimeUtils;
import com.gdestiny.github.utils.ViewUtils;

public class GistItemAdapter extends BaseAdapter {

	private Context context;
	private List<Comment> datas;
	private List<GistFile> files;

	private OnListener listener;
	private boolean isCollaborator;

	public boolean isCollaborator() {
		return isCollaborator;
	}

	public void setIsCollaborator(boolean isCollaborator) {
		this.isCollaborator = isCollaborator;
	}

	public List<GistFile> getFiles() {
		return files;
	}

	public int getFilesSize() {
		if (files != null)
			return files.size();
		return 0;
	}

	public GistFile getFile(int position) {
		if (files != null && position < files.size())
			return files.get(position);
		return null;
	}

	public void setFiles(List<GistFile> files) {
		this.files = files;
	}

	public GistItemAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		int size = 0;
		if (datas != null)
			size += datas.size();
		if (files != null)
			size += files.size();
		return size;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_gist_item, null);
			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		int fileSize = 0;
		if (files != null)
			fileSize = files.size();
		if (position < fileSize) {
			holder.bindFile(files.get(position));
		} else {
			holder.bindComment(position - fileSize,
					datas.get(position - fileSize));
		}
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
		int fileSize = 0;
		if (files != null)
			fileSize = files.size();
		return position < fileSize;
	}

	private class Holder {
		ImageView icon;
		TextView content;
		TextView name;
		TextView date;
		View edit;
		View delete;

		View btnLayout;

		// file
		TextView fileName;
		TextView size;
		ImageView fileIcon;
		ImageView forward;

		// layout
		View fileLayout, commentLayout;

		public Holder(View v) {
			icon = (ImageView) v.findViewById(R.id.icon);
			content = (TextView) v.findViewById(R.id.content);
			name = (TextView) v.findViewById(R.id.name);
			date = (TextView) v.findViewById(R.id.date);
			edit = v.findViewById(R.id.edit);
			delete = v.findViewById(R.id.delete);

			btnLayout = v.findViewById(R.id.comment_btn);
			// file
			fileName = (TextView) v.findViewById(R.id.file_name);
			size = (TextView) v.findViewById(R.id.size);
			fileIcon = (ImageView) v.findViewById(R.id.file_icon);
			forward = (ImageView) v.findViewById(R.id.icon_forword);
			ViewUtils.setVisibility(forward, View.GONE);

			fileLayout = v.findViewById(R.id.file_layout);
			commentLayout = v.findViewById(R.id.comment_layout);
		}

		public void bindFile(GistFile file) {
			ViewUtils.setVisibility(fileLayout, View.VISIBLE);
			ViewUtils.setVisibility(commentLayout, View.GONE);
			fileName.setText(file.getFilename());
			fileIcon.setImageResource(R.drawable.common_file_grey);
			size.setText(CommonUtils.sizeToSuitable(file.getSize()));
		}

		public void bindComment(final int position, final Comment comment) {
			ViewUtils.setVisibility(fileLayout, View.GONE);
			ViewUtils.setVisibility(commentLayout, View.VISIBLE);
			ImageLoaderUtils.displayImage(comment.getUser().getAvatarUrl(),
					icon, R.drawable.default_avatar, R.drawable.default_avatar,
					true);

			String name = comment.getUser().getLogin();
			if (isCollaborator || CommonUtils.isAuthUser(name)) {
				ViewUtils.setVisibility(btnLayout, View.VISIBLE);
			} else {
				ViewUtils.setVisibility(btnLayout, View.GONE);
			}
			edit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (listener != null) {
						listener.onEdit(position, comment);
					}
				}
			});
			delete.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (listener != null) {
						listener.onDelete(comment);
					}
				}
			});

			this.name.setText(name);
			content.setText(Html.fromHtml(comment.getBodyHtml(),
					new AsyncImageGetter(context, content), null));
			ViewUtils.handleLink(content);

			date.setText(TimeUtils.getTime(comment.getCreatedAt().getTime()));
		}
	}

	public OnListener getOnListener() {
		return listener;
	}

	public void setOnListener(OnListener listener) {
		this.listener = listener;
	}

	public interface OnListener {
		public void onEdit(int position, Comment comment);

		public void onDelete(Comment comment);
	}
}
