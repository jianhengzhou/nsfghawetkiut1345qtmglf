package com.gdestiny.github.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.bean.CodeTree;
import com.gdestiny.github.utils.CommonUtils;
import com.gdestiny.github.utils.ViewUtils;

public class CodeTreeAdapter extends BaseAdapter {

	private CodeTree codeTree;
	private Context context;
	private int treeCount;

	public CodeTreeAdapter(Context context) {
		this.context = context;
		treeCount = 0;
		codeTree = null;
	}

	public CodeTreeAdapter(Context context, CodeTree codeTree) {
		this.codeTree = codeTree;
		this.context = context;
		this.treeCount = codeTree.getTreeCount();
	}

	@Override
	public int getCount() {
		if (codeTree == null)
			return 0;
		return codeTree.getCount();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
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
					R.layout.item_code_tree, null);
			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if (position < treeCount) {
			holder.name.setText(codeTree.subTree.get(position).name);
			holder.icon.setImageResource(R.drawable.common_folder_grey);
			ViewUtils.setVisibility(holder.forward, View.VISIBLE);
			ViewUtils.setVisibility(holder.size, View.GONE);
		} else {
			holder.name.setText(CommonUtils.pathToName(codeTree.subEntry.get(
					position - treeCount).getPath()));
			holder.icon.setImageResource(R.drawable.common_file_grey);
			ViewUtils.setVisibility(holder.forward, View.GONE);
			ViewUtils.setVisibility(holder.size, View.VISIBLE);
			holder.size.setText(CommonUtils.sizeToSuitable(codeTree.subEntry
					.get(position - treeCount).getSize()));
		}
		return convertView;
	}

	private class Holder {
		public TextView name;
		public TextView size;
		public ImageView icon;
		public ImageView forward;

		public Holder(View v) {
			name = (TextView) v.findViewById(R.id.file_name);
			size = (TextView) v.findViewById(R.id.size);
			icon = (ImageView) v.findViewById(R.id.file_icon);
			forward = (ImageView) v.findViewById(R.id.icon_forword);
		}
	}

	public CodeTree getCodeTree() {
		return codeTree;
	}

	public void setCodeTree(CodeTree codeTree) {
		this.codeTree = codeTree;
		this.treeCount = codeTree.getTreeCount();
		notifyDataSetChanged();
	}

}
