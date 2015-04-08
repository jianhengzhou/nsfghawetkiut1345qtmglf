package com.gdestiny.github.ui.activity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.User;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseLoadFragmentActivity;
import com.gdestiny.github.adapter.GistItemAdapter;
import com.gdestiny.github.async.DeleteGistCommentTask;
import com.gdestiny.github.async.DeleteGistTask;
import com.gdestiny.github.async.ForkGistTask;
import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.async.StarGistTask;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.dialog.StatusPopWindowItem;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.CommonUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.TimeUtils;
import com.gdestiny.github.utils.ViewUtils;

public class GistDetailActivity extends
		BaseLoadFragmentActivity<Void, List<Comment>> {

	private Gist gist;
	private ListView commentList;
	private GistItemAdapter gistItemAdapter;
	private List<Comment> comments;

	private View stickComment;
	private View detailLayout;
	private boolean isStar = false;

	private boolean hasChange = false;
	private int position;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.act_gist_detail, R.id.pull_refresh_layout);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		commentList = (ListView) findViewById(R.id.list);

		stickComment = findViewById(R.id.layout_comment);
		stickComment
				.setBackgroundResource(R.drawable.selector_item_grey_to_blue);
		View.OnClickListener stickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				openComment();
			}
		};
		stickComment.setOnClickListener(stickListener);

		gistItemAdapter = new GistItemAdapter(context);
		gistItemAdapter.setOnListener(new GistItemAdapter.OnListener() {

			@Override
			public void onEdit(int position, Comment comment) {
				IntentUtils
						.create(context, EditGistCommentActivity.class)
						.putExtra(Constants.Extra.COMMENT, comment)
						.putExtra(Constants.Extra.POSITION, position)
						.putExtra(Constants.Extra.GIST, gist)
						.startForResult(GistDetailActivity.this,
								Constants.Request.EDIT_COMMENT);
			}

			@Override
			public void onDelete(final Comment comment) {
				new DeleteGistCommentTask(context, gist.getId(), comment) {

					@Override
					public void onSuccess(Boolean result) {
						super.onSuccess(result);
						TextView commentText = (TextView) detailLayout
								.findViewById(R.id.comment);
						gist.setComments(gist.getComments() - 1);
						commentText.setText(gist.getComments() + "");

						hasChange = true;

						comments.remove(comment);
						gistItemAdapter.notifyDataSetChanged();
					}
				}.execute();
			}
		});
		commentList.setAdapter(gistItemAdapter);
		commentList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (firstVisibleItem == 0) {
					ViewUtils.setVisibility(stickComment, View.GONE);
				} else {
					ViewUtils.setVisibility(stickComment, View.VISIBLE);
				}
			}
		});
		commentList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0)
					return;
				IntentUtils
						.create(context, GistFileActivity.class)
						.putExtra(Constants.Extra.GIST, gist)
						.putExtra(
								Constants.Extra.FILE_NAME,
								gistItemAdapter.getFile(position - 1)
										.getFilename()).start();
			}
		});

		detailLayout = LayoutInflater.from(context).inflate(
				R.layout.layout_gist_detail, null);
		detailLayout.findViewById(R.id.btn_comment).setOnClickListener(
				stickListener);
		commentList.addHeaderView(detailLayout);
	}

	private void updateDetail(Gist gist) {
		ImageView icon = (ImageView) detailLayout
				.findViewById(R.id.detail_icon);
		TextView name = (TextView) findViewById(R.id.detail_name);

		User user = gist.getUser();
		if (user != null) {
			ImageLoaderUtils.displayImage(gist.getUser().getAvatarUrl(), icon,
					R.drawable.default_avatar, R.drawable.default_avatar, true);

			name.setText(gist.getUser().getLogin());
		} else {
			ImageLoaderUtils.displayImage("", icon,
					R.drawable.common_anonymous, R.drawable.common_anonymous,
					true);
			name.setText(R.string.anonymous);
		}

		TextView createdDate = (TextView) findViewById(R.id.created_date);
		createdDate.setText("created at "
				+ TimeUtils.getTime(gist.getCreatedAt().getTime()));

		TextView updateDate = (TextView) findViewById(R.id.updated_date);
		updateDate.setText("updated at "
				+ TimeUtils.getTime(gist.getUpdatedAt().getTime()));

		TextView comment = (TextView) findViewById(R.id.comment);
		comment.setText(gist.getComments() + "");

		TextView content = (TextView) findViewById(R.id.content);
		String description = gist.getDescription();
		if (TextUtils.isEmpty(description)) {
			content.setText("No Description");
		} else {
			content.setText(description);
		}

		TextView sha = (TextView) findViewById(R.id.sha);
		sha.setText(gist.getId());
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		gist = (Gist) getIntent().getSerializableExtra(Constants.Extra.GIST);
		position = getIntent().getIntExtra(Constants.Extra.POSITION, -1);

		User user = gist.getUser();
		if (user != null) {
			getTitlebar().setLeftLayout(user.getAvatarUrl(), gist.getId(),
					user.getLogin());
		} else {
			getTitlebar().setTitleText(gist.getId())
					.setTitleTextSecondly(R.string.anonymous)
					.setTitleIcon(R.drawable.common_anonymous_round);
		}
		updateFiles();
		updateDetail(gist);

		if (CommonUtils.isAuthUser(gist.getUser()))
			getTitlebar().getStatusPopup().addItem(context, 0, R.string.delete,
					R.drawable.common_delete);

		execute();
	}

	private void updateFiles() {
		Map<String, GistFile> files = gist.getFiles();
		if (files != null && !files.isEmpty()) {
			List<GistFile> fileList = new ArrayList<GistFile>();
			for (GistFile gf : files.values())
				fileList.add(gf);
			gistItemAdapter.setFiles(fileList);
		}
	}

	private void openComment() {
		IntentUtils.create(context, NewGistCommentActivity.class)
				.putExtra(Constants.Extra.GIST, gist)
				.startForResult(this, Constants.Request.GIST_COMMENT);
	}

	@Override
	public List<Comment> onBackground(Void params) throws Exception {
		isStar = GitHubConsole.getInstance().isStarred(gist.getId());
		return GitHubConsole.getInstance().getGistComment(gist.getId());
	}

	@Override
	public void onSuccess(List<Comment> result) {
		super.onSuccess(result);
		refreshStarPopup();
		comments = result;
		gistItemAdapter
				.setIsCollaborator(CommonUtils.isAuthUser(gist.getUser()));
		gistItemAdapter.setDatas(comments);

	}

	@Override
	public void onRefresh() {
		execute();
	}

	private void refreshStarPopup() {
		StatusPopWindowItem item = null;
		if (CommonUtils.isAuthUser(gist.getUser()))
			item = getTitlebar().getStatusPopup().getAction(1);
		else
			item = getTitlebar().getStatusPopup().getAction(0);
		if (isStar)
			item.mTitle = getResources().getString(R.string.unstar);
		else
			item.mTitle = getResources().getString(R.string.star);
	}

	@Override
	public void finish() {
		if (hasChange) {
			IntentUtils.create(context).putExtra(Constants.Extra.GIST, gist)
					.putExtra(Constants.Extra.POSITION, position).setResultOk();
		}
		super.finish();
	}

	@Override
	public void onResultOk(int requestCode, Intent data) {
		super.onResultOk(requestCode, data);
		if (requestCode == Constants.Request.EDIT_COMMENT) {
			Comment comment = (Comment) data
					.getSerializableExtra(Constants.Extra.COMMENT);
			int position = data.getIntExtra(Constants.Extra.POSITION, -1);
			if (comment != null && position != -1) {
				gistItemAdapter.getDatas().get(position)
						.setBodyHtml(comment.getBodyHtml());
				gistItemAdapter.notifyDataSetChanged();
				// commentAdapter.getDatas().remove(position);
				// commentAdapter.getDatas().add(position, comment);
				// commentAdapter.notifyDataSetChanged();
			}
		} else if (requestCode == Constants.Request.GIST_COMMENT) {
			Comment comment = (Comment) data
					.getSerializableExtra(Constants.Extra.COMMENT);
			if (comment != null) {
				gist.setComments(gist.getComments() + 1);
				TextView commentText = (TextView) detailLayout
						.findViewById(R.id.comment);
				commentText.setText(gist.getComments() + "");
				hasChange = true;

				comments.add(comment);
				gistItemAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	protected void initActionBar(final TitleBar titleBar) {
		super.initActionBar(titleBar);
		LinkedHashMap<Integer, Integer> itemmap = new LinkedHashMap<Integer, Integer>();

		itemmap.put(R.string.star, R.drawable.common_star_grey);
		itemmap.put(R.string.fork, R.drawable.common_branch_grey);
		itemmap.put(R.string.share, R.drawable.common_share_grey);
		itemmap.put(R.string.comment, R.drawable.common_comment);
		itemmap.put(R.string.refresh, R.drawable.common_status_refresh);
		StatusPopUpWindow.StatusPopUpWindowItemClickListener menuListener = new StatusPopUpWindow.StatusPopUpWindowItemClickListener() {

			@Override
			public void onitemclick(int titleId) {
				switch (titleId) {
				case R.string.delete:
					new DeleteGistTask(context, gist.getId()) {

						@Override
						public void onSuccess(Boolean result) {
							super.onSuccess(result);
							IntentUtils
									.create(context)
									.putExtra(Constants.Extra.POSITION,
											position).setResultOk().finish();
						}
					}.execute();
					break;
				case R.string.star:
					new StarGistTask(context, isStar, gist.getId()) {

						@Override
						public void onSuccess(Boolean result) {
							super.onSuccess(result);
							isStar = !isStar;
							refreshStarPopup();
						}
					}.execute();
					break;
				case R.string.fork:
					new ForkGistTask(context, gist.getId())
							.execute();
					break;
				case R.string.share:
					AndroidUtils.share(context, "Gist",
							"https://gist.github.com/" + gist.getId());
					break;
				case R.string.comment:
					openComment();
					break;
				case R.string.refresh:
					if (isLoading()) {
						GLog.sysout("update is not complete");
						return;
					}
					onRefresh();
					break;
				default:
					break;
				}
				titleBar.dissmissStatus();
			}
		};
		titleBar.setStatusItem(context, itemmap, menuListener);
	}

	@Override
	protected void onleftLayout() {
		finish();
	}
}
