package com.gdestiny.github.ui.activity;

import org.eclipse.egit.github.core.Comment;

import com.gdestiny.github.R;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.async.CommentTask;
import com.gdestiny.github.async.PreviewTask;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.AndroidUtils;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class CommentActivity extends BaseFragmentActivity {

	private EditText content;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_comment);
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
				AndroidUtils.Keyboard.hideKeyboard(context);
				commitComment();
			}
		});
		right.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				AndroidUtils.Keyboard.hideKeyboard(context);
				new PreviewTask(context, content.getText().toString()) {

					@Override
					public void onSend() {
						commitComment();
					}
				}.execute(GitHubApplication.getClient());
				return true;
			}
		});
	}

	private void commitComment() {
		new CommentTask(context, null, null, content.getText().toString()) {

			@Override
			public void onSuccess(Comment Comment) {
				// TODO Auto-generated method stub
				super.onSuccess(Comment);
			}
		}.execute(GitHubApplication.getClient());
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		content = (EditText) findViewById(R.id.comment_content);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		getTitlebar().setLeftLayout(null, "issue #" + 1, "Comment");
	}

	@Override
	protected void onleftLayout() {
		// TODO Auto-generated method stub

	}

}
