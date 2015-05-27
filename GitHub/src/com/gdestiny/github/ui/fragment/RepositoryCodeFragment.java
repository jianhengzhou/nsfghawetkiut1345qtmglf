package com.gdestiny.github.ui.fragment;

import java.util.LinkedHashMap;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.Tree;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.fragment.BaseLoadFragment;
import com.gdestiny.github.adapter.CodeTreeAdapter;
import com.gdestiny.github.async.GitHubConsole;
import com.gdestiny.github.bean.CodeTree;
import com.gdestiny.github.ui.activity.CodeFileActivity;
import com.gdestiny.github.ui.activity.RepositoryDetailActivity;
import com.gdestiny.github.ui.dialog.BranchDialog;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.view.ListPopupView;
import com.gdestiny.github.ui.view.PathView;
import com.gdestiny.github.ui.view.PathView.PathClickListener;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.CacheUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.ViewUtils;

public class RepositoryCodeFragment extends BaseLoadFragment<Void, CodeTree> {

	public static final String EXTRA_CODE = "codetree";

	private Repository repository;

	private ListView codeList;
	private CodeTreeAdapter codeAdapter;

	private CodeTree currCodeTree;
	private PathView pathView;
	private ListPopupView listPopup;
	private TextView branch;
	private String curBranch;
	private BranchDialog branchDialog;

	@Override
	protected void setCurrentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentView(inflater, R.layout.frag_repository_code,
				R.id.pull_refresh_layout);
		if (savedInstanceState != null) {
			currCodeTree = (CodeTree) savedInstanceState
					.getSerializable(EXTRA_CODE);
		}
	}

	@Override
	public String getCacheName() {
		return CacheUtils.DIR.CODE_TREE + repository.getName() + "#"
				+ curBranch + "#" + CacheUtils.NAME.CODE_TREE;
	}

	@Override
	protected void initView() {

		codeList = (ListView) findViewById(R.id.list);
		codeAdapter = new CodeTreeAdapter(context);
		codeList.setAdapter(codeAdapter);
		codeList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position < currCodeTree.getTreeCount()) {
					currCodeTree = currCodeTree.subTree.get(position);
					codeAdapter.setCodeTree(currCodeTree);
					codeList.setSelection(0);
					pathView.add(currCodeTree.currEntry.getPath());
				} else {
					IntentUtils
							.create(context, CodeFileActivity.class)
							.putExtra(
									Constants.Extra.CODE_ENTRY,
									currCodeTree.subEntry.get(position
											- currCodeTree.getTreeCount()))
							.putExtra(Constants.Extra.REPOSITORY, repository)
							.start();
				}
			}
		});

		pathView = (PathView) findViewById(R.id.pathview);
		pathView.setPathListener(new PathClickListener() {

			@Override
			public void onPathClick(String path) {
				setCodeData(path);
			}
		});

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
										execute();
									}
								}
							});
				}
				branchDialog.show();
			}
		});
		listPopup = (ListPopupView) findViewById(R.id.branch_popup);
		listPopup.bind(codeList);

	}

	@Override
	protected void initData() {
		repository = (Repository) context.getIntent().getSerializableExtra(
				Constants.Extra.REPOSITORY);

		curBranch = repository.getMasterBranch();

		setLoadCache(CacheUtils.contain(getCacheName()));

		if (currCodeTree == null)
			execute();
		else {
			ViewUtils.setVisibility(listPopup, View.VISIBLE, R.anim.alpha_in);
			branch.setText(curBranch);
			codeAdapter.setCodeTree(currCodeTree);
			if (!currCodeTree.name.equals(CodeTree.ROOT))
				pathView.resetView(currCodeTree.currEntry.getPath());
		}
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
		title.setStatusItem(context, itemmap, menuListener);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(EXTRA_CODE, currCodeTree);
	}

	@Override
	public CodeTree onBackground(Void params) throws Exception {
		Tree tree = null;
		if (isLoadCache()) {
			tree = CacheUtils.getCacheObject(getCacheName(), Tree.class);
			setLoadCache(false);
		} else {
			tree = GitHubConsole.getInstance().getTree(repository, curBranch);
			CacheUtils.cacheObject(getCacheName(), tree);
		}

		return CodeTree.toCodeTree(tree);
	}

	@Override
	public void onSuccess(CodeTree result) {
		super.onSuccess(result);
		currCodeTree = result;
		showData();
	}

	private void showData() {
		codeAdapter.setCodeTree(currCodeTree);
		ViewUtils.setVisibility(listPopup, View.VISIBLE, R.anim.alpha_in);
		branch.setText(curBranch);
	}

	public boolean onBackPressed() {
		if (currCodeTree == null || currCodeTree.name.equals(CodeTree.ROOT))
			return true;
		setCodeData(currCodeTree.parent);
		return false;
	}

	private void setCodeData(String path) {
		currCodeTree = CodeTree.allFolder.get(path);
		codeAdapter.setCodeTree(currCodeTree);
		codeList.setSelection(0);
		// ¸üÐÂpathview
		if (currCodeTree.currEntry == null)
			pathView.resetPath(CodeTree.ROOT);
		else
			pathView.resetPath(currCodeTree.currEntry.getPath());
	}

	@Override
	public void onRefresh() {
		execute();
	}

}
