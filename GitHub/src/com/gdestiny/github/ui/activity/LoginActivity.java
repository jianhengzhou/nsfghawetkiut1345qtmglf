package com.gdestiny.github.ui.activity;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseFragmentActivity;
import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.app.DefaultClient;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Base64Util;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.PreferencesUtils;
import com.gdestiny.github.utils.ToastUtils;
import com.gdestiny.github.utils.ViewUtils;

public class LoginActivity extends BaseFragmentActivity implements
		OnClickListener {

	private EditText account;
	private EditText password;
	private View accountDel;
	private View passwordDel;

	@Override
	protected void onleftLayout() {
		finish();
	}

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_login);
		setSwipeBackEnable(false);
	}

	@Override
	protected void initActionBar(TitleBar titleBar) {
		super.initActionBar(titleBar);

		titlebar.setTitleText(R.string.login);
		titlebar.getRightBtn().setImageResource(R.drawable.common_login);
		titlebar.showRightBtn();
		ViewUtils.setVisibility(titlebar.getTitleBackIcon(), View.GONE);
	}

	@Override
	protected void initView() {
		account = (EditText) findViewById(R.id.account);
		password = (EditText) findViewById(R.id.password);
		passwordDel = findViewById(R.id.password_del);
		accountDel = findViewById(R.id.account_del);

		account.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(s))
					ViewUtils.setVisibility(accountDel, View.INVISIBLE);
				else
					ViewUtils.setVisibility(accountDel, View.VISIBLE);
			}
		});
		password.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(s))
					ViewUtils.setVisibility(passwordDel, View.INVISIBLE);
				else
					ViewUtils.setVisibility(passwordDel, View.VISIBLE);
			}
		});

		TextView registe = (TextView) findViewById(R.id.registe);
		registe.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

		registe.setOnClickListener(this);
		passwordDel.setOnClickListener(this);
		accountDel.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		try {
			String ac = PreferencesUtils.getString(context,
					Base64Util.encodeString(ACCOUNT));
			if (!TextUtils.isEmpty(ac)) {

				account.setText(Base64Util.decodeToString(ac));

				String pass = PreferencesUtils.getString(context,
						Base64Util.encodeString(PASSWORD));
				if (!TextUtils.isEmpty(pass)) {
					password.setText(Base64Util.decodeToString(pass));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			account.setText("");
			password.setText("");
		}
	}

	private boolean isLoginEnable() {
		return !TextUtils.isEmpty(account.getText())
				&& !TextUtils.isEmpty(password.getText());

	}

	@Override
	protected void onRightBtn() {
		if (isLoginEnable()) {
			login(account.getText().toString(), password.getText().toString());
		} else {
			ToastUtils.show(context,
					getResources().getString(R.string.login_error));
		}
	}

	private void login(String account, String password) {
		hideSoftInputMethod();
		final DefaultClient client = GitHubApplication.initClient(account,
				password);
		new DialogTask<GitHubClient, User>(context) {

			@Override
			public User onBackground(GitHubClient params) throws Exception {
				// client.setUserAgent(getString(R.string.app_name));
				// Authorization authorization = null;
				// OAuthService oAuthService = new OAuthService(client);
				// try {
				// List<Authorization> authorizationList = oAuthService
				// .getAuthorizations();
				//
				// for (Authorization a : authorizationList) {
				// if (getString(R.string.app_name).equals(a.getNote())) {
				// authorization = a;
				// break;
				// }
				// }
				//
				// /* 如果当前应用没有被认证，则新建认证 */
				// if (authorization == null) {
				// GLog.sysout("createAuthorization");
				// authorization = new Authorization();
				// authorization.setNote(getString(R.string.app_name));
				// authorization.setUrl("https://github.com/gdestiny");
				// List<String> scopes = new ArrayList<String>();
				// scopes.add("notifications");
				// scopes.add("repo");
				// scopes.add("user");
				// authorization.setScopes(scopes);
				// authorization = oAuthService
				// .createAuthorization(authorization);
				// }
				// String token = authorization.getToken();
				// PreferencesUtils.putString(context, "token", token);
				// GLog.sysout("token:" + token);
				// } catch (Exception ex) {
				// ex.printStackTrace();
				// return null;
				// }
				// return null;

				// // ///////////////////////////////////
				User user = null;
				user = new UserService(client).getUser();
				return user;
			}

			@Override
			public void onException(Exception ex) {
				Exception e = new Exception(getResources().getString(
						R.string.auth_error)
						+ ex.getMessage());
				super.onException(e);
			}

			@Override
			public void onSuccess(User result) {
				if (result == null) {
					ToastUtils.show(context, R.string.auth_error);
				} else {
					try {
						saveLoginState(result);
						GitHubApplication.setUser(result);
						GitHubApplication.setClient(client);
					} catch (Exception e) {
						e.printStackTrace();
						ToastUtils.show(context, e.getMessage());
					}
					Intent intent = new Intent(context, HomeActivity.class);
					startActivity(intent);
					finish();
				}
			}
		}.setTitle("Logging In").setLoadingMessage(account).execute(client);
	}

	public static final String IS_LOGIN = "isLogin";
	public static final String ACCOUNT = "account";
	public static final String PASSWORD = "password";

	private void saveLoginState(User user) throws Exception {
		SharedPreferences settings = context.getSharedPreferences(
				PreferencesUtils.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();

		editor.putBoolean(IS_LOGIN, true);
		editor.putString("login", user.getLogin());
		editor.putString(Base64Util.encodeString(ACCOUNT),
				Base64Util.encodeString(account.getText().toString()));
		editor.putString(Base64Util.encodeString(PASSWORD),
				Base64Util.encodeString(password.getText().toString()));
		editor.putString("user", Base64Util.encodeObject(user, false));

		editor.commit();

		// PreferencesUtils.putBoolean(context, "isLogin", true);
		// PreferencesUtils.putString(context, Base64.decode("account",
		// Base64.DEFAULT), Base64.decode(account.getText()
		// .toString(), Base64.DEFAULT));
		// CacheUtils.cacheObject("user", user);

		// SnappyDBUtils.putBoolean(context, "isLogin", true);
		// SnappyDBUtils.putString(context, "account", account.getText()
		// .toString());
		// SnappyDBUtils.putString(context, "password", password.getText()
		// .toString());
		// SnappyDBUtils.putSerializable(context, "user", user);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.registe:
			hideSoftInputMethod();
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			intent.setData(Uri.parse(Constants.GIT_JOIN));
			// startActivity(intent);
			IntentUtils.create(context, WebViewActivity.class)
					.putExtra(Constants.Extra.URL, Constants.GIT_JOIN).start();
			break;
		case R.id.account_del:
			account.requestFocus();
			if (!TextUtils.isEmpty(account.getText()))
				account.setText("");
			break;
		case R.id.password_del:
			password.requestFocus();
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

}
