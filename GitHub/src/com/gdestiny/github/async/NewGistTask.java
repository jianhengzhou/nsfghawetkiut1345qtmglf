package com.gdestiny.github.async;

import java.util.Map;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.GistService;

import android.content.Context;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.ToastUtils;

public class NewGistTask extends DialogTask<GitHubClient, Gist> {

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
	public Gist onBackground(GitHubClient params) throws Exception {
		GistService service = new GistService(params);

		Gist gist = new Gist().setPublic(isPublic).setDescription(description)
				.setFiles(files);
		return service.createGist(gist);
	}

	@Override
	public void onSuccess(Gist result) {
		ToastUtils.show(context, R.string.new_gist_succeed);
	}

}
