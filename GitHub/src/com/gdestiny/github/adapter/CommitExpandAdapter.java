package com.gdestiny.github.adapter;

import java.io.Serializable;

import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.CommitFile;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.async.AsyncImageGetter;
import com.gdestiny.github.bean.CommitLine;
import com.gdestiny.github.bean.CommitTree;
import com.gdestiny.github.utils.CommonUtils;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.TimeUtils;
import com.gdestiny.github.utils.ViewUtils;

public class CommitExpandAdapter extends BaseExpandableListAdapter {

	private Context context;
	private CommitTree commitTree;

	private OnCommitCommentListener listener;
	private boolean isCollaborator;

	public boolean isCollaborator() {
		return isCollaborator;
	}

	public CommitExpandAdapter setIsCollaborator(boolean isCollaborator) {
		this.isCollaborator = isCollaborator;
		return this;
	}

	public CommitTree getCommitTree() {
		return commitTree;
	}

	public CommitExpandAdapter setCommitTree(CommitTree commitTree) {
		this.commitTree = commitTree;
		notifyDataSetChanged();
		return this;
	}

	public CommitExpandAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getGroupCount() {
		if (commitTree == null)
			return 0;
		return commitTree.getGroupCount();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (commitTree == null)
			return 0;
		return commitTree.getChildCount(groupPosition);
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		GroupHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_commit_group, null);
			holder = new GroupHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (GroupHolder) convertView.getTag();
		}
		CommitFile commitFile = commitTree.getCommitFile(groupPosition);
		String path = commitFile.getFilename();

		holder.fileName.setText(CommonUtils.pathToName(path));
		holder.status.setText(commitFile.getStatus());
		String filepath = CommonUtils.getPath(path);
		if (TextUtils.isEmpty(filepath))
			ViewUtils.setVisibility(holder.filePath, View.GONE);
		else {
			ViewUtils.setVisibility(holder.filePath, View.VISIBLE);
			holder.filePath.setText(path);
		}
		holder.addition.setText("+" + commitFile.getAdditions());
		holder.deletion.setText("-" + commitFile.getDeletions());
		if (isExpanded) {
			holder.icon.setImageResource(R.drawable.common_triangle_down);
		} else {
			holder.icon.setImageResource(R.drawable.common_triangle_right);
		}

		convertView.setTag(R.id.tag_group, groupPosition);
		convertView.setTag(R.id.tag_child, -1);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChildHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_commit_child, null);
			holder = new ChildHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ChildHolder) convertView.getTag();
		}

		Serializable obj = commitTree.getChild(groupPosition, childPosition);
		if (obj instanceof CommitLine) {
			CommitLine line = (CommitLine) obj;
			int maxDigit = commitTree.getMaxDigit(groupPosition);
			holder.bindFile(line, maxDigit);
		} else if (obj instanceof CommitComment) {
			CommitComment cc = (CommitComment) obj;
			holder.bindComment(cc, childPosition);
		}

		convertView.setTag(R.id.tag_group, groupPosition);
		convertView.setTag(R.id.tag_child, childPosition);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		Serializable obj = commitTree.getChild(groupPosition, childPosition);
		if (obj instanceof CommitLine) {
			CommitLine line = (CommitLine) obj;
			return !line.isMark();
		}
		return false;
	}

	private class GroupHolder {
		TextView fileName;
		TextView filePath;
		TextView addition;
		TextView deletion;
		ImageView icon;
		TextView status;

		public GroupHolder(View v) {
			fileName = (TextView) v.findViewById(R.id.file_name);
			filePath = (TextView) v.findViewById(R.id.file_path);
			addition = (TextView) v.findViewById(R.id.file_addition);
			deletion = (TextView) v.findViewById(R.id.file_deletion);
			status = (TextView) v.findViewById(R.id.file_status);
			icon = (ImageView) v.findViewById(R.id.icon);
		}
	}

	private class ChildHolder {

		View lineLayout;
		View commentLayout;

		TextView line;
		TextView oldLine;
		TextView newLine;
		// comment
		ImageView icon;
		TextView content;
		TextView name;
		TextView date;
		View edit;
		View delete;
		View btnLayout;

		public ChildHolder(View v) {
			line = (TextView) v.findViewById(R.id.line);
			oldLine = (TextView) v.findViewById(R.id.old_line);
			newLine = (TextView) v.findViewById(R.id.new_line);
			lineLayout = v.findViewById(R.id.line_layout);
			commentLayout = v.findViewById(R.id.comment_layout);
			// comment
			icon = (ImageView) v.findViewById(R.id.icon);
			content = (TextView) v.findViewById(R.id.content);
			name = (TextView) v.findViewById(R.id.name);
			date = (TextView) v.findViewById(R.id.date);
			edit = v.findViewById(R.id.edit);
			delete = v.findViewById(R.id.delete);
			btnLayout = v.findViewById(R.id.comment_btn);
		}

		public void bindFile(CommitLine line, int maxDigit) {
			ViewUtils.setVisibility(lineLayout, View.VISIBLE);
			ViewUtils.setVisibility(commentLayout, View.GONE);

			this.line.setText(line.getLine());
			this.oldLine.setText(line.getOldLine(maxDigit));
			this.newLine.setText(line.getNewLine(maxDigit));

			Resources res = context.getResources();
			int bkcolor = res.getColor(R.color.transparent);
			int textcolor = res.getColor(R.color.common_light_black);
			int lineTextColor = res.getColor(R.color.common_icon_grey);
			if (line.isAddition()) {
				bkcolor = res.getColor(R.color.light_addition);
			} else if (line.isDeletion()) {
				bkcolor = res.getColor(R.color.light_deletion);
			} else if (line.isMark()) {
				lineTextColor = res.getColor(R.color.white);
				bkcolor = res.getColor(R.color.common_icon_grey);
				textcolor = res.getColor(R.color.white);
			}
			this.line.setTextColor(textcolor);
			this.lineLayout.setBackgroundColor(bkcolor);

			this.oldLine.setTextColor(lineTextColor);
			this.newLine.setTextColor(lineTextColor);
		}

		public void bindComment(final CommitComment comment,
				final int childPosition) {
			ViewUtils.setVisibility(lineLayout, View.GONE);
			ViewUtils.setVisibility(commentLayout, View.VISIBLE);

			ImageLoaderUtils.displayImage(comment.getUser().getAvatarUrl(),
					this.icon, R.drawable.default_avatar,
					R.drawable.default_avatar, true);

			String name = comment.getUser().getLogin();
			if (isCollaborator || CommonUtils.isAuthUser(name)) {
				ViewUtils.setVisibility(this.btnLayout, View.VISIBLE);
			} else {
				ViewUtils.setVisibility(this.btnLayout, View.GONE);
			}
			this.edit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (listener != null) {
						listener.onEdit(childPosition, comment);
					}
				}
			});
			this.delete.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (listener != null) {
						listener.onDelete(comment);
					}
				}
			});

			this.name.setText(name);
			this.content.setText(Html.fromHtml(comment.getBodyHtml(),
					new AsyncImageGetter(context, this.content), null));
			ViewUtils.handleLink(this.content);

			this.date.setText(TimeUtils.getTime(comment.getCreatedAt()
					.getTime()));
		}
	}

	public OnCommitCommentListener getOnListener() {
		return listener;
	}

	public void setOnListener(OnCommitCommentListener listener) {
		this.listener = listener;
	}

	public interface OnCommitCommentListener {
		public void onEdit(int position, CommitComment comment);

		public void onDelete(CommitComment comment);
	}
}
