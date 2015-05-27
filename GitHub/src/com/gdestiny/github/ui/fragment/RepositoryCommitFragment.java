package com.gdestiny.github.ui.fragment;

import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.fragment.BaseLoadPageFragment;
import com.gdestiny.github.adapter.CommitAdapter;
import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.ui.activity.CommitDetailActivity;
import com.gdestiny.github.ui.activity.RepositoryDetailActivity;
import com.gdestiny.github.ui.dialog.BranchDialog;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.view.ListPopupView;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.CacheUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.ViewUtils;
import com.google.gson.reflect.TypeToken;

public class RepositoryCommitFragment extends
		BaseLoadPageFragment<RepositoryCommit, Void> {

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
										onRefresh();
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
	public String getCacheName() {
		return CacheUtils.DIR.COMMIT + repository.getName() + "#" + curBranch
				+ "#" + CacheUtils.NAME.LIST_COMMIT;
	}

	@Override
	protected void initData() {
		repository = (Repository) context.getIntent().getSerializableExtra(
				Constants.Extra.REPOSITORY);
		curBranch = repository.getMasterBranch();

		List<RepositoryCommit> list = CacheUtils.getCacheObject(getCacheName(),
				new TypeToken<List<RepositoryCommit>>() {
				}.getType());
		if (list != null) {
			setDatas(list);
			((CommitAdapter) getBaseAdapter()).setDatas(list);
		}

		execute();
	}

	@Override
	public void newListAdapter() {
		commitAdapter = new CommitAdapter(context);
		commitAdapter.setDatas(getDatas());
		setBaseAdapter(commitAdapter);
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
						onRefresh();
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
	public void onRefresh() {
		super.onRefresh();
		execute();
	}

	@Override
	public void newPageData(Void params) {
		setDataPage(GitHubConsole.getInstance().pageCommits(repository,
				curBranch));
	}

	@Override
	public void onSuccess(List<RepositoryCommit> result) {
		super.onSuccess(result);
		ViewUtils.setVisibility(listPopup, View.VISIBLE, R.anim.alpha_in);
		branch.setText(curBranch);
	}

	@Override
	public void onResultOk(int requestCode, Intent data) {
		super.onResultOk(requestCode, data);
		if (requestCode == Constants.Request.COMMIT_DETAIL) {
			int count = data.getIntExtra(Constants.Extra.COMMENT_COUNT, -1);
			int position = data.getIntExtra(Constants.Extra.POSITION, -1);
			if (count >= 0 && position >= 0) {
				commitAdapter.getDatas().get(position).getCommit()
						.setCommentCount(count);
				commitAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		IntentUtils
				.create(context, CommitDetailActivity.class)
				.putExtra(Constants.Extra.REPOSITORY_COMMIT,
						getDatas().get(position))
				.putExtra(Constants.Extra.POSITION, position)
				.putExtra(Constants.Extra.REPOSITORY, repository)
				.startForResult(RepositoryCommitFragment.this,
						Constants.Request.COMMIT_DETAIL);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		return false;
	}

}
