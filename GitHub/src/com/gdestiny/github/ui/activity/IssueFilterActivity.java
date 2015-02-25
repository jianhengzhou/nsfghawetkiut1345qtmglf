package com.gdestiny.github.ui.activity;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gdestiny.github.R;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.async.LabelLoadTask;

public class IssueFilterActivity extends BaseFragmentActivity implements
		OnClickListener {

	@SuppressWarnings("unused")
	private Issue issue;
	private Repository repository;

	private RadioGroup stateGroup;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.act_issue_filter);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		stateGroup = (RadioGroup) findViewById(R.id.state_group);
		RadioButton rb = (RadioButton) stateGroup.getChildAt(0);
		rb.setChecked(true);

		stateGroup.check(R.id.state_open);

		findViewById(R.id.assign_layout).setOnClickListener(this);
		findViewById(R.id.milestone_layout).setOnClickListener(this);
		findViewById(R.id.label_layout).setOnClickListener(this);
	}

	@Override
	protected void initData() {
		getTitlebar().setLeftLayout(null, "issue filter", null);
		repository = (Repository) getIntent().getSerializableExtra(
				RepositoryDetailActivity.EXTRA_REPOSITORY);
	}

	@Override
	protected void onleftLayout() {
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.assign_layout:

			break;
		case R.id.milestone_layout:

			break;
		case R.id.label_layout:
			new LabelLoadTask(context, repository).execute(GitHubApplication
					.getClient());
			break;
		default:
			break;
		}
	}

}
