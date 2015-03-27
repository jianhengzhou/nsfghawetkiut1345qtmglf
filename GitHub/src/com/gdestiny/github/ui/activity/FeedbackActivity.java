package com.gdestiny.github.ui.activity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.User;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.FeedbackAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.activity.abstracts.BaseLoadFragmentActivity;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.ToastUtils;
import com.gdestiny.github.utils.ViewUtils;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.SyncListener;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Reply;
import com.umeng.fb.model.UserInfo;

public class FeedbackActivity extends BaseLoadFragmentActivity<Void, Void> {

	private EditText inputEdit;
	private TextView infoText;

	private ListView list;
	private FeedbackAdapter adapter;

	private FeedbackAgent agent;
	private Conversation mComversation;
	private UserInfo userInfo;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_feedback, R.id.pull_refresh_layout);
	}

	@Override
	protected void initView() {
		agent = new FeedbackAgent(this);
		agent.closeAudioFeedback();
		userInfo = agent.getUserInfo();
		if (userInfo == null)
			userInfo = new UserInfo();
		mComversation = agent.getDefaultConversation();

		list = (ListView) findViewById(R.id.fb_reply_list);
		adapter = new FeedbackAdapter(context);
		adapter.setComversation(mComversation);
		list.setAdapter(adapter);
		scrollToBottom();

		inputEdit = (EditText) findViewById(R.id.fb_send_content);
		infoText = (TextView) findViewById(R.id.fb_userinfo);

		findViewById(R.id.fb_send_btn).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String content = inputEdit.getText().toString();
						inputEdit.getEditableText().clear();
						if (!TextUtils.isEmpty(content)) {
							// 将内容添加到会话列表
							mComversation.addUserReply(content);
							// 刷新ListView
							adapter.notifyDataSetChanged();
							scrollToBottom();
							// 数据同步
							sync();
						}
					}
				});
	}

	@Override
	protected void initData() {
		User user = GitHubApplication.getUser();
		String info = userInfoToString();
		if (user != null) {
			getTitlebar().setLeftLayout(user.getAvatarUrl(), "Feedback",
					user.getLogin());
			if (TextUtils.isEmpty(info)) {
				StringBuffer sb = new StringBuffer();
				sb.append("Name: ").append(user.getLogin());
				updateUserContact("plaint", user.getLogin());
				if (!TextUtils.isEmpty(user.getEmail())) {
					sb.append("\n").append("Email: ").append(user.getEmail());
					updateUserContact("plaint", user.getEmail());
				}
				agent.setUserInfo(userInfo);
				ViewUtils.setText(infoText, sb.toString());
			} else {
				ViewUtils.setText(infoText, info);
			}

		} else {
			ViewUtils.setText(infoText, info);
		}
	}

	private String userInfoToString() {
		if (userInfo == null)
			return "";
		StringBuffer sb = new StringBuffer();
		Map<String, String> contact = userInfo.getContact();
		if (contact != null) {
			String plain = contact.get("plain");
			String email = contact.get("email");
			String phone = contact.get("phone");
			if (!TextUtils.isEmpty(plain)) {
				sb.append("Name: ").append(plain).append("\n");
			}
			if (!TextUtils.isEmpty(email)) {
				sb.append("Email: ").append(email).append("\n");
			}
			if (!TextUtils.isEmpty(phone)) {
				sb.append("Phone: ").append(phone).append("\n");
			}
		}
		int age = userInfo.getAgeGroup();
		if (age >= 0)
			sb.append(
					"Age: "
							+ getResources().getStringArray(
									R.array.age_group_list)[age]).append("\n");
		String gender = userInfo.getGender();
		if (!TextUtils.isEmpty(gender))
			sb.append(gender).append("\n");
		if (sb.length() <= 0)
			return "";
		return sb.toString().substring(0, sb.length() - 1);
	}

	@SuppressWarnings("unused")
	private void updateUserContact(String key, String value) {
		Map<String, String> contact = userInfo.getContact();
		if (contact == null)
			contact = new HashMap<String, String>();
		contact.put(key, value);
		userInfo.setContact(contact);
	}

	private void sync() {
		mComversation.sync(new SyncListener() {

			@Override
			public void onSendUserReply(List<Reply> replys) {
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onReceiveDevReply(List<Reply> replyList) {
				dismissProgress();
				if (replyList == null || replyList.size() == 0)
					return;
				String content = "";
				if (replyList.size() == 1) {
					content = replyList.get(0).content;
				} else {
					content = replyList.size() + " feedback";
				}

				try {
					NotificationManager notificationManager = (NotificationManager) context
							.getSystemService(Context.NOTIFICATION_SERVICE);
					String tickerText = "feedback reply";
					Intent intentToLaunch = new Intent(context,
							FeedbackActivity.class);// 将CustomActivity替换成自定义Activity类名
					intentToLaunch
							.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					int requestCode = (int) SystemClock.uptimeMillis();
					PendingIntent contentIntent = PendingIntent.getActivity(
							context, requestCode, intentToLaunch,
							PendingIntent.FLAG_UPDATE_CURRENT);

					int smallIcon = context.getPackageManager().getPackageInfo(
							context.getPackageName(), 0).applicationInfo.icon;

					NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
							context).setSmallIcon(smallIcon)
							.setContentTitle(tickerText).setTicker(tickerText)
							.setContentText(content).setAutoCancel(true)
							.setContentIntent(contentIntent);
					notificationManager.notify(0, mBuilder.build());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void scrollToBottom() {
		list.setSelection(adapter.getCount());
	}

	@Override
	protected void initActionBar(TitleBar titleBar) {
		super.initActionBar(titleBar);
		LinkedHashMap<Integer, Integer> itemmap = new LinkedHashMap<Integer, Integer>();
		itemmap.put(R.string.comment, R.drawable.common_comment);
		itemmap.put(R.string.refresh, R.drawable.common_status_refresh);

		StatusPopUpWindow.StatusPopUpWindowItemClickListener menuListener = new StatusPopUpWindow.StatusPopUpWindowItemClickListener() {

			@Override
			public void onitemclick(int titleId) {
				GLog.sysout(context.getResources().getString(titleId) + "");
				switch (titleId) {
				case R.string.refresh:
					if (!isLoading())
						onRefreshStarted(null);
					break;
				default:
					ToastUtils.show(context, "TODO "
							+ context.getResources().getString(titleId));
					break;
				}
				titlebar.dissmissStatus();
			}
		};
		titlebar.setStatusItem(context, itemmap, menuListener);
	}

	@Override
	protected void onleftLayout() {
		finish();
	}

	@Override
	public void onRefreshStarted(View view) {
		showProgress();
		sync();
	}

}
