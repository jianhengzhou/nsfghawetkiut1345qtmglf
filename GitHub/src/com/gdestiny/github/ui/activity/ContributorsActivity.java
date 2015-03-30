package com.gdestiny.github.ui.activity;

import java.util.List;

import org.eclipse.egit.github.core.Contributor;
import org.eclipse.egit.github.core.Repository;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.ContributorsAdapter;
import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.ui.activity.abstracts.BaseLoadFragmentActivity;
import com.gdestiny.github.utils.Constants;

public class ContributorsActivity extends
		BaseLoadFragmentActivity<Void, List<Contributor>> {

	private Repository repository;
	private ListView contributorList;
	private ContributorsAdapter contributorsAdapter;

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		execute();
	}

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.act_contributors, R.id.pull_refresh_layout);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		repository = (Repository) getIntent().getSerializableExtra(
				Constants.Extra.REPOSITORY);

		getTitlebar().hideRight();
		getTitlebar().setLeftLayout(repository.getOwner().getAvatarUrl(),
				repository.generateId());

		contributorList = (ListView) findViewById(R.id.list);
		contributorsAdapter = new ContributorsAdapter(context);
		contributorList.setAdapter(contributorsAdapter);

		execute();
	}

	@Override
	public List<Contributor> onBackground(Void params) throws Exception {
		// TODO Auto-generated method stub
		return GitHubConsole.getInstance().getContributor(repository);
	}

	@Override
	public void onSuccess(List<Contributor> result) {
		// TODO Auto-generated method stub
		super.onSuccess(result);
		contributorsAdapter.setDatas(result);
	}

	@Override
	protected void onleftLayout() {
		// TODO Auto-generated method stub
		finish();
	}

}
