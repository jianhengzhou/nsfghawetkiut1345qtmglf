package com.gdestiny.github.async;

import java.util.List;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CollaboratorService;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.gdestiny.github.ui.dialog.MaterialDialog;

public class CollaboratorLoadTask extends DialogTask<GitHubClient, List<User>> {

	private Repository repository;
	private Context context;
	private User selected;

	public CollaboratorLoadTask(Context context, Repository repository) {
		super(context);
		this.repository = repository;
		this.context = context;
	}

	public CollaboratorLoadTask(Context context, Repository repository,
			User selected) {
		super(context);
		this.repository = repository;
		this.context = context;
		this.selected = selected;
	}

	public CollaboratorLoadTask putSelected(User selected) {
		this.selected = selected;
		return this;
	}

	@Override
	public List<User> onBackground(GitHubClient params) throws Exception {
		// TODO Auto-generated method stub
		CollaboratorService service = new CollaboratorService(params);
		List<User> result = service.getCollaborators(repository);
		return result;
	}

	@Override
	public void onSuccess(List<User> result) {
		// TODO Auto-generated method stub

		final MaterialDialog dialog = new MaterialDialog(context);
		dialog.setTitle("Collaborators").setCanceledOnTouchOutside(true);
		if (result == null || result.isEmpty()) {
			dialog.setMessage("No Datas In This Repository!");
			dialog.show();
			return;
		}

		dialog.initListView();

		for (User u : result) {
			dialog.addItem(u.getAvatarUrl(), u.getLogin());
		}

		dialog.getmListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public void onCollaborator(User selected) {

	}
}
