package com.gdestiny.github.async;

import org.eclipse.egit.github.core.Comment;

import android.content.Context;
import android.text.TextUtils;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.utils.ToastUtils;

public class GistCommentTask extends DialogTask<Void, Comment> {

	private String gistid;

	private String commentContent;
	private Comment comment;

	public GistCommentTask(Context context, String gistid, Comment comment) {
		super(context);
		this.comment = comment;
		this.gistid = gistid;
		this.setLoadingMessage(R.string.committing_comment);
	}

	public GistCommentTask(Context context, String gistid, String comment) {
		super(context);
		this.gistid = gistid;
		this.commentContent = comment;
		this.setLoadingMessage(R.string.committing_comment);
	}

	@Override
	public Comment onBackground(Void params) throws Exception {

		if (comment != null) {
			return GitHubConsole.getInstance().editGistComment(gistid, comment);
		} else {
			if (TextUtils.isEmpty(commentContent.trim())) {
				throw new IllegalAccessException("the content is empty");
			}
			return GitHubConsole.getInstance().createGistComment(gistid,
					commentContent);
		}
	}

	@Override
	public void onSuccess(Comment comment) {
		ToastUtils.show(context, R.string.commen_succeed);
	}

}
