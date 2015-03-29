package com.gdestiny.github.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Reply;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class FeedbackAdapter extends BaseAdapter {

	private static int VIEW_TYPE_DEV = 0;
	private static int VIEW_TYPE_USER = 1;

	private Conversation mComversation;
	private Context mContext;

	public FeedbackAdapter(Context mContext) {
		this.mContext = mContext;
	}

	public Conversation getComversation() {
		return mComversation;
	}

	public void setComversation(Conversation mComversation) {
		this.mComversation = mComversation;
	}

	@Override
	public int getCount() {
		return mComversation.getReplyList().size();
	}

	@Override
	public Object getItem(int arg0) {
		return mComversation.getReplyList().get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public int getViewTypeCount() {
		// 两种不同的Item布局
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		// 获取单条回复
		Reply reply = mComversation.getReplyList().get(position);
		if (Reply.TYPE_DEV_REPLY.equals(reply.type)) {
			// 开发者回复Item布局
			return VIEW_TYPE_DEV;
		} else {
			// 用户反馈、回复Item布局
			return VIEW_TYPE_USER;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		// 获取单条回复
		Reply reply = mComversation.getReplyList().get(position);
		if (convertView == null) {
			// 根据Type的类型来加载不同的Item布局
			if (Reply.TYPE_DEV_REPLY.equals(reply.type)) {
				// 开发者的回复
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_reply_dev, null);
			} else {
				// 用户的反馈、回复
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_reply_user, null);
			}

			// 创建ViewHolder并获取各种View
			holder = new ViewHolder();
			holder.replyContent = (TextView) convertView
					.findViewById(R.id.fb_reply_content);
			holder.replyProgressBar = (CircularProgressBar) convertView
					.findViewById(R.id.fb_reply_progressBar);
			holder.replyStateFailed = (ImageView) convertView
					.findViewById(R.id.fb_reply_state_failed);
			holder.replyDate = (TextView) convertView
					.findViewById(R.id.fb_reply_date);
			holder.userIcon = (ImageView) convertView
					.findViewById(R.id.user_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 以下是填充数据
		// 设置Reply的内容
		holder.replyContent.setText(reply.content);
		// 在App界面，对于开发者的Reply来讲status没有意义
		if (!Reply.TYPE_DEV_REPLY.equals(reply.type)) {
			// 根据Reply的状态来设置replyStateFailed的状态
			try {
				ImageLoaderUtils.displayImage(GitHubApplication.getUser()
						.getAvatarUrl(), holder.userIcon,
						R.drawable.default_avatar, R.drawable.default_avatar,
						true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (Reply.STATUS_NOT_SENT.equals(reply.status)) {
				holder.replyStateFailed.setVisibility(View.VISIBLE);
			} else {
				holder.replyStateFailed.setVisibility(View.GONE);
				// holder.replyStateFailed.setVisibility(View.VISIBLE);
			}

			// 根据Reply的状态来设置replyProgressBar的状态
			if (Reply.STATUS_SENDING.equals(reply.status)) {
				holder.replyProgressBar.setVisibility(View.VISIBLE);
			} else {
				holder.replyProgressBar.setVisibility(View.GONE);
				// holder.replyProgressBar.setVisibility(View.VISIBLE);
			}
		}

		// 回复的时间数据，这里仿照QQ两条Reply之间相差100000ms则展示时间
		Date replyTime = new Date(reply.created_at);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		holder.replyDate.setText(sdf.format(replyTime));
		if (position > 0) {
			Reply lastReply = mComversation.getReplyList().get(position - 1);
			if (reply.created_at - lastReply.created_at > 100000) {
				holder.replyDate.setVisibility(View.VISIBLE);
			} else {
				holder.replyDate.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	class ViewHolder {
		TextView replyContent;
		CircularProgressBar replyProgressBar;
		ImageView replyStateFailed;
		TextView replyDate;
		ImageView userIcon;
	}
}