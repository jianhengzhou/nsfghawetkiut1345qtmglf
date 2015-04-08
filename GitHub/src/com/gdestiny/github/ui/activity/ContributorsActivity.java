package com.gdestiny.github.ui.activity;

import java.util.List;

import org.eclipse.egit.github.core.Contributor;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseLoadFragmentActivity;
import com.gdestiny.github.adapter.ContributorsAdapter;
import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.utils.CommonUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.IntentUtils;

public class ContributorsActivity extends
		BaseLoadFragmentActivity<Void, List<Contributor>> {

	private Repository repository;
	private ListView contributorList;
	private ContributorsAdapter contributorsAdapter;

	@Override
	public void onRefresh() {
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
		repository = (Repository) getIntent().getSerializableExtra(
				Constants.Extra.REPOSITORY);

		getTitlebar().hideRight();
		getTitlebar().setLeftLayout(repository.getOwner().getAvatarUrl(),
				repository.generateId());

		contributorList = (ListView) findViewById(R.id.list);
		contributorsAdapter = new ContributorsAdapter(context);
		contributorList.setAdapter(contributorsAdapter);
		contributorList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Contributor c = contributorsAdapter.getDatas().get(position);
				User user = new User().setAvatarUrl(c.getAvatarUrl())
						.setLogin(c.getLogin()).setName(c.getName());
				if (CommonUtils.isAuthUser(user))
					return;
				IntentUtils.create(context, UserNavigationActivity.class)
						.putExtra(Constants.Extra.USER, user).start();
			}
		});

		execute();
	}

	@Override
	public List<Contributor> onBackground(Void params) throws Exception {
		return GitHubConsole.getInstance().getContributor(repository);
	}

	@Override
	public void onSuccess(List<Contributor> result) {
		super.onSuccess(result);
		contributorsAdapter.setDatas(result);
	}

	@Override
	protected void onleftLayout() {
		finish();
	}

}
