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
		// ���ֲ�ͬ��Item����
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		// ��ȡ�����ظ�
		Reply reply = mComversation.getReplyList().get(position);
		if (Reply.TYPE_DEV_REPLY.equals(reply.type)) {
			// �����߻ظ�Item����
			return VIEW_TYPE_DEV;
		} else {
			// �û��������ظ�Item����
			return VIEW_TYPE_USER;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		// ��ȡ�����ظ�
		Reply reply = mComversation.getReplyList().get(position);
		if (convertView == null) {
			// ����Type�����������ز�ͬ��Item����
			if (Reply.TYPE_DEV_REPLY.equals(reply.type)) {
				// �����ߵĻظ�
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_reply_dev, null);
			} else {
				// �û��ķ������ظ�
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_reply_user, null);
			}

			// ����ViewHolder����ȡ����View
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

		// �������������
		// ����Reply������
		holder.replyContent.setText(reply.content);
		// ��App���棬���ڿ����ߵ�Reply����statusû������
		if (!Reply.TYPE_DEV_REPLY.equals(reply.type)) {
			// ����Reply��״̬������replyStateFailed��״̬
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

			// ����Reply��״̬������replyProgressBar��״̬
			if (Reply.STATUS_SENDING.equals(reply.status)) {
				holder.replyProgressBar.setVisibility(View.VISIBLE);
			} else {
				holder.replyProgressBar.setVisibility(View.GONE);
				// holder.replyProgressBar.setVisibility(View.VISIBLE);
			}
		}

		// �ظ���ʱ�����ݣ��������QQ����Reply֮�����100000ms��չʾʱ��
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