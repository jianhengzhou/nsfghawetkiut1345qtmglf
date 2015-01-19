package com.gdestiny.github.ui.activity;

import java.io.IOException;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.async.GitHubTask;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.SnappyDBUtils;
import com.gdestiny.github.utils.TestUtils;
import com.gdestiny.github.utils.ToastUtils;
import com.gdestiny.github.utils.ViewUtils;
import com.snappydb.SnappydbException;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private EditText account;
	private EditText password;
	private Context context;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_login);

		initView();
		setSwipeBackEnable(false);
	}

	@Override
	protected void initActionBar() {
		// TODO Auto-generated method stub
		super.initActionBar();
		titlebar.setTitleText(R.string.login);
		titlebar.hideRight();
		ViewUtils.setVisibility(titlebar.getTitleBackIcon(),View.GONE);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		context = this;
		account = (EditText) findViewById(R.id.account);
		password = (EditText) findViewById(R.id.password);
		TextView registe = (TextView) findViewById(R.id.registe);
		registe.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

		if (Constants.isDebug) {
			account.setText("guandichao@163.com");
			password.setText("gdc723124938215");
		}

		registe.setOnClickListener(this);
		findViewById(R.id.login).setOnClickListener(this);
		findViewById(R.id.password_del).setOnClickListener(this);
		findViewById(R.id.account_del).setOnClickListener(this);
	}

	@Override
	protected void initData() {

	}

	private boolean isLoginEnable() {
		return !TextUtils.isEmpty(account.getText())
				&& !TextUtils.isEmpty(password.getText());

	}

	private void login(String account, String password) {
		hideSoftInputMethod();
		if (dialog == null)
			dialog = new ProgressDialog(this);
		dialog.setMessage(account);
		dialog.setCancelable(true);
		final GitHubClient client = GitHubApplication.initClient(account,
				password);
		new GitHubTask<User>(new GitHubTask.TaskListener<User>() {

			@Override
			public void onPrev() {
				dialog.show();
			}

			@Override
			public User onExcute(GitHubClient client) {
				User user = null;
				try {
					user = new UserService(client).getUser();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e.getMessage());
					return null;
				}
				return user;
			}

			@Override
			public void onSuccess(User result) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (result == null) {
				} else {
					System.out.println("" + TestUtils.printUser(result));
					try {
						saveLoginState(result);
						GitHubApplication.setUser(result);
						GitHubApplication.setClient(client);
					} catch (SnappydbException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ToastUtils.show(context, e.getMessage());
					}
					Intent intent = new Intent(context, HomeActivity.class);
					startActivity(intent);
					finish();
				}
			}

			@Override
			public void onError() {
				dialog.dismiss();
				ToastUtils.show(context,
						getResources().getString(R.string.auth_error));

			}
		}).execute(client);
	}

	private void saveLoginState(User user) throws SnappydbException {
		SnappyDBUtils.putBoolean(context, "isLogin", true);
		SnappyDBUtils.putString(context, "account", account.getText()
				.toString());
		SnappyDBUtils.putString(context, "password", password.getText()
				.toString());
		SnappyDBUtils.putSerializable(context, "user", user);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login:
			if (isLoginEnable()) {
				login(account.getText().toString(), password.getText()
						.toString());
			} else {
				ToastUtils.show(context,
						getResources().getString(R.string.login_error));
			}
			break;
		case R.id.registe:
			hideSoftInputMethod();
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			intent.setData(Uri.parse(Constants.GIT_JOIN));
			startActivity(intent);
			break;
		case R.id.account_del:
			if (!TextUtils.isEmpty(account.getText()))
				account.setText("");
			break;
		case R.id.password_del:
			if (!TextUtils.isEmpty(password.getText()))
				password.setText("");
			break;
		}
	}

	private void hideSoftInputMethod() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(
					this.getCurrentFocus().getWindowToken(), 0);
		}
	}

	@Override
	protected void onleftLayout() {
		finish();
	}

}
