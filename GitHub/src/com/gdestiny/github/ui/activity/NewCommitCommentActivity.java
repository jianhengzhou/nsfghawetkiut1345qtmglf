package com.gdestiny.github.ui.activity;

import org.eclipse.egit.github.core.CommitComment;

import android.text.TextUtils;

import com.gdestiny.github.async.CommitCommentTask;
import com.gdestiny.github.bean.CommitLine;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.IntentUtils;

public class NewCommitCommentActivity extends AbstractCommentActivity {

	private CommitLine commitLine;
	private String sha;
	private String path;

	@Override
	protected void initData() {
		super.initData();
		commitLine = (CommitLine) getIntent().getSerializableExtra(
				Constants.Extra.COMMIT_LINE);
		sha = (String) getIntent().getSerializableExtra(Constants.Extra.SHA);
		path = (String) getIntent().getSerializableExtra(Constants.Extra.PATH);

		getTitlebar().setLeftLayout(getRepository().getOwner().getAvatarUrl(),
				"New", getRepository().generateId());

		if (commitLine != null)
			getEditText().setHint("Comment On:" + commitLine.getLine());
	}

	@Override
	protected void onOK() {
		CommitComment comment = new CommitComment();
		comment.setBody(getContent());

		if (!TextUtils.isEmpty(path) && commitLine != null) {
			comment.setPath(path);

			int line = commitLine.getNewLine();
			if (line <= 0)
				line = commitLine.getOldLine();
			comment.setPosition(commitLine.getPosition() + 1);
		}

		new CommitCommentTask(context, getRepository(), sha, comment) {

			@Override
			public void onSuccess(CommitComment comment) {
				super.onSuccess(comment);
				IntentUtils.create(context)
						.putExtra(Constants.Extra.COMMIT_COMMENT, comment)
						.setResultOk().finish();
			}
		}.execute();
	}

}
