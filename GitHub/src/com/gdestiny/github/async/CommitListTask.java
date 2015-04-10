package com.gdestiny.github.async;

import java.util.List;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.RepositoryCommit;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.adapter.CommitDialogAdapter;
import com.gdestiny.github.ui.dialog.MaterialDialog;

public abstract class CommitListTask extends
		DialogTask<Void, List<RepositoryCommit>> {

	private String base;
	private String head;
	private IRepositoryIdProvider repository;

	public CommitListTask(Context context, IRepositoryIdProvider repository,
			String base, String head) {
		super(context);
		this.base = base;
		this.head = head;
		this.repository = repository;
		this.setLoadingMessage(R.string.loading);
	}

	@Override
	public List<RepositoryCommit> onBackground(Void params) throws Exception {
		return GitHubConsole.getInstance()
				.getCommitCompare(repository, base, head).getCommits();

	}

	@Override
	public void onSuccess(List<RepositoryCommit> result) {
		final MaterialDialog dialog = new MaterialDialog(context).setTitle(
				"RepositoryCommit").setCanceledOnTouchOutside(true);

		if (result == null || result.isEmpty()) {
			dialog.setMessage("No Datas");
			dialog.show();
			return;
		}

		final CommitDialogAdapter adapter = new CommitDialogAdapter(context);
		adapter.setDatas(result);

		dialog.initListView();
		dialog.getmListView().setAdapter(adapter);
		dialog.getmListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				dialog.dismiss();
				onRepositoryCommit(adapter.getDatas().get(position));
			}
		});
		dialog.show();
	}

	public abstract void onRepositoryCommit(RepositoryCommit commit);
}
