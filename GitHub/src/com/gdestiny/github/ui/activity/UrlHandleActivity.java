package com.gdestiny.github.ui.activity;

import java.util.List;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.RepositoryService;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseFragmentActivity;
import com.gdestiny.github.abstracts.async.BaseAsyncTask;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.ViewUtils;

public class UrlHandleActivity extends BaseFragmentActivity {

	private View noData;
	private View normalView;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_url_handle);
	}

	@Override
	protected void initView() {
		getSupportActionBar().hide();
		noData = findViewById(R.id.nodata);
		normalView = findViewById(R.id.normalview);
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		final Uri uri = intent.getData();
		
		new BaseAsyncTask<Void, Void, Repository>() {

			@Override
			protected Repository doInBackground(Void... params) {
				RepositoryService service = new RepositoryService(
						GitHubApplication.getClient());
				try {
					Repository repo = getRepository(uri);
					return service.getRepository(repo.getOwner().getLogin(),
							repo.getName());
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(Repository result) {
				super.onPostExecute(result);
				if (result != null) {
					ViewUtils.setVisibility(normalView, View.GONE);
					IntentUtils.start(context, RepositoryDetailActivity.class,
							Constants.Extra.REPOSITORY, result);
					finish();
				} else {
					ViewUtils.setVisibility(noData, View.VISIBLE);
				}
			}
		}.execute();
	}

	@Override
	protected void onleftLayout() {
		finish();
	}

	public static Repository getRepository(Uri uri) {
		List<String> segments = uri.getPathSegments();
		if (segments == null) {
			return null;
		}
		if (segments.size() < 2) {
			return null;
		}

		String repoOwner = segments.get(0);
		if (!isValidOwner(repoOwner)) {
			return null;
		}

		String repoName = segments.get(1);
		if (!isValidRepo(repoName)) {
			return null;
		}

		Repository repository = new Repository();
		repository.setName(repoName);
		repository.setOwner(new User().setLogin(repoOwner));
		return repository;
	}

	public static boolean isValidOwner(final String name) {
		if (TextUtils.isEmpty(name))
			return false;

		if ("about".equals(name) //
				|| "account".equals(name) //
				|| "admin".equals(name) //
				|| "api".equals(name) //
				|| "blog".equals(name) //
				|| "camo".equals(name) //
				|| "contact".equals(name) //
				|| "dashboard".equals(name) //
				|| "downloads".equals(name) //
				|| "edu".equals(name) //
				|| "explore".equals(name) //
				|| "features".equals(name) //
				|| "home".equals(name) //
				|| "inbox".equals(name) //
				|| "languages".equals(name) //
				|| "login".equals(name) //
				|| "logout".equals(name) //
				|| "new".equals(name) //
				|| "notifications".equals(name) //
				|| "organizations".equals(name) //
				|| "orgs".equals(name) //
				|| "repositories".equals(name) //
				|| "search".equals(name) //
				|| "security".equals(name) //
				|| "settings".equals(name) //
				|| "stars".equals(name) //
				|| "styleguide".equals(name) //
				|| "timeline".equals(name) //
				|| "training".equals(name) //
				|| "users".equals(name) //
				|| "watching".equals(name))
			return false;
		else
			return true;
	}

	/**
	 * Is the given repo name valid?
	 * 
	 * @param name
	 * @return true if valid, false otherwise
	 */
	public static boolean isValidRepo(final String name) {
		if (TextUtils.isEmpty(name))
			return false;

		if ("followers".equals(name) //
				|| "following".equals(name))
			return false;
		else
			return true;
	}
}
