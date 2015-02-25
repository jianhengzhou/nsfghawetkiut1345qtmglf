package com.gdestiny.github.async;

import java.util.List;

import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.LabelService;

import com.gdestiny.github.adapter.LabelAdapter;
import com.gdestiny.github.ui.dialog.MaterialDialog;

import android.content.Context;
import android.view.View;

public class LabelLoadTask extends DialogTask<GitHubClient, List<Label>> {

	private Repository repository;
	private Context context;

	public LabelLoadTask(Context context, Repository repository) {
		super(context);
		this.repository = repository;
		this.context = context;

	}

	@Override
	public List<Label> onBackground(GitHubClient params) throws Exception {
		// TODO Auto-generated method stub
		LabelService service = new LabelService(params);
		return service.getLabels(repository);
	}

	@Override
	public void onSuccess(List<Label> result) {
		// TODO Auto-generated method stub
		MaterialDialog dialog = new MaterialDialog(context).setTitle("Labels")
				.setPositiveButton("ok", new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

					}
				}).setNegativeButton("cancle", new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

					}
				});

		LabelAdapter adapter = new LabelAdapter(context, result);
		dialog.initListView();
		dialog.getmListView().setAdapter(adapter);
		dialog.show();
	}

}
