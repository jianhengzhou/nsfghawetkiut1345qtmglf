package com.gdestiny.github.adapter;

import org.eclipse.egit.github.core.CommitFile;

import com.gdestiny.github.R;
import com.gdestiny.github.bean.CommitTree;
import com.gdestiny.github.utils.CommonUtils;
import com.gdestiny.github.utils.ViewUtils;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class CommitExpandAdapter extends BaseExpandableListAdapter {

	private Context context;
	private CommitTree commitTree;

	public CommitTree getCommitTree() {
		return commitTree;
	}

	public void setCommitTree(CommitTree commitTree) {
		this.commitTree = commitTree;
		notifyDataSetChanged();
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
		String filepath = CommonUtils.getPath(path);
		if (TextUtils.isEmpty(filepath))
			ViewUtils.setVisibility(holder.filePath, View.GONE);
		else {
			ViewUtils.setVisibility(holder.filePath, View.VISIBLE);
			holder.filePath.setText(path);
		}
		holder.addition.setText("+" + commitFile.getAdditions());
		holder.deletion.setText("-" + commitFile.getDeletions());

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

		String line = commitTree.getLines(groupPosition, childPosition);
		holder.line.setText(line);
		if (line.startsWith("+")) {
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.addition));
		} else if (line.startsWith("-")) {
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.deletion));
		} else if (line.startsWith("@")) {
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.common_listitem_blue));
		} else {
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.transparent));
		}

		convertView.setTag(R.id.tag_group, groupPosition);
		convertView.setTag(R.id.tag_child, childPosition);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	private class GroupHolder {
		TextView fileName;
		TextView filePath;
		TextView addition;
		TextView deletion;

		public GroupHolder(View v) {
			fileName = (TextView) v.findViewById(R.id.file_name);
			filePath = (TextView) v.findViewById(R.id.file_path);
			addition = (TextView) v.findViewById(R.id.file_addition);
			deletion = (TextView) v.findViewById(R.id.file_deletion);
		}
	}

	private class ChildHolder {
		TextView line;

		public ChildHolder(View v) {
			line = (TextView) v.findViewById(R.id.line);
		}
	}
}
