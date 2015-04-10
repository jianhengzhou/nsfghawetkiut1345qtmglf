package com.gdestiny.github.ui.activity;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Issue;

import com.gdestiny.github.async.CommentTask;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.IntentUtils;

public class NewCommentActivity extends AbstractCommentActivity {

	private Issue issue;

	@Override
	protected void initData() {
		super.initData();
		issue = (Issue) getIntent().getSerializableExtra(Constants.Extra.ISSUE);
		getTitlebar().setLeftLayout(null, "issue #" + issue.getNumber(),
				"Comment");
	}

	@Override
	protected void onOK() {
		commitComment();
	}

	private void commitComment() {
		AndroidUtils.Keyboard.hideKeyboard(context);
		new CommentTask(context, getRepository(), issue, getContent()) {

			@Override
			public void onSuccess(Comment comment) {
				super.onSuccess(comment);
				IntentUtils.create(context)
						.putExtra(Constants.Extra.COMMENT, comment)
						.setResultOk().finish();
			}
		}.execute();
	}
}
