package com.gdestiny.github.ui.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.Reference;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.Tree;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.DataService;

import com.gdestiny.github.R;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.async.GitHubTask;
import com.gdestiny.github.ui.fragment.BaseLoadFragment;
import com.gdestiny.github.ui.fragment.FollowerFragment;
import com.gdestiny.github.ui.fragment.FollowingFragment;
import com.gdestiny.github.ui.fragment.RepositoryCodeFragment;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.TestUtils;
import com.gdestiny.github.utils.ToastUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class RepositoryDetailActivity extends BaseFragmentActivity {

	public static final String data = "repository";

	private Repository repository;
	private ViewPager viewpager;
	private RepositoryPageAdapter adapter;

	private List<BaseLoadFragment> fragments = new ArrayList<BaseLoadFragment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void setContentView() {
		setContentView(R.layout.act_repository_detail);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		viewpager = (ViewPager) findViewById(R.id.viewpager);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		repository = (Repository) getIntent().getSerializableExtra(data);
		titlebar.setLeftLayout(repository.getOwner().getAvatarUrl(),
				repository.getName());

		fragments.add(new RepositoryCodeFragment());
		fragments.add(new FollowerFragment());
		fragments.add(new FollowingFragment());
		
		adapter = new RepositoryPageAdapter(getSupportFragmentManager());
		viewpager.setAdapter(adapter);
		// getDetail();
	}

	private class RepositoryPageAdapter extends FragmentStatePagerAdapter {

		public RepositoryPageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			if (fragments == null)
				return 1;
			return fragments.size();
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return FragmentStatePagerAdapter.POSITION_NONE;
		}
	}

	@SuppressWarnings("unused")
	private void getDetail() {
		new GitHubTask<Tree>(new GitHubTask.TaskListener<Tree>() {

			@Override
			public void onPrev() {
				// TODO Auto-generated method stub

			}

			@Override
			public Tree onExcute(GitHubClient client) {
				// TODO Auto-generated method stub
				DataService dataService = new DataService(client);
				Tree tree = null;
				try {
					GLog.sysout(repository.getMasterBranch());
					Reference ref = dataService.getReference(repository,
							"heads/" + repository.getMasterBranch());
					Commit commit = dataService.getCommit(repository, ref
							.getObject().getSha());
					tree = dataService.getTree(repository, commit.getTree()
							.getSha(), true);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return tree;
			}

			@Override
			public void onSuccess(Tree result) {
				// TODO Auto-generated method stub
				ToastUtils.show(context, "onSuccess");
				IntentUtils.startTest(context, TestUtils.printTree(result));
			}

			@Override
			public void onError() {
				// TODO Auto-generated method stub
				ToastUtils.show(context, "onError");
			}
		}).execute(GitHubApplication.getClient());
	}

	@Override
	public void onBackPressed() {
		if (viewpager.getCurrentItem() == 0)
			if (((RepositoryCodeFragment) fragments.get(0)).onBackPressed())
				super.onBackPressed();
	}

	@Override
	protected void onleftLayout() {
		// TODO Auto-generated method stub
		finish();
	}

}
