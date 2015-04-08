package com.gdestiny.github.ui.activity;

import java.util.ArrayList;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.Milestone;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.IssueService;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseFragmentActivity;
import com.gdestiny.github.async.CollaboratorLoadTask;
import com.gdestiny.github.async.LabelLoadTask;
import com.gdestiny.github.async.MilestoneLoadTask;
import com.gdestiny.github.bean.IssueFilter;
import com.gdestiny.github.ui.view.LabelViewGroup;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.TimeUtils;
import com.gdestiny.github.utils.ViewUtils;

public class IssueFilterActivity extends BaseFragmentActivity implements
		OnClickListener, OnLongClickListener {

	@SuppressWarnings("unused")
	private Issue issue;
	private Repository repository;
	private IssueFilter filter;

	private RadioGroup stateGroup;
	private LabelViewGroup labelGroup;

	// Tab
	private View assigneeNone;
	private View milestoneNone;
	private View labelNone;

	private View milestone;
	private View assignee;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.act_issue_filter);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		stateGroup = (RadioGroup) findViewById(R.id.state_group);
		RadioButton rb = (RadioButton) stateGroup.getChildAt(0);
		rb.setChecked(true);

		stateGroup.check(R.id.state_open);
		stateGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == R.id.state_close)
					filter.put(IssueService.STATE_CLOSED);
				else
					filter.put(IssueService.STATE_OPEN);
			}
		});

		View assign = findViewById(R.id.assign_layout);
		assign.setOnClickListener(this);
		assign.setOnLongClickListener(this);
		View ms = findViewById(R.id.milestone_layout);
		ms.setOnClickListener(this);
		ms.setOnLongClickListener(this);
		View label = findViewById(R.id.label_layout);
		label.setOnClickListener(this);
		label.setOnLongClickListener(this);

		assigneeNone = findViewById(R.id.assing_none);
		milestoneNone = findViewById(R.id.milestone_none);
		labelNone = findViewById(R.id.label_none);

		milestone = findViewById(R.id.milestone_item);
		assignee = findViewById(R.id.assing_item);

		labelGroup = (LabelViewGroup) findViewById(R.id.label_viewgroup);
	}

	@Override
	protected void initData() {
		repository = (Repository) getIntent().getSerializableExtra(
				Constants.Extra.REPOSITORY);

		getTitlebar().setLeftLayout(repository.getOwner().getAvatarUrl(),
				"Issue Filter", repository.generateId());

		// init filter data
		filter = (IssueFilter) getIntent().getSerializableExtra(
				Constants.Extra.ISSUE_FILTER);
		if (filter == null)
			filter = new IssueFilter();
		if (filter.getState().equals(IssueService.STATE_OPEN)) {
			stateGroup.check(R.id.state_open);
		} else {
			stateGroup.check(R.id.state_close);
		}
		onMilestone(filter.getMilestone());
		onLabels(filter.getLabels());
		onAssignee(filter.getAssignee());
	}

	@Override
	protected void initActionBar(TitleBar titleBar) {
		// TODO Auto-generated method stub
		super.initActionBar(titleBar);
		titleBar.showRightBtn();
		ImageButton right = titleBar.getRightBtn();
		right.setImageResource(R.drawable.common_btn_ok);
		right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				IntentUtils.create(context)
						.putExtra(Constants.Extra.ISSUE_FILTER, filter)
						.setResultOk().finish();
			}
		});
	}

	@Override
	protected void onleftLayout() {
		IntentUtils.setResultCancle(context);
	}

	private void onLabels(ArrayList<Label> selected) {
		if (selected != null && !selected.isEmpty()) {
			ViewUtils.setVisibility(labelNone, View.GONE);
			ViewUtils.setVisibility(labelGroup, View.VISIBLE);
			filter.put(selected);
		} else {
			ViewUtils.setVisibility(labelNone, View.VISIBLE);
			ViewUtils.setVisibility(labelGroup, View.GONE);
		}
		labelGroup.setLabel(selected);
	}

	private void onMilestone(Milestone selected) {
		if (selected == null) {
			ViewUtils.setVisibility(milestoneNone, View.VISIBLE);
			ViewUtils.setVisibility(milestone, View.GONE);
			return;
		}

		filter.put(selected);

		ViewUtils.setVisibility(milestoneNone, View.GONE);
		ViewUtils.setVisibility(milestone, View.VISIBLE);
		milestone.setBackgroundResource(R.drawable.selector_border_blue);

		TextView title = (TextView) findViewById(R.id.milestone_title);
		TextView createAt = (TextView) findViewById(R.id.milestone_date);
		TextView creator = (TextView) findViewById(R.id.milestone_name);
		ImageView createIcon = (ImageView) findViewById(R.id.milestone_icon);
		TextView open = (TextView) findViewById(R.id.open);
		TextView close = (TextView) findViewById(R.id.close);
		// set data
		title.setText(selected.getTitle());
		if (selected.getState().equals(IssueService.STATE_CLOSED)) {
			title.getPaint().setFlags(
					android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
							| android.graphics.Paint.ANTI_ALIAS_FLAG);
		} else {
			title.getPaint().setFlags(android.graphics.Paint.ANTI_ALIAS_FLAG);
		}

		open.setText(selected.getOpenIssues() + "");
		close.setText("  " + selected.getClosedIssues() + "  ");
		close.getPaint().setFlags(
				android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
						| android.graphics.Paint.ANTI_ALIAS_FLAG);

		creator.setText(selected.getCreator().getLogin());
		createAt.setText(TimeUtils.getTime(selected.getCreatedAt().getTime()));

		ImageLoaderUtils.displayImage(selected.getCreator().getAvatarUrl(),
				createIcon, R.drawable.default_avatar,
				R.drawable.default_avatar, true);
		milestone.invalidate();
	}

	private void onAssignee(User selected) {
		if (selected == null) {
			ViewUtils.setVisibility(assigneeNone, View.VISIBLE);
			ViewUtils.setVisibility(assignee, View.GONE);
			return;
		}
		filter.put(selected);

		ViewUtils.setVisibility(assigneeNone, View.GONE);
		ViewUtils.setVisibility(assignee, View.VISIBLE);

		TextView name = (TextView) findViewById(R.id.name);
		ImageView icon = (ImageView) findViewById(R.id.icon);

		name.setText(selected.getLogin());
		ImageLoaderUtils.displayImage(selected.getAvatarUrl(), icon,
				R.drawable.default_avatar, R.drawable.default_avatar, true);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.assign_layout:
			new CollaboratorLoadTask(context, repository) {

				@Override
				public void onCollaborator(User selected) {
					onAssignee(selected);
				}
			}.execute();
			break;
		case R.id.milestone_layout:
			new MilestoneLoadTask(context, repository) {

				@Override
				public void onMilestone(Milestone selected) {
					IssueFilterActivity.this.onMilestone(selected);
				}
			}.putSelected(filter.getMilestone()).execute();
			break;
		case R.id.label_layout:
			new LabelLoadTask(context, repository) {

				@Override
				public void onLabels(ArrayList<Label> selected) {
					IssueFilterActivity.this.onLabels(selected);
				}
			}.putSelected(filter.getLabels()).execute();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.assign_layout:
			if (!filter.isAssigneeValid())
				return false;
			filter.clearAssignee();
			ViewUtils.setVisibility(assignee, View.GONE);
			ViewUtils.setVisibility(assigneeNone, View.VISIBLE);
			break;
		case R.id.milestone_layout:
			if (!filter.isMilestoneValid())
				return false;
			filter.clearMilestone();
			ViewUtils.setVisibility(milestone, View.GONE);
			ViewUtils.setVisibility(milestoneNone, View.VISIBLE);
			break;
		case R.id.label_layout:
			if (!filter.isLabelsValid())
				return false;
			filter.clearLabels();
			ViewUtils.setVisibility(labelGroup, View.GONE);
			ViewUtils.setVisibility(labelNone, View.VISIBLE);
			break;
		default:
			break;
		}
		AndroidUtils.vibrate(context, 100);
		return true;
	}
}
