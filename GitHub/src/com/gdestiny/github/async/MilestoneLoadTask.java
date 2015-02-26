package com.gdestiny.github.async;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.egit.github.core.Milestone;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.MilestoneService;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.gdestiny.github.adapter.MilestoneAdapter;
import com.gdestiny.github.bean.comparator.MilestoneComparator;
import com.gdestiny.github.ui.dialog.MaterialDialog;

public class MilestoneLoadTask extends
		DialogTask<GitHubClient, List<Milestone>> {

	private Repository repository;
	private Context context;

	public MilestoneLoadTask(Context context, Repository repository) {
		super(context);
		this.repository = repository;
		this.context = context;

	}

	@Override
	public List<Milestone> onBackground(GitHubClient params) throws Exception {
		// TODO Auto-generated method stub
		MilestoneService service = new MilestoneService(params);
		List<Milestone> result = new ArrayList<Milestone>();
		result.addAll(service
				.getMilestones(repository, IssueService.STATE_OPEN));
		result.addAll(service.getMilestones(repository,
				IssueService.STATE_CLOSED));
		Collections.sort(result, new MilestoneComparator());
		return result;
	}

	@Override
	public void onSuccess(List<Milestone> result) {
		// TODO Auto-generated method stub

		final MaterialDialog dialog = new MaterialDialog(context);
		final MilestoneAdapter adapter = new MilestoneAdapter(context, result);

		dialog.setTitle("Milestones").initListView();

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
