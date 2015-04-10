package com.gdestiny.github.ui.activity;

import org.eclipse.egit.github.core.Comment;

import com.gdestiny.github.async.CommentTask;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.IntentUtils;

public class EditCommentActivity extends AbstractCommentActivity {

	private Comment comment;
	private int position;

	@Override
	protected void initData() {
		super.initData();
		comment = (Comment) getIntent().getSerializableExtra(
				Constants.Extra.COMMENT);
		position = getIntent().getIntExtra(Constants.Extra.POSITION, -1);
		setContent(comment.getBodyText());

		getTitlebar().setLeftLayout(null, "issue", "Comment");
	}

	@Override
	protected void onOK() {
		editComment();
	}

	private void editComment() {
		comment.setBody(getContent());
		AndroidUtils.Keyboard.hideKeyboard(context);
		new CommentTask(context, getRepository(), comment) {

			@Override
			public void onSuccess(Comment comment) {
				super.onSuccess(comment);
				IntentUtils.create(context)
						.putExtra(Constants.Extra.COMMENT, comment)
						.putExtra(Constants.Extra.POSITION, position)
						.setResultOk().finish();
			}
		}.execute();
	}
}
