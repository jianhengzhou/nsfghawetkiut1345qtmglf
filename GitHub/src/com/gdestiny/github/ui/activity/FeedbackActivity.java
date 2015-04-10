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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseLoadFragmentActivity;
import com.gdestiny.github.abstracts.async.SimpleUpdateTask;
import com.gdestiny.github.adapter.FeedbackAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.CommonUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.ToastUtils;
import com.gdestiny.github.utils.ViewUtils;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.SyncListener;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Reply;
import com.umeng.fb.model.UserInfo;

public class FeedbackActivity extends BaseLoadFragmentActivity<Void, Void> {

	private static final String PRE_NAME = "umeng_feedback_conversations";
	@SuppressWarnings("unused")
	private static final String USER_PRE_NAME = "umeng_feedback_user_info";
	private EditText inputEdit;
	private TextView infoText;

	private ListView list;
	private FeedbackAdapter adapter;

	private FeedbackAgent agent;
	private Conversation mComversation;
	private UserInfo userInfo;
	private String infoString;
	private String infoStringBackup;

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
		View infoView = LayoutInflater.from(context).inflate(
				R.layout.layout_feedback_header, null);
		infoText = (TextView) infoView.findViewById(R.id.fb_userinfo);
		list.addHeaderView(infoView);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					IntentUtils
							.create(context, FeedbackInfoActivity.class)
							.putExtra(Constants.Extra.USER_INFO, infoString)
							.startForResult(context,
									Constants.Request.USER_INFO);
				}
			}
		});

		adapter = new FeedbackAdapter(context);
		adapter.setComversation(mComversation);
		list.setAdapter(adapter);
		scrollToBottom();

		inputEdit = (EditText) findViewById(R.id.fb_send_content);
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
							onUserInfo();
						}
					}
				});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		inputEdit.requestFocus();
		scrollToBottom();
	}

	@Override
	protected void initData() {
		User user = GitHubApplication.getUser();
		infoString = userInfoToString(userInfo);
		infoStringBackup = new String(infoString);

		String info = infoToShow(infoString);
		if (user != null) {
			getTitlebar().setLeftLayout(user.getAvatarUrl(), "Feedback",
					user.getLogin());
			if ("Contact: N/A".equals(info)) {
				StringBuffer sb = new StringBuffer();
				sb.append("Name: ").append(user.getLogin());
				updateUserContact("plain", user.getLogin());
				if (!TextUtils.isEmpty(user.getEmail())) {
					sb.append("\n").append("Email: ").append(user.getEmail());
					updateUserContact("email", user.getEmail());
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

	private void onUserInfo() {
		if (generateUserInfo(infoString)) {
			agent.setUserInfo(userInfo);
			GLog.sysout("update userinfo");
			new SimpleUpdateTask(new SimpleUpdateTask.UpdateListener() {

				@Override
				public void onSuccess() {
					sync();
					// agent.sync();
				}

				@Override
				public void onPrev() {

				}

				@Override
				public void onExcute() {
					GLog.sysout("result:" + agent.updateUserInfo());
				}
			}).execute();
			return;
		} else {
			sync();
			GLog.sysout("no need to update userinfo");
		}
	}

	private void scrollToBottom() {
		list.setSelection(adapter.getCount());
	}

	@Override
	public void onResultOk(int requestCode, Intent data) {
		super.onResultOk(requestCode, data);
		if (requestCode == Constants.Request.USER_INFO) {
			String info = data.getStringExtra(Constants.Extra.USER_INFO);
			if (!TextUtils.isEmpty(info)) {
				infoString = info;
				infoText.setText(infoToShow(info));
			}
		}
	}

	private void updateUserContact(String key, String value) {
		GLog.sysout("update:" + key + "," + value);
		Map<String, String> contact = userInfo.getContact();
		if (contact == null)
			contact = new HashMap<String, String>();
		contact.put(key, value);
		userInfo.setContact(contact);
	}

	private String userInfoToString(UserInfo info) {
		if (info == null)
			return "0#N/A#N/A#N/A#N/A";
		StringBuffer sb = new StringBuffer();
		sb.append(info.getAgeGroup());
		sb.append("#").append(CommonUtils.nullToNA(info.getGender()));
		Map<String, String> contact = userInfo.getContact();
		if (contact != null) {
			sb.append("#").append(CommonUtils.nullToNA(contact.get("plain")));
			sb.append("#").append(CommonUtils.nullToNA(contact.get("email")));
			sb.append("#").append(CommonUtils.nullToNA(contact.get("phone")));
		}
		return sb.toString();
	}

	private String infoToShow(String info) {
		StringBuilder sb = new StringBuilder();
		if (info != null) {
			String[] split = info.split("#");
			if (split.length == 5) {
				int age = 0;
				String gender = split[1];
				String name = split[2];
				String email = split[3];
				String phone = split[4];
				try {
					age = Integer.valueOf(split[0]);
				} catch (Exception ex) {
					ex.printStackTrace();
					age = 0;
				}
				if (age > 0)
					sb.append("Age: ")
							.append(getResources().getStringArray(
									R.array.age_group_list)[age]).append("\n");
				if (!"N/A".equals(gender))
					sb.append("Gender: ").append(gender).append("\n");
				if (!"N/A".equals(name))
					sb.append("Name: ").append(name).append("\n");
				if (!"N/A".equals(email))
					sb.append("Email: ").append(email).append("\n");
				if (!"N/A".equals(phone))
					sb.append("Phone: ").append(phone).append("\n");

			}
		}
		if (sb.length() <= 0)
			return "Contact: N/A";
		return sb.toString().substring(0, sb.length() - 1);
	}

	private boolean generateUserInfo(String info) {
		if (info.equals(infoStringBackup))
			return false;
		if (info != null) {
			GLog.sysout(info);
			String[] split = info.split("#");
			if (split.length == 5) {
				int age = 0;
				String gender = split[1];
				String name = split[2];
				String email = split[3];
				String phone = split[4];
				try {
					age = Integer.valueOf(split[0]);
				} catch (Exception ex) {
					ex.printStackTrace();
					age = 0;
				}
				userInfo.setAgeGroup(age);
				if (!"N/A".equals(gender))
					userInfo.setGender(gender);
				Map<String, String> contact = new HashMap<String, String>();
				if (!"N/A".equals(name))
					contact.put("plain", name);
				if (!"N/A".equals(email))
					contact.put("email", email);
				if (!"N/A".equals(phone))
					contact.put("phone", phone);
				userInfo.setContact(contact);
			}
		}
		infoStringBackup = new String(info);
		return true;
	}

	@Override
	protected void initActionBar(TitleBar titleBar) {
		super.initActionBar(titleBar);
		LinkedHashMap<Integer, Integer> itemmap = new LinkedHashMap<Integer, Integer>();
		itemmap.put(R.string.clear, R.drawable.common_clear);
		itemmap.put(R.string.refresh, R.drawable.common_status_refresh);

		StatusPopUpWindow.StatusPopUpWindowItemClickListener menuListener = new StatusPopUpWindow.StatusPopUpWindowItemClickListener() {

			@Override
			public void onitemclick(int titleId) {
				GLog.sysout(context.getResources().getString(titleId) + "");
				switch (titleId) {
				case R.string.refresh:
					if (!isLoading())
						onRefresh();
					break;
				case R.string.clear:
					if (AndroidUtils.FileManager.clearPreference(context,
							PRE_NAME)) {
						// if (AndroidUtils.FileManager.clearPreference(context,
						// USER_PRE_NAME)) {
						// infoString = "0#N/A#N/A#N/A#N/A";
						// infoText.setText("Contact: N/A");
						// }
						mComversation = agent.getDefaultConversation();
						adapter.setComversation(mComversation);
						adapter.notifyDataSetChanged();
					}
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
	public void onRefresh() {
		showProgress();
		sync();
	}

}
