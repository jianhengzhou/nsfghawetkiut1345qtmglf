package com.gdestiny.github.ui.activity;

import org.eclipse.egit.github.core.CommitComment;

import com.gdestiny.github.async.CommitCommentTask;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.IntentUtils;

public class EditCommitCommentActivity extends AbstractCommentActivity {

	private int groupPosition;
	private int childPosition;
	private CommitComment comment;

	@Override
	protected void initData() {
		super.initData();
		groupPosition = getIntent().getIntExtra(Constants.Extra.GRIUP_POSITION,
				-1);
		childPosition = getIntent().getIntExtra(Constants.Extra.CHILD_POSITION,
				-1);
		comment = (CommitComment) getIntent().getSerializableExtra(
				Constants.Extra.COMMIT_COMMENT);
		setContent(comment.getBody());

		getTitlebar().setLeftLayout(getRepository().getOwner().getAvatarUrl(),
				"Edit", getRepository().generateId());
	}

	@Override
	protected void onOK() {
		comment.setBody(getContent());
		new CommitCommentTask(context, getRepository(), null, comment) {

			@Override
			public void onSuccess(CommitComment comment) {
				super.onSuccess(comment);
				IntentUtils
						.create(context)
						.putExtra(Constants.Extra.COMMIT_COMMENT, comment)
						.putExtra(Constants.Extra.GRIUP_POSITION, groupPosition)
						.putExtra(Constants.Extra.CHILD_POSITION, childPosition)
						.setResultOk().finish();
			}
		}.execute();
	}

}
