package com.gdestiny.github.ui.activity;

import java.io.File;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;

import com.gdestiny.github.R;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.async.NewEditIssueTask;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.IntentUtils;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class NewEditIssueActivity extends BaseFragmentActivity {

	private Repository repository;
	private EditText title;
	private EditText content;

	private Issue issue;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.act_new_edit_issue);
	}

	@Override
	protected void initView() {
		title = (EditText) findViewById(R.id.issue_title);
		content = (EditText) findViewById(R.id.issue_content);
	}

	@Override
	protected void initActionBar(TitleBar titleBar) {
		// TODO Auto-generated method stub
		super.initActionBar(titleBar);
		titleBar.showRightBtn();
		ImageButton right = titleBar.getRightBtn();
		right.setImageResource(R.drawable.common_btn_ok);
		right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (issue == null || issue.getNumber() == 0) {
					newIssue();
					new NewEditIssueTask(context, repository, issue) {

						@Override
						public void onSuccess(Issue result) {
							super.onSuccess(result);
							issue = result;
							IntentUtils.create(context)
									.putExtra(Constants.Extra.ISSUE, result)
									.setResultOk().finish();
						}

					}.execute(GitHubApplication.getClient());
				}
			}
		});
	}

	private void newIssue() {
		if (issue == null)
			issue = new Issue();
		issue.setTitle(title.getText().toString());
		issue.setBody(content.getText().toString() + "in my githun");
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		repository = (Repository) getIntent().getSerializableExtra(
				Constants.Extra.REPOSITORY);

		getTitlebar().setLeftLayout(
				repository.getOwner().getAvatarUrl(),
				"New Issue",
				repository.getOwner().getLogin() + File.separator
						+ repository.getName());
	}

	@Override
	protected void onleftLayout() {
		IntentUtils.setResultCancle(context);
	}

}
