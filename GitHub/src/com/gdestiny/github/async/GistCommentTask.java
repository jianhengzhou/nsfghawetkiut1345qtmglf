package com.gdestiny.github.async;

import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_COMMENTS;
import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_GISTS;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.GistService;

import android.content.Context;
import android.text.TextUtils;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.ToastUtils;

public class GistCommentTask extends DialogTask<GitHubClient, Comment> {

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
	public Comment onBackground(GitHubClient params) throws Exception {
		GistService service = new GistService(params);

		if (comment != null) {
			return service.getClient().post(editComment(gistid, comment),
					comment, Comment.class);
		} else {
			if (TextUtils.isEmpty(commentContent.trim())) {
				throw new IllegalAccessException("the content is empty");
			}
			return service.createComment(gistid, commentContent);
		}
	}

	/**
	 * Edit gist comment.
	 * 
	 * TODO: Remove this method once egit GistService.java Gist Comment APIs are
	 * fixed. https://github.com/eclipse/egit-github/pull/7
	 * 
	 * @param comment
	 * @return edited comment
	 * @throws IOException
	 */
	private String editComment(String gistId, Comment comment) throws Exception {
		StringBuilder uri = new StringBuilder(SEGMENT_GISTS);
		uri.append('/').append(gistId);
		uri.append(SEGMENT_COMMENTS);
		uri.append('/').append(comment.getId());
		return uri.toString();
	}

	@Override
	public void onSuccess(Comment comment) {
		ToastUtils.show(context, R.string.commen_succeed);
	}

}
