package com.gdestiny.github.ui.activity;

import static org.eclipse.egit.github.core.client.IGitHubConstants.CHARSET_UTF8;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseFragmentActivity;
import com.gdestiny.github.abstracts.async.GitHubTask;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.async.ContributionWebTask;
import com.gdestiny.github.async.EditUserTask;
import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.ui.dialog.ConfirmDialog;
import com.gdestiny.github.ui.view.ImageViewEx;
import com.gdestiny.github.ui.view.ObservableScrollView;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.CacheUtils;
import com.gdestiny.github.utils.CommonUtils;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.SnappyDBUtils;
import com.gdestiny.github.utils.TimeUtils;
import com.gdestiny.github.utils.ToastUtils;
import com.gdestiny.github.utils.ViewUtils;
import com.snappydb.SnappydbException;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class UserActivity extends BaseFragmentActivity implements
		View.OnClickListener {

	private ObservableScrollView scrollView;

	private FrameLayout titleLayout;
	private ImageButton edit;
	private SmoothProgressBar refreshBar;

	private ContributionWebTask contributionWebTask;

	private View iconLayout;
	private int iconHeight;
	private boolean firstShow = false;

	private User user;

	private String nameBackup, emailBackup, companyBackup, locationBackup;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_user);
	}

	@Override
	protected void initActionBar(TitleBar titleBar) {
		// super.initActionBar(titleBar);
		// titleBar.hideRight();
		// titleBar.setLeftLayout(null, null, null);
		// titleBar.getBackground().setAlpha(100);
		ActionBar ab = getSupportActionBar();

		ab.hide();
	}

	private void initTitleBar() {
		View backLayout = titlebar.findViewById(R.id.title_left_layout);
		backLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onleftLayout();
			}
		});
		titlebar.showRightBtn();
		ImageButton rightBtn = (ImageButton) titlebar
				.findViewById(R.id.titlebar_rignt_btn);
		rightBtn.setImageResource(R.drawable.common_edit_pen);
		rightBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onRightBtn();
			}
		});
	}

	@Override
	protected void initView() {
		initTitleBar();
		refreshBar = (SmoothProgressBar) findViewById(R.id.refresh_bar);
		ShapeDrawable shape = new ShapeDrawable();
		shape.setShape(new RectShape());
		shape.getPaint().setColor(0xffffffff);
		ClipDrawable clipDrawable = new ClipDrawable(shape, Gravity.CENTER,
				ClipDrawable.HORIZONTAL);
		refreshBar.setProgressDrawable(clipDrawable);

		iconLayout = findViewById(R.id.icon_layout);
		titleLayout = (FrameLayout) findViewById(R.id.title_layout);
		titleLayout.addView(titlebar);

		ViewUtils.setVisibility(titlebar, View.INVISIBLE);
		findViewById(R.id.load_contribution).setOnClickListener(this);
		cancle = findViewById(R.id.cancle_contribution);
		cancle.setOnClickListener(this);
		findViewById(R.id.refresh_contribution).setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
		// findViewById(R.id.block).setOnClickListener(this);
		findViewById(R.id.followers).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						refreshBar.setIndeterminate(false);
						refreshBar.setProgress(0);
					}
				});
		edit = (ImageButton) findViewById(R.id.edit);
		edit.setOnClickListener(this);

		scrollView = (ObservableScrollView) findViewById(R.id.scroll);
		scrollView.setCanDrag(true);
		scrollView
				.setOnScrollChangedListener(new ObservableScrollView.OnScrollChangedListener() {

					@Override
					public void onScrollChanged(ScrollView who, int l, int t,
							int oldl, int oldt) {
						// System.out.println(t + "," + oldt);
						if (t > iconHeight) {
							if (!firstShow) {
								ViewUtils.setVisibility(titlebar, View.VISIBLE,
										R.anim.alpha_in);
								changeRefreshStyle(false);
								firstShow = true;
							}
						} else {
							if (firstShow) {
								ViewUtils.setVisibility(titlebar,
										View.INVISIBLE, R.anim.alpha_out);
								changeRefreshStyle(true);
								firstShow = false;
							}
						}

					}

					@Override
					public void onDragDown(float percent) {
						if (refreshBar.isIndeterminate())
							return;
						int progress = (int) (percent * 100);
						refreshBar.setProgress(progress);
						if (progress >= 100) {
							onRefreshStarted();
						}
					}
				});
		initWebView();
	}

	private void changeRefreshStyle(boolean top) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		if (top) {
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			refreshBar.setSmoothProgressDrawableColor(0xffffffff);
		} else {
			params.addRule(RelativeLayout.BELOW, R.id.title_layout);
			refreshBar.setSmoothProgressDrawableColor(0xff56abe4);
		}
		refreshBar.setLayoutParams(params);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		iconHeight = iconLayout.getHeight() - titleLayout.getHeight();
		// System.out.println("getHeight" + titleLayout.getHeight());
	}

	ImageViewEx avator;
	TextView loginName;

	EditText name;
	EditText email;
	EditText company;
	EditText location;

	TextView join;
	TextView disk;
	TextView followers;
	TextView following;

	TextView repoPublic;
	TextView repoPrivate;
	TextView gistPublic;
	TextView gistPrivate;

	CircularProgressBar lodingBar;
	ProgressBar webBar;
	WebView webview;
	TextView webProgress;
	View cancle;

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		user = GitHubApplication.getUser();

		avator = (ImageViewEx) findViewById(R.id.avatar);
		loginName = (TextView) findViewById(R.id.login_name);
		name = (EditText) findViewById(R.id.name);
		email = (EditText) findViewById(R.id.e_mail);
		email.setMovementMethod(LinkMovementMethod.getInstance());
		company = (EditText) findViewById(R.id.company);
		location = (EditText) findViewById(R.id.location);
		join = (TextView) findViewById(R.id.join);
		disk = (TextView) findViewById(R.id.disk);
		followers = (TextView) findViewById(R.id.followers);
		following = (TextView) findViewById(R.id.following);

		repoPublic = (TextView) findViewById(R.id.repo_public);
		repoPrivate = (TextView) findViewById(R.id.repo_private);
		gistPublic = (TextView) findViewById(R.id.gist_public);
		gistPrivate = (TextView) findViewById(R.id.gist_private);

		bindUser(user);
		if (CacheUtils.contain(CacheUtils.NAME.CONTRIBUTION)) {
			ViewUtils.setVisibility(findViewById(R.id.load_contribution),
					View.GONE);
			loadContribution(user.getLogin());
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		webProgress = (TextView) findViewById(R.id.web_progress);
		lodingBar = (CircularProgressBar) findViewById(R.id.loading_progress);
		webBar = (ProgressBar) findViewById(R.id.web_bar);
		webview = (WebView) findViewById(R.id.webview);
		webview.setBackgroundColor(0xfffafafa);
		webview.setInitialScale((int) (webview.getScale() * 45));

		WebSettings websetting = webview.getSettings();
		websetting.setJavaScriptEnabled(true);
		websetting.setUseWideViewPort(true);// êPæIüc
		// websetting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// websetting.setLoadWithOverviewMode(true);

		webview.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				GLog.sysout("onPageStarted:" + url);
				ViewUtils.setVisibility(webBar, View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				GLog.sysout("onPageFinished:" + url);
				webBar.setProgress(100);
				ViewUtils.setVisibility(lodingBar, View.GONE);
				ViewUtils.setVisibility(webBar, View.GONE);
				ViewUtils.setVisibility(webProgress, View.GONE);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println("shouldOverrideUrlLoading");
				return true;
			}

		});
	}

	private void loadContribution(final String name) {

		if (contributionWebTask == null) {
			contributionWebTask = new ContributionWebTask(context) {

				@Override
				public void onPrev() {
					webProgress.setText(R.string.zero);
					webBar.setProgress(0);
					ViewUtils.setVisibility(webBar, View.VISIBLE);
					ViewUtils.setVisibility(lodingBar, View.VISIBLE);
					ViewUtils.setVisibility(webProgress, View.VISIBLE);
					ViewUtils.setVisibility(cancle, View.VISIBLE);
				}

				@Override
				public void onProgress(int progress) {
					webBar.setProgress(progress);
					webProgress.setText(progress + "");
				}

				@Override
				public void onSuccess(String result) {
					ViewUtils.setVisibility(cancle, View.GONE);
					webview.loadDataWithBaseURL(null, result, "text/html",
							CHARSET_UTF8, null);
					// ToastUtils.show(context, "succeed");
				}

				@Override
				public void onCancelled() {
					ViewUtils.setVisibility(webBar, View.GONE);
					ViewUtils.setVisibility(lodingBar, View.GONE);
					ViewUtils.setVisibility(webProgress, View.GONE);
					ViewUtils.setVisibility(cancle, View.GONE);
				}

			};
		} else {
			contributionWebTask.reload(name);
		}
		contributionWebTask.execute(name);
	}

	private void onRefreshStarted() {
		new GitHubTask<User>(new GitHubTask.TaskListener<User>() {

			@Override
			public void onPrev() {
				refreshBar.setIndeterminate(true);
			}

			@Override
			public User onExcute(GitHubClient client) {
				try {
					return GitHubConsole.getInstance().getUser(user.getLogin());
				} catch (Exception e) {
					e.printStackTrace();
					ToastUtils.showAsync(context, e.getMessage());
				}
				return null;
			}

			@Override
			public void onSuccess(User result) {
				user = result;
				bindUser(result);
				try {
					SnappyDBUtils.putSerializable(context, "user", result);
				} catch (SnappydbException e) {
					e.printStackTrace();
				}
				refreshBar.setIndeterminate(false);
				refreshBar.setProgress(0);
			}

			@Override
			public void onError() {
				refreshBar.setIndeterminate(false);
				refreshBar.setProgress(0);

			}
		}).execute();
	}

	private void bindUserEdit(User user) {
		ViewUtils.setText(name, user.getName());

		bing(email, user.getEmail());
		bing(company, user.getCompany());
		bing(location, user.getLocation());
	}

	private void bindUser(User user) {
		titlebar.setLeftLayout(user.getAvatarUrl(), user.getLogin(),
				user.getName());
		ImageLoaderUtils.displayImage(user.getAvatarUrl(), avator,
				R.drawable.default_avatar, R.drawable.default_avatar, false);

		loginName.setText(user.getLogin());
		bindUserEdit(user);

		bing(join,
				"Joined on "
						+ TimeUtils.getTime(user.getCreatedAt().getTime(),
								TimeUtils.DATE_FORMAT_DATE));
		bing(disk, CommonUtils.sizeToSuitable(user.getDiskUsage() * 1024));
		bing(followers, user.getFollowers() + "");
		bing(following, user.getFollowing() + "");

		bing(repoPublic, user.getPublicRepos() + "");
		bing(repoPrivate,
				user.getTotalPrivateRepos() + " ( "
						+ user.getOwnedPrivateRepos() + " )");
		bing(gistPublic, user.getPrivateGists() + "");
		bing(gistPrivate, user.getPrivateGists() + "");

	}

	private void bing(TextView editText, String text) {
		editText.setText(CommonUtils.nullToNA(text));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.load_contribution:
			ViewUtils.setVisibility(v, View.GONE, R.anim.alpha_out);
			loadContribution(user.getLogin());
			break;
		case R.id.cancle_contribution:
			contributionWebTask.cancle();
			break;
		case R.id.refresh_contribution:
			loadContribution(user.getLogin());
			break;
		case R.id.block:
			// IntentUtils.create(context,WebViewActivity.class).putExtra(Constants.Extra.DATA,
			// webview.getc)
			break;
		case R.id.back:
			onleftLayout();
			break;
		case R.id.edit:
			onRightBtn();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onleftLayout() {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		if (isEditing()) {
			if (!hasChange()) {
				refreshEditState(true);
				ViewUtils.setText(name, nameBackup);
				return;
			}
			AndroidUtils.Keyboard.hideKeyboard(context);
			new ConfirmDialog(context, R.string.discard_change) {

				@Override
				public void onOk() {
					refreshEditState(true);
					loadBackup();
				}

				@Override
				public void onCancle() {

				}
			}.show();
			return;
		}
		super.onBackPressed();
	}

	@Override
	protected void onRightBtn() {
		super.onRightBtn();
		if (isEditing()) {
			if (!hasChange()) {
				ToastUtils.show(context, R.string.no_change);
				refreshEditState(true);
				ViewUtils.setText(name, nameBackup);
				return;
			}
			if (!TextUtils.isEmpty(CommonUtils.NAToNull(email.getText()
					.toString()))
					&& !AndroidUtils.isEmail(email.getText().toString().trim())) {
				ToastUtils.show(context, R.string.not_email);
				return;
			}
			AndroidUtils.Keyboard.hideKeyboard(UserActivity.this.context);
			new ConfirmDialog(context, R.string.sure_to_update_profile) {

				@Override
				public void onOk() {
					editUser();
					new EditUserTask(context, user) {

						@Override
						public void onSuccess(User result) {
							super.onSuccess(result);
							try {
								SnappyDBUtils.putSerializable(context, "user",
										result);
							} catch (SnappydbException e) {
								e.printStackTrace();
							}
							refreshEditState(true);
							bindUserEdit(result);
						}
					}.execute();
				}

				@Override
				public void onCancle() {
				}
			}.show();

		} else {
			refreshEditState(false);
		}
	}

	private void refreshEditState(boolean editingEnd) {
		int id = 0;
		if (editingEnd) {
			id = R.drawable.common_edit_pen;
			setEnabled(false);
		} else {
			backup();
			id = R.drawable.common_btn_ok;
			setEnabled(true);
			AndroidUtils.Keyboard.showKeyboard(context, name);
		}
		getTitlebar().getRightBtn().setImageResource(id);
		edit.setImageResource(id);
		scrollView.scrollTo(0, 0);
	}

	private boolean hasChange() {
		return !equal(emailBackup, email.getText().toString())
				|| !equal(locationBackup, location.getText().toString())
				|| !equal(nameBackup, name.getText().toString())
				|| !equal(companyBackup, company.getText().toString());
	}

	private boolean equal(String c1, String c2) {
		if (c1 == null && c2 == null) {
			return true;
		}
		if (c1 != null && c2 != null)
			return c1.trim().equals(c2.trim());
		return false;
	}

	private void editUser() {
		user.setName(name.getText().toString());
		user.setEmail(CommonUtils.NAToNull(email.getText().toString()));
		user.setCompany(CommonUtils.NAToNull(company.getText().toString()));
		user.setLocation(CommonUtils.NAToNull(location.getText().toString()));
	}

	private void loadBackup() {
		ViewUtils.setText(name, nameBackup);
		user.setEmail(emailBackup);
		user.setCompany(companyBackup);
		user.setLocation(locationBackup);
		bindUserEdit(user);
	}

	private void backup() {
		nameBackup = user.getName();
		emailBackup = CommonUtils.nullToNA(user.getEmail());
		companyBackup = CommonUtils.nullToNA(user.getCompany());
		locationBackup = CommonUtils.nullToNA(user.getLocation());
	}

	private boolean isEditing() {
		return email.isEnabled();
	}

	private void setEnabled(boolean enabled) {
		ViewUtils.setVisibility(name, View.VISIBLE);
		email.setEnabled(enabled);
		name.setEnabled(enabled);
		company.setEnabled(enabled);
		location.setEnabled(enabled);
	}
}
