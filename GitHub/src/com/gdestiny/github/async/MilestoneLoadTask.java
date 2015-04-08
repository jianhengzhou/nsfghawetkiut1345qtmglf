package com.gdestiny.github.async;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.egit.github.core.Milestone;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.IssueService;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.adapter.MilestoneAdapter;
import com.gdestiny.github.bean.comparator.MilestoneComparator;
import com.gdestiny.github.ui.dialog.MaterialDialog;

public class MilestoneLoadTask extends DialogTask<Void, List<Milestone>> {

	private Repository repository;
	private Context context;
	private Milestone selected;

	public MilestoneLoadTask(Context context, Repository repository) {
		super(context);
		this.repository = repository;
		this.context = context;
	}

	public MilestoneLoadTask(Context context, Repository repository,
			Milestone selected) {
		super(context);
		this.repository = repository;
		this.context = context;
		this.selected = selected;
	}

	public MilestoneLoadTask putSelected(Milestone selected) {
		this.selected = selected;
		return this;
	}

	@Override
	public List<Milestone> onBackground(Void params) throws Exception {
		List<Milestone> result = new ArrayList<Milestone>();
		result.addAll(GitHubConsole.getInstance().getMilestones(repository,
				IssueService.STATE_OPEN));
		result.addAll(GitHubConsole.getInstance().getMilestones(repository,
				IssueService.STATE_CLOSED));
		Collections.sort(result, new MilestoneComparator());
		return result;
	}

	@Override
	public void onSuccess(List<Milestone> result) {
		// TODO Auto-generated method stub

		final MaterialDialog dialog = new MaterialDialog(context);
		dialog.setTitle("Milestones").setCanceledOnTouchOutside(true);
		if (result == null || result.isEmpty()) {
			dialog.setMessage("No Milestones In This Repository!");
			dialog.show();
			return;
		}

		final MilestoneAdapter adapter = new MilestoneAdapter(context, result)
				.setSelectedMilestone(selected);
		dialog.initListView();

		dialog.getmListView().setAdapter(adapter);

		dialog.getmListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onMilestone(adapter.getMilestones().get(position));
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public void onMilestone(Milestone selected) {

	}
}
