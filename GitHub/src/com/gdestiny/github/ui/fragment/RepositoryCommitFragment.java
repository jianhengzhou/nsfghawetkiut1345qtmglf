package com.gdestiny.github.ui.fragment;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.CommitAdapter;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.activity.CommitDetailActivity;
import com.gdestiny.github.ui.activity.RepositoryDetailActivity;
import com.gdestiny.github.ui.dialog.BranchDialog;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.view.ListPopupView;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.ViewUtils;

public class RepositoryCommitFragment extends
		BaseLoadPageFragment<RepositoryCommit, GitHubClient> {

	private Repository repository;

	private TextView branch;
	private String curBranch;
	private BranchDialog branchDialog;
	private ListPopupView listPopup;

	private CommitAdapter commitAdapter;

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_repository_commit,
				R.id.pull_refresh_layout, R.id.list);
	}

	@Override
	protected void initView() {

		branch = (TextView) findViewById(R.id.branch);
		branch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (branchDialog == null) {
					branchDialog = new BranchDialog(context, repository)
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									// TODO Auto-generated method stub
									String branch = branchDialog
											.getBranchList().get(position)
											.getName();
									if (!branch.equals(curBranch)) {
										curBranch = branch;
										onRefreshStarted(null);
									}
								}
							});
				}
				branchDialog.show();
			}
		});
		listPopup = (ListPopupView) findViewById(R.id.branch_popup);
		listPopup.bind(getMoreList());
	}

	@Override
	protected void initData() {
		repository = (Repository) context.getIntent().getSerializableExtra(
				Constants.Extra.REPOSITORY);
		curBranch = repository.getMasterBranch();
		execute(GitHubApplication.getClient());
		// 防止与其他页面重叠
		getPullToRefreshLayout().getHeaderTransformer()
				.setProgressbarVisibility(View.GONE);
	}

	@Override
	public void initStatusPopup(final TitleBar title) {
		// TODO Auto-generated method stub
		if (itemmap == null) {
			itemmap = new LinkedHashMap<Integer, Integer>();
			itemmap.put(R.string.star, R.drawable.common_star_grey);
			itemmap.put(R.string.fork, R.drawable.common_branch_grey);
			itemmap.put(R.string.contributors,
					R.drawable.common_own_people_grey);
			itemmap.put(R.string.share, R.drawable.common_share_grey);
			itemmap.put(R.string.refresh, R.drawable.common_status_refresh);
			itemmap.put(R.string.commit, R.drawable.common_status_refresh);
		}
		if (menuListener == null) {
			menuListener = new StatusPopUpWindow.StatusPopUpWindowItemClickListener() {

				@Override
				public void onitemclick(int titleId) {
					GLog.sysout(context.getResources().getString(titleId) + "");
					boolean dismiss = true;
					switch (titleId) {
					case R.string.refresh:
						if (isLoading()) {
							GLog.sysout("update is not complete");
							return;
						}
						onRefreshStarted(null);
						break;
					default:
						((RepositoryDetailActivity) context).onMenu(titleId);
						break;
					}
					if (dismiss)
						title.dissmissStatus();
				}
			};
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		super.onRefreshStarted(view);
		execute(GitHubApplication.getClient());
	}

	@Override
	public void newListAdapter() {
		commitAdapter = new CommitAdapter(context);
		commitAdapter.setDatas(getDatas());
		setBaseAdapter(commitAdapter);
	}

	@Override
	public void newPageData(GitHubClient params) {
		if (TextUtils.isEmpty(curBranch)) {
			try {
				curBranch = new RepositoryService(params).getRepository(
						repository).getMasterBranch();
			} catch (IOException e) {
				e.printStackTrace();
				curBranch = "master";
			}
			if (TextUtils.isEmpty(curBranch))
				curBranch = "master";
		}
		CommitService service = new CommitService(params);
		setDataPage(service.pageCommits(repository, curBranch, null,
				Constants.DEFAULT_PAGE_SIZE));
	}

	@Override
	public void onSuccess(List<RepositoryCommit> result) {
		super.onSuccess(result);
		ViewUtils.setVisibility(listPopup, View.VISIBLE, R.anim.alpha_in);
		branch.setText(curBranch);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		IntentUtils
				.create(context, CommitDetailActivity.class)
				.putExtra(Constants.Extra.REPOSITORY_COMMIT,
						getDatas().get(position))
				.putExtra(Constants.Extra.REPOSITORY, repository).start();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		return false;
	}

}
