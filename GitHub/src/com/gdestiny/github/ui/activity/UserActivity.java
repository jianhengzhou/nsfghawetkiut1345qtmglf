package com.gdestiny.github.ui.activity;

import static org.eclipse.egit.github.core.client.IGitHubConstants.CHARSET_UTF8;

import org.eclipse.egit.github.core.User;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.gdestiny.github.R;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.async.ContributionWebTask;
import com.gdestiny.github.ui.dialog.MaterialDialog;
import com.gdestiny.github.ui.view.ImageViewEx;
import com.gdestiny.github.ui.view.ObservableScrollView;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.CommonUtils;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.TimeUtils;
import com.gdestiny.github.utils.ToastUtils;
import com.gdestiny.github.utils.ViewUtils;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class UserActivity extends BaseFragmentActivity implements
		View.OnClickListener {

	private ObservableScrollView scrollView;

	private FrameLayout titleLayout;
	private ImageButton edit;

	private View iconLayout;
	private int iconHeight;
	private boolean firstShow = false;

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
		iconLayout = findViewById(R.id.icon_layout);
		titleLayout = (FrameLayout) findViewById(R.id.title_layout);
		titleLayout.addView(titlebar);
		ViewUtils.setVisibility(titlebar, View.INVISIBLE);
		findViewById(R.id.back).setOnClickListener(this);
		edit = (ImageButton) findViewById(R.id.edit);
		edit.setOnClickListener(this);

		scrollView = (ObservableScrollView) findViewById(R.id.scroll);
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
								firstShow = true;
							}
						} else {
							if (firstShow) {
								ViewUtils.setVisibility(titlebar,
										View.INVISIBLE, R.anim.alpha_out);
								firstShow = false;
							}
						}

					}
				});
		initWebView();
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

	SmoothProgressBar webBar;
	WebView webview;

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		User user = GitHubApplication.getUser();

		avator = (ImageViewEx) findViewById(R.id.avatar);
		loginName = (TextView) findViewById(R.id.login_name);
		name = (EditText) findViewById(R.id.name);
		email = (EditText) findViewById(R.id.e_mail);
		company = (EditText) findViewById(R.id.company);
		location = (EditText) findViewById(R.id.location);
		join = (TextView) findViewById(R.id.join);
		disk = (TextView) findViewById(R.id.disk);
		followers = (TextView) findViewById(R.id.followers);
		following = (TextView) findViewById(R.id.following);

		bindUser(user);
		loadContribution(user.getLogin());
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		webBar = (SmoothProgressBar) findViewById(R.id.web_bar);
		webview = (WebView) findViewById(R.id.webview);
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
				ViewUtils.setVisibility(webBar, View.GONE);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println("shouldOverrideUrlLoading");
				return true;
			}

		});
	}

	private void loadContribution(final String name) {
		new ContributionWebTask(context) {

			@Override
			public void onPrev() {
				System.out.println("onPrev");
				ViewUtils.setVisibility(webBar, View.VISIBLE);
			}

			@Override
			public void onSuccess(String result) {
				webview.loadDataWithBaseURL(null, result, "text/html",
						CHARSET_UTF8, null);
				ToastUtils.show(context, "succeed");
			}

		}.execute(name);
	}

	private void bindUser(User user) {
		titlebar.setLeftLayout(user.getAvatarUrl(), user.getLogin(),
				user.getName());
		ImageLoaderUtils.displayImage(user.getAvatarUrl(), avator,
				R.drawable.default_avatar, R.drawable.default_avatar, false);

		loginName.setText(user.getLogin());

		ViewUtils.setText(name, user.getName());

		bing(email, user.getEmail());
		bing(company, user.getCompany());
		bing(location, user.getLocation());
		bing(join,
				"Joined on "
						+ TimeUtils.getTime(user.getCreatedAt().getTime(),
								TimeUtils.DATE_FORMAT_DATE));
		bing(disk, CommonUtils.sizeToSuitable(user.getDiskUsage() * 1024));
		bing(followers, user.getFollowers() + "");
		bing(following, user.getFollowing() + "");
	}

	private void bing(TextView editText, String text) {
		editText.setText(CommonUtils.nullToNA(text));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
			final MaterialDialog dialog = new MaterialDialog(context);

			dialog.setTitle(R.string.warning).setCanceledOnTouchOutside(true)
					.setMessage(R.string.discard_change)
					.setPositiveButton("ok", new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							getTitlebar().getRightBtn().setImageResource(
									R.drawable.common_edit_pen);
							edit.setImageResource(R.drawable.common_edit_pen);
							setEnabled(false);
						}
					}).setNegativeButton("cancle", new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							return;
						}
					}).show();

			return;
		}
		super.onBackPressed();
	}

	@Override
	protected void onRightBtn() {
		super.onRightBtn();
		int id = 0;
		if (isEditing()) {
			id = R.drawable.common_edit_pen;
			setEnabled(false);

		} else {
			id = R.drawable.common_btn_ok;
			setEnabled(true);
		}
		getTitlebar().getRightBtn().setImageResource(id);
		edit.setImageResource(id);
		scrollView.scrollTo(0, 0);
	}

	private boolean isEditing() {
		return email.isEnabled();
	}

	private void setEnabled(boolean enabled) {
		email.setEnabled(enabled);
		name.setEnabled(enabled);
		company.setEnabled(enabled);
		location.setEnabled(enabled);
	}
}
