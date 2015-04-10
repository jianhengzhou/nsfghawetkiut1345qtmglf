package com.gdestiny.github.ui.activity;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.User;

import com.gdestiny.github.R;
import com.gdestiny.github.async.GistCommentTask;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.IntentUtils;

public class EditGistCommentActivity extends AbstractCommentActivity {

	private Comment comment;
	private int position;
	private Gist gist;

	@Override
	protected void initData() {
		super.initData();
		comment = (Comment) getIntent().getSerializableExtra(
				Constants.Extra.COMMENT);

		setContent(comment.getBody());

		position = getIntent().getIntExtra(Constants.Extra.POSITION, -1);
		gist = (Gist) getIntent().getSerializableExtra(Constants.Extra.GIST);
		User user = gist.getUser();
		if (user != null) {
			getTitlebar().setLeftLayout(user.getAvatarUrl(), gist.getId(),
					user.getLogin());
		} else {
			getTitlebar().setTitleText(gist.getId())
					.setTitleTextSecondly(R.string.anonymous)
					.setTitleIcon(R.drawable.common_anonymous_round);
		}
	}

	@Override
	protected void onOK() {
		AndroidUtils.Keyboard.hideKeyboard(context);
		comment.setBody(getContent());
		new GistCommentTask(context, gist.getId(), comment) {

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
