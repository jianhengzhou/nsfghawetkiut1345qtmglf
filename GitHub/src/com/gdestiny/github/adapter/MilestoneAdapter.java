package com.gdestiny.github.adapter;

import java.util.List;

import org.eclipse.egit.github.core.Milestone;
import org.eclipse.egit.github.core.service.IssueService;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.TimeUtils;
import com.gdestiny.github.utils.ViewUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MilestoneAdapter extends BaseAdapter {

	private Context context;
	private List<Milestone> milestones;
	private Milestone selectedMilestone;

	public MilestoneAdapter(Context context) {
		this.context = context;
	}

	public MilestoneAdapter(Context context, List<Milestone> milestones,
			Milestone selectedMilestone) {
		this.context = context;
		this.milestones = milestones;
		this.selectedMilestone = selectedMilestone;
	}

	public MilestoneAdapter(Context context, List<Milestone> milestones) {
		this.context = context;
		this.milestones = milestones;
	}

	@Override
	public int getCount() {
		if (milestones != null)
			return milestones.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_milestone, null);
			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		Milestone ms = milestones.get(position);

		holder.title.setText(ms.getTitle());
		if (ms.getState().equals(IssueService.STATE_CLOSED)) {
			holder.title.getPaint().setFlags(
					android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
							| android.graphics.Paint.ANTI_ALIAS_FLAG);
		} else {
			holder.title.getPaint().setFlags(
					android.graphics.Paint.ANTI_ALIAS_FLAG);
		}

		holder.open.setText(ms.getOpenIssues() + "");
		holder.close.setText("  " + ms.getClosedIssues() + "  ");
		holder.close.getPaint().setFlags(
				android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
						| android.graphics.Paint.ANTI_ALIAS_FLAG);

		holder.description.setText(ms.getDescription());
		holder.creator.setText(ms.getCreator().getLogin());
		holder.createAt.setText(TimeUtils.getTime(ms.getCreatedAt().getTime()));

		ImageLoaderUtils.displayImage(ms.getCreator().getAvatarUrl(),
				holder.createIcon, R.drawable.default_avatar,
				R.drawable.default_avatar, true);

		// test
		// if (position == 3)
		// selectedMilestone = ms;
		if (selectedMilestone == null) {
			ViewUtils.setVisibility(holder.check, View.GONE);
		} else if (selectedMilestone.getNumber() == ms.getNumber()) {
			ViewUtils.setVisibility(holder.check, View.VISIBLE);
			holder.check.setImageResource(R.drawable.common_check);
		} else {
			ViewUtils.setVisibility(holder.check, View.VISIBLE);
			holder.check.setImageBitmap(null);
		}
		return convertView;
	}

	public List<Milestone> getMilestones() {
		return milestones;
	}

	public void setMilestones(List<Milestone> milestones) {
		this.milestones = milestones;
	}

	public Milestone getSelectedMilestone() {
		return selectedMilestone;
	}

	public MilestoneAdapter setSelectedMilestone(Milestone selectedMilestone) {
		this.selectedMilestone = selectedMilestone;
		return this;
	}

	private class Holder {

		ImageView check;

		TextView title;
		TextView open;
		TextView close;
		TextView description;

		TextView creator;
		ImageView createIcon;

		TextView createAt;

		public Holder(View v) {
			check = (ImageView) v.findViewById(R.id.selected);

			title = (TextView) v.findViewById(R.id.milestone_title);
			createAt = (TextView) v.findViewById(R.id.milestone_date);
			description = (TextView) v.findViewById(R.id.description);
			creator = (TextView) v.findViewById(R.id.milestone_name);
			createIcon = (ImageView) v.findViewById(R.id.milestone_icon);
			open = (TextView) v.findViewById(R.id.open);
			close = (TextView) v.findViewById(R.id.close);
		}
	}
}
