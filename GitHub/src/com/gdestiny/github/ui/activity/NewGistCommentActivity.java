package com.gdestiny.github.ui.activity;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.User;

import com.gdestiny.github.R;
import com.gdestiny.github.async.GistCommentTask;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.IntentUtils;

public class NewGistCommentActivity extends AbstractCommentActivity {

	private Gist gist;

	@Override
	protected void initData() {
		super.initData();
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
		new GistCommentTask(context, gist.getId(), getContent()) {

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
