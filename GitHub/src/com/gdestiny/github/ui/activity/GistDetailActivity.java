package com.gdestiny.github.ui.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.GistService;

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
import com.gdestiny.github.adapter.GistItemAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.utils.CommonUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.TimeUtils;
import com.gdestiny.github.utils.ViewUtils;

public class GistDetailActivity extends
		BaseLoadFragmentActivity<GitHubClient, List<Comment>> {

	private Gist gist;
	private ListView commentList;
	private GistItemAdapter gistItemAdapter;
	private List<Comment> comments;

	private View stickComment;
	private View detailLayout;

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
				// TODO Auto-generated method stub
			}

			@Override
			public void onDelete(final Comment comment) {
				// TODO Auto-generated method stub
				GLog.sysout("onDelete");
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
		execute(GitHubApplication.getClient());
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
		IntentUtils.create(context, NewCommentActivity.class)
				.putExtra(Constants.Extra.GIST, gist).start();
	}

	@Override
	public List<Comment> onBackground(GitHubClient params) throws Exception {
		GistService service = new GistService(params);
		return service.getComments(gist.getId());
	}

	@Override
	public void onSuccess(List<Comment> result) {
		super.onSuccess(result);
		comments = result;
		gistItemAdapter
				.setIsCollaborator(CommonUtils.isAuthUser(gist.getUser()));
		gistItemAdapter.setDatas(comments);

	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		execute(GitHubApplication.getClient());
	}

	@Override
	protected void onleftLayout() {
		finish();
	}
}
