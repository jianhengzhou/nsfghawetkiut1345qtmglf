package com.gdestiny.github.async;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.MarkdownService;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.ui.dialog.MaterialDialog;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;

public abstract class PreviewTask extends DialogTask<GitHubClient, String> {

	private Repository repository;
	private String raw;

	public PreviewTask(Context context, String raw) {
		super(context);
		this.raw = raw;
		this.setLoadingMessage(R.string.loading_preview);
	}

	public PreviewTask(Context context, Repository repository, String raw) {
		super(context);
		this.repository = repository;
		this.raw = raw;
		this.setLoadingMessage(R.string.loading_preview);
	}

	@Override
	public String onBackground(GitHubClient params) throws Exception {
		// TODO Auto-generated method stub
		MarkdownService mdSerview = new MarkdownService(params);
		if (!TextUtils.isEmpty(raw)) {
			if (repository != null) {
				return mdSerview.getRepositoryHtml(repository, raw);
			}
			return mdSerview.getHtml(raw, MarkdownService.MODE_GFM);

		}
		return "";
	}

	@Override
	public void onSuccess(String result) {
		if (TextUtils.isEmpty(result))
			result = "Nothing to preview;";
		final MaterialDialog dialog = new MaterialDialog(context).setTitle(
				"Preview").setMessage(Html.fromHtml(result));
		dialog.setPositiveButton("send", new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				onSend();
			}
		}).setNegativeButton("cancle", new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public abstract void onSend();
}
