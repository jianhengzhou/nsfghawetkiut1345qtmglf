package com.gdestiny.github.async;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.util.LabelComparator;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.adapter.LabelAdapter;
import com.gdestiny.github.ui.dialog.MaterialDialog;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;

public class LabelLoadTask extends DialogTask<Void, List<Label>> {

	private Repository repository;
	private Context context;

	private ArrayList<Label> selected;

	public LabelLoadTask(Context context, Repository repository) {
		super(context);
		this.repository = repository;
		this.context = context;

	}

	public LabelLoadTask(Context context, Repository repository,
			ArrayList<Label> selected) {
		super(context);
		this.repository = repository;
		this.context = context;
		this.selected = selected;
	}

	public LabelLoadTask putSelected(ArrayList<Label> selected) {
		this.selected = selected;
		return this;
	}

	@Override
	public List<Label> onBackground(Void params) throws Exception {
		List<Label> result = GitHubConsole.getInstance().getLabels(repository);
		Collections.sort(result, new LabelComparator());
		return result;
	}

	@Override
	public void onSuccess(List<Label> result) {

		final MaterialDialog dialog = new MaterialDialog(context).setTitle(
				"Labels").setCanceledOnTouchOutside(true);
		if (result == null || result.isEmpty()) {
			dialog.setMessage("No Labels In This Repository!");
			dialog.show();
			return;
		}

		final LabelAdapter adapter = new LabelAdapter(context, result)
				.setSelectedLabel(selected);
		dialog.setPositiveButton("ok", new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<Label> ls = adapter.getSelectedLabel();
				Collections.sort(ls, new LabelComparator());
				onLabels(ls);
				dialog.dismiss();
			}
		}).setNegativeButton("cancle", new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.initListView();
		dialog.getmListView().setAdapter(adapter);
		dialog.getmListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox);
				cb.setChecked(!cb.isChecked());
			}
		});
		dialog.show();
	}

	public void onLabels(ArrayList<Label> selected) {

	}
}
