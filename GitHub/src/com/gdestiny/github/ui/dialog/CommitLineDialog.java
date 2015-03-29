package com.gdestiny.github.ui.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.bean.CommitLine;
import com.gdestiny.github.utils.CommonUtils;

public abstract class CommitLineDialog implements View.OnClickListener {

	private MaterialDialog dialog;
	private Context context;

	public CommitLineDialog(Context context, String path, CommitLine line) {
		this.context = context;
		dialog = new MaterialDialog(context);
		dialog.setTitle(CommonUtils.pathToName(path));
		dialog.setCanceledOnTouchOutside(true);

		View view = LayoutInflater.from(context).inflate(
				R.layout.layout_commitline_dialog, null);
		dialog.setContentView(view);
		view.findViewById(R.id.item_one).setOnClickListener(this);
		view.findViewById(R.id.item_two).setOnClickListener(this);
		bindLine(line, view);
	}

	private void bindLine(CommitLine line, View v) {
		TextView lineTv = (TextView) v.findViewById(R.id.line);
		TextView oldLine = (TextView) v.findViewById(R.id.old_line);
		TextView newLine = (TextView) v.findViewById(R.id.new_line);
		View lineLayout = v.findViewById(R.id.line_layout);

		String lineStr = line.getLine();
		lineStr = lineStr.charAt(0) + lineStr.substring(1).trim();
		lineTv.setText(lineStr);
		oldLine.setText(line.getOldLine(1));
		newLine.setText(line.getNewLine(1));

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
		lineTv.setTextColor(textcolor);
		lineLayout.setBackgroundColor(bkcolor);

		oldLine.setTextColor(lineTextColor);
		newLine.setTextColor(lineTextColor);
	}

	public void show() {
		dialog.show();
	}

	protected abstract void onItemClick(int position);

	@Override
	public void onClick(View v) {
		dialog.dismiss();
		switch (v.getId()) {
		case R.id.item_one:
			onItemClick(0);
			break;
		case R.id.item_two:
			onItemClick(1);
			break;
		default:
			break;
		}
	}
}
