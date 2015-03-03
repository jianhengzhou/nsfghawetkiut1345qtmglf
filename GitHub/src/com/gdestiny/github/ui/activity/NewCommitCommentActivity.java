package com.gdestiny.github.ui.activity;

import org.eclipse.egit.github.core.CommitComment;

import com.gdestiny.github.utils.Constants;

public class NewCommitCommentActivity extends AbstractCommentActivity {

	@SuppressWarnings("unused")
	private CommitComment commitComment;

	@Override
	protected void initData() {
		super.initData();
		commitComment = (CommitComment) getIntent().getSerializableExtra(
				Constants.Extra.COMMIT_COMMENT);
	}

	@Override
	protected void onOK() {
		// TODO Auto-generated method stub

	}

}
