package com.gdestiny.github.ui.activity;

import org.eclipse.egit.github.core.Repository;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseFragmentActivity;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.async.PreviewTask;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.Constants;

public abstract class AbstractCommentActivity extends BaseFragmentActivity {

	private Repository repository;
	private EditText content;

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public String getContent() {
		return content.getText().toString();
	}

	public void setContent(String content) {
		this.content.setText(content);
	}

	public EditText getEditText() {
		return content;
	}

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
				AbstractCommentActivity.this.onOK();
			}
		});
		right.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				preview();
				return true;
			}
		});
	}

	protected abstract void onOK();

	private void preview() {
		AndroidUtils.Keyboard.hideKeyboard(context);
		new PreviewTask(context, repository, content.getText().toString()) {

			@Override
			public void onSend() {
				onOK();
			}
		}.execute(GitHubApplication.getClient());
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		content = (EditText) findViewById(R.id.comment_content);

		TextView tips = (TextView) findViewById(R.id.tips);
		String str = tips.getText().toString();
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new ClickableSpan() {

			@Override
			public void onClick(View widget) {
				preview();
			}

			@Override
			public void updateDrawState(TextPaint ds) {
				ds.setColor(0xff56abe4);
				ds.setUnderlineText(true);
			}
		}, str.lastIndexOf("preview"),
				str.lastIndexOf("preview") + "preview".length(),
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		tips.setText(ss);
		tips.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		repository = (Repository) getIntent().getSerializableExtra(
				Constants.Extra.REPOSITORY);
	}

	@Override
	protected void onleftLayout() {
		finish();
	}

}
