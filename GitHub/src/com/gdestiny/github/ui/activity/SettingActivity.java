package com.gdestiny.github.ui.activity;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseFragmentActivity;
import com.gdestiny.github.abstracts.async.BaseAsyncTask;
import com.gdestiny.github.abstracts.async.ConfirmDialogTask;
import com.gdestiny.github.abstracts.async.DialogTask;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.dialog.ConfirmDialog;
import com.gdestiny.github.ui.dialog.MaterialDialog;
import com.gdestiny.github.ui.dialog.MaterialUpdateDialog;
import com.gdestiny.github.ui.dialog.StartUpDialog;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.Base64Util;
import com.gdestiny.github.utils.CacheUtils;
import com.gdestiny.github.utils.CommonUtils;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.PreferencesUtils;
import com.gdestiny.github.utils.TestUtils;
import com.gdestiny.github.utils.ToastUtils;
import com.gdestiny.github.utils.ViewUtils;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class SettingActivity extends BaseFragmentActivity implements
		View.OnClickListener {

	private MaterialUpdateDialog updateDialog;
	private CircularProgressBar updateBar;
	private View updateForword;
	private View updateNew;
	private TextView sizeText;
	private TextView startUpName;
	private TextView accountName;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_setting);

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		findViewById(R.id.rate).setOnClickListener(this);
		findViewById(R.id.share).setOnClickListener(this);
		findViewById(R.id.check_for_update).setOnClickListener(this);
		findViewById(R.id.feedback).setOnClickListener(this);
		findViewById(R.id.delete_cache).setOnClickListener(this);
		findViewById(R.id.start_up).setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.account).setOnClickListener(this);
		findViewById(R.id.about_us).setOnClickListener(this);
		findViewById(R.id.logout).setOnClickListener(this);
		updateBar = (CircularProgressBar) findViewById(R.id.update_progressBar);
		updateForword = findViewById(R.id.update_forword);
		updateNew = findViewById(R.id.update_new);
		sizeText = (TextView) findViewById(R.id.size_text);
		startUpName = (TextView) findViewById(R.id.start_up_name);
		accountName = (TextView) findViewById(R.id.account_name);

		TextView license = (TextView) findViewById(R.id.license);
		license.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		license.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			onleftLayout();
			break;
		case R.id.feedback:
			IntentUtils.create(context, FeedbackActivity.class).start();
			break;
		case R.id.rate:
			AndroidUtils.market(context);
			break;
		case R.id.account:
			IntentUtils.start(context, UserActivity.class);
			break;
		case R.id.about_us:
			IntentUtils.start(context, AboutUSActivity.class);
			break;
		case R.id.logout:
			logout();
			break;
		case R.id.license:
			new MaterialDialog(context)
					.setTitle("License")
					.setMessage(
							AndroidUtils.FileManager.getAssets(context,
									"terms-and-privacy")).show();
			break;
		case R.id.start_up:
			new StartUpDialog(context) {

				@Override
				public void onSelected(String name) {
					startUpName.setText("(" + name + ")");
				}
			}.show();
			break;
		case R.id.delete_cache:
			new ConfirmDialog(context, R.string.sure_to_delete_cache) {

				@Override
				public void onOk() {
					clearCache();
				}

				@Override
				public void onCancle() {

				}
			}.show();
			break;
		case R.id.share:
			AndroidUtils
					.share(context, "Github", "https://github.com/gdestiny");
			break;
		case R.id.check_for_update:
			ViewUtils.setVisibility(updateForword, View.GONE);
			ViewUtils.setVisibility(updateBar, View.VISIBLE);
			if (updateDialog == null) {
				updateDialog = new MaterialUpdateDialog(context);
				updateDialog.setOnUpdateListener(new UmengUpdateListener() {

					@Override
					public void onUpdateReturned(int updateStatus,
							UpdateResponse updateInfo) {
						ViewUtils.setVisibility(updateBar, View.GONE);
						ViewUtils.setVisibility(updateForword, View.VISIBLE);
						if (updateStatus == UpdateStatus.Yes) {
							ViewUtils.setVisibility(updateNew, View.VISIBLE);
						}else{
							ViewUtils.setVisibility(updateNew, View.GONE);
						}
						PreferencesUtils.putString(context, "net_version",
								updateInfo.version);
					}
				});
			}
			UmengUpdateAgent.forceUpdate(this);
			break;
		}
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		String netVersion = PreferencesUtils.getString(context, "net_version");
		if (!TextUtils.isEmpty(netVersion)) {
			String curVersion = AndroidUtils.getVersion(context);
			if (curVersion.compareToIgnoreCase(netVersion) < 0) {
				ViewUtils.setVisibility(updateNew, View.VISIBLE);
			}
		}
		accountName.setText("( " + GitHubApplication.getUser().getLogin()
				+ " )");
		int startup = PreferencesUtils.getInt(context, "startup", 0);
		startUpName.setText("( "
				+ getResources().getStringArray(R.array.startup_list)[startup]
				+ " )");
		updateCacheSize(false);
	}

	private void clearCache() {
		new DialogTask<Void, Boolean>(context) {

			@Override
			public Boolean onBackground(Void params) throws Exception {
				// TestUtils.interrupt(5000);
				// return true;
				try {
					AndroidUtils.FileManager.deleteContents(new File(
							CacheUtils.DATA_PATH));
				} catch (Exception ex) {
					return false;
				}
				return true;
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					ToastUtils.show(context, R.string.clearing_succeed);
					// CacheUtils.init(context);
					updateCacheSize(true);
				} else {
					ToastUtils.show(context, R.string.clearing_failed);
				}
			}
		}.setLoadingMessage(R.string.clearing_cache).execute(null);
	}

	/**
	 * 
	 * @param force
	 *            强制更新
	 */
	private void updateCacheSize(final boolean force) {
		new BaseAsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				long saveTime = PreferencesUtils.getLong(context,
						"cache_update_time");
				String size = null;
				// 大于一天更新
				if (!force && System.currentTimeMillis() - saveTime < 86400000L) {
					size = PreferencesUtils.getString(context, "cache_size");
					GLog.sysout("No Update Size");
					if (!TextUtils.isEmpty(size)) {
						return size;
					}
				}
				GLog.sysout("Update Size");
				size = "( "
						+ CommonUtils.sizeToSuitable(AndroidUtils.FileManager
								.getSize(CacheUtils.DATA_PATH)) + " )";
				// save
				PreferencesUtils.putString(context, "cache_size", size);
				PreferencesUtils.putLong(context, "cache_update_time",
						System.currentTimeMillis());
				GLog.sysout(size);
				return size;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if (!TextUtils.isEmpty(result)) {
					sizeText.setText(result);
				}
			}
		}.execute();
	}

	private void logout() {
		new ConfirmDialogTask<Void, Boolean>(context, "Are you sure to Logout?") {

			@Override
			public Boolean onBackground(Void params) throws Exception {
				SharedPreferences settings = context.getSharedPreferences(
						PreferencesUtils.PREFERENCE_NAME, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();

				editor.remove(LoginActivity.IS_LOGIN);
				editor.remove("login");
				editor.remove(Base64Util.encodeString(LoginActivity.PASSWORD));
				editor.remove("user");

				editor.commit();
				AndroidUtils.FileManager.deleteContents(new File(
						CacheUtils.DATA_PATH));
				TestUtils.interrupt(2000);
				return true;
			}

			@Override
			public void onSuccess(Boolean result) {
				AndroidUtils.goHome(SettingActivity.this, LoginActivity.class);
			}
		}.setLoadingMessage("Logging Out").execute();
	}

	@Override
	protected void initActionBar(TitleBar titleBar) {
		// TODO Auto-generated method stub
		// super.initActionBar(titleBar);
		getSupportActionBar().hide();
	}

	@Override
	protected void onleftLayout() {
		finish();
	}

}
