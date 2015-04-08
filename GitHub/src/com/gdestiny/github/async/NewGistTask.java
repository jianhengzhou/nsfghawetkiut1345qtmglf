package com.gdestiny.github.async;

import java.util.Map;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;

import android.content.Context;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.utils.ToastUtils;

public class NewGistTask extends DialogTask<Void, Gist> {

	private boolean isPublic;
	private String description;
	private Map<String, GistFile> files;

	public NewGistTask(Context context, boolean isPublic, String description,
			Map<String, GistFile> files) {
		super(context);
		this.isPublic = isPublic;
		this.description = description;
		this.files = files;
		this.setLoadingMessage(R.string.newing_gist);
	}

	@Override
	public Gist onBackground(Void params) throws Exception {
		Gist gist = new Gist().setPublic(isPublic).setDescription(description)
				.setFiles(files);
		return GitHubConsole.getInstance().createGist(gist);
	}

	@Override
	public void onSuccess(Gist result) {
		ToastUtils.show(context, R.string.new_gist_succeed);
	}

}
