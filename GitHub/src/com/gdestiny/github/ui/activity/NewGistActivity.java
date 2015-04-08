package com.gdestiny.github.ui.activity;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseFragmentActivity;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.async.NewGistTask;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.SwipeDismissTouchListener;
import com.gdestiny.github.utils.ToastUtils;

public class NewGistActivity extends BaseFragmentActivity {

	private LinearLayout fileLayout;
	private EditText description;
	private CheckBox isPublic;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_new_gist);
	}

	@Override
	protected void initActionBar(TitleBar titleBar) {
		super.initActionBar(titleBar);
		titleBar.showRightBtn();
		ImageButton right = titleBar.getRightBtn();
		right.setImageResource(R.drawable.common_btn_ok);
		right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Map<String, GistFile> files = getFiles();
				if (files == null)
					return;
				AndroidUtils.Keyboard.hideKeyboard(context);
				new NewGistTask(context, isPublic.isChecked(), description
						.getText().toString(), files) {

					@Override
					public void onSuccess(Gist result) {
						super.onSuccess(result);
						IntentUtils.create(context)
								.putExtra(Constants.Extra.GIST, result)
								.setResultOk().finish();
					}
				}.execute();
			}
		});
		right.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				addFile();
				return true;
			}
		});
	}

	@Override
	protected void initView() {
		fileLayout = (LinearLayout) findViewById(R.id.file_layout);
		View init = findViewById(R.id.init_file);
		SwipeDismissTouchListener dismissTouch = new SwipeDismissTouchListener(
				init, null, new SwipeDismissTouchListener.DismissCallbacks() {

					@Override
					public void onDismiss(View view, Object token) {
						fileLayout.removeView(view);
					}

					@Override
					public boolean canDismiss(Object token) {
						return fileLayout.getChildCount() > 8;
					}
				});
		init.setOnTouchListener(dismissTouch);

		isPublic = (CheckBox) findViewById(R.id.isPublic);
		description = (EditText) findViewById(R.id.gist_description);
		findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				addFile();
			}
		});
	}

	private Map<String, GistFile> getFiles() {
		Map<String, GistFile> files = new HashMap<String, GistFile>();

		for (int i = 4; i < fileLayout.getChildCount() - 3; i++) {
			View v = fileLayout.getChildAt(i);
			String name = ((EditText) v.findViewById(R.id.file_name)).getText()
					.toString();
			if (files.containsKey(name)) {
				ToastUtils.show(context, R.string.conflict_filename);
				return null;
			}
			String content = ((EditText) v.findViewById(R.id.file_content))
					.getText().toString();

			GistFile file = new GistFile().setFilename(name)
					.setContent(content);
			files.put(name, file);
		}
		return files;
	}

	private void addFile() {
		View v = LayoutInflater.from(context).inflate(
				R.layout.item_new_gist_file, null);

		SwipeDismissTouchListener dismissTouch = new SwipeDismissTouchListener(
				v, null, new SwipeDismissTouchListener.DismissCallbacks() {

					@Override
					public void onDismiss(View view, Object token) {
						fileLayout.removeView(view);
					}

					@Override
					public boolean canDismiss(Object token) {
						return fileLayout.getChildCount() > 8;
					}
				});
		v.setOnTouchListener(dismissTouch);

		int count = fileLayout.getChildCount();
		v.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.add_view_horizontal));
		fileLayout.addView(v, count - 3);

	}

	@Override
	protected void initData() {
		User user = GitHubApplication.getUser();
		getTitlebar().setLeftLayout(user.getAvatarUrl(), "New Gist",
				user.getLogin());
	}

	@Override
	protected void onleftLayout() {
		finish();
	}

}
