package com.gdestiny.github.ui.activity;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;

import com.gdestiny.github.R;
import com.gdestiny.github.R.id;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class NewIssueActivity extends BaseFragmentActivity {

	@SuppressWarnings("unused")
	private Repository repository;
	private EditText title;
	private EditText content;

	private Issue issue;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.act_new_issue);
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
				// TODO Auto-generated method stub
				// Intent data = new Intent();
				// data.putExtra(EXTRA_ISSUE_FILTER, filter);
				// setResult(RESULT_OK, data);
				// finish();
			}
		});
	}

	private void newIssue() {
		if (issue == null)
			issue = new Issue();
		issue.setTitle(title.getText().toString());
		issue.setBody(content.getText().toString());
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		repository = (Repository) getIntent().getSerializableExtra(
				Constants.Extra.REPOSITORY);
	}

	@Override
	protected void onleftLayout() {
		// TODO Auto-generated method stub
		finish();
	}

}
