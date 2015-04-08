package com.gdestiny.github.ui.activity;

import org.eclipse.egit.github.core.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseFragmentActivity;
import com.gdestiny.github.app.GitHubApplication;
import com.gdestiny.github.ui.dialog.MaterialDialog;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.CommonUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.IntentUtils;
import com.gdestiny.github.utils.ViewUtils;

public class FeedbackInfoActivity extends BaseFragmentActivity implements
		View.OnClickListener {

	private String[] ages;
	private int curAgeGroup = 0;
	private TextView age;
	private EditText name;
	private EditText email;
	private EditText phone;
	private RadioGroup genderGroup;
	private String gender;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.act_feedback_info);
		ages = getResources().getStringArray(R.array.age_group_list);
	}

	@Override
	protected void initView() {
		age = (TextView) findViewById(R.id.age_spin);
		age.setOnClickListener(this);

		name = (EditText) findViewById(R.id.name);
		email = (EditText) findViewById(R.id.e_mail);
		phone = (EditText) findViewById(R.id.p_hone);
		genderGroup = (RadioGroup) findViewById(R.id.state_group);
		genderGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.state_male)
					gender = "male";
				else if (checkedId == R.id.state_female)
					gender = "female";
			}
		});
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		String info = getIntent().getStringExtra(Constants.Extra.USER_INFO);
		GLog.sysout("info:" + info);
		// info = "3#gender#name#email#phone";
		if (info != null) {
			String[] split = info.split("#");
			if (split.length == 5) {
				try {
					curAgeGroup = Integer.valueOf(split[0]);
					if (curAgeGroup < 0)
						age.setText(ages[0]);
					else
						age.setText(ages[curAgeGroup]);
				} catch (Exception ex) {
					ex.printStackTrace();
					curAgeGroup = 0;
					age.setText(ages[0]);
				}
				if ("male".equals(split[1])) {
					genderGroup.check(R.id.state_male);
				} else if ("female".equals(split[1])) {
					genderGroup.check(R.id.state_female);
				}
				name.setText(CommonUtils.NAToNull(split[2]));
				email.setText(CommonUtils.NAToNull(split[3]));
				phone.setText(CommonUtils.NAToNull(split[4]));
			}
		}
		User user = GitHubApplication.getUser();
		if (user != null) {
			getTitlebar().setLeftLayout(user.getAvatarUrl(), "Feedback",
					user.getLogin());
		}
	}

	private void showAgeDialog() {
		MaterialDialog ageDialog = new MaterialDialog(context);
		ageDialog.setTitle("Age").setCanceledOnTouchOutside(true);
		ageDialog.initListView();

		AgeAdapter adapter = new AgeAdapter();
		ageDialog.getmListView().setAdapter(adapter);
		ageDialog.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				age.setText(ages[position]);
				curAgeGroup = position;
			}
		}).show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.age_spin:
			showAgeDialog();
			break;
		default:
			break;
		}
	}

	/**
	 * age#gender#name#email#phone
	 * 
	 * @return
	 */
	private String generateInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append(curAgeGroup);
		sb.append("#").append(CommonUtils.nullToNA(gender));
		sb.append("#").append(CommonUtils.nullToNA(name.getText().toString()));
		sb.append("#").append(CommonUtils.nullToNA(email.getText().toString()));
		sb.append("#").append(CommonUtils.nullToNA(phone.getText().toString()));
		return sb.toString();
	}

	@Override
	protected void onRightBtn() {
		super.onRightBtn();
		IntentUtils.create(context)
				.putExtra(Constants.Extra.USER_INFO, generateInfo())
				.setResultOk().finish();
	}

	@Override
	protected void initActionBar(TitleBar titleBar) {
		super.initActionBar(titleBar);
		titleBar.showRightBtn();
		ImageButton right = titleBar.getRightBtn();
		right.setImageResource(R.drawable.common_btn_ok);
	}

	@Override
	protected void onleftLayout() {
		finish();
	}

	private class AgeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return ages.length;
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
			Holder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_age, null);
				holder = new Holder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			holder.title.setText(ages[position]);
			if (position == curAgeGroup) {
				ViewUtils.setVisibility(holder.selected, View.VISIBLE);
			} else
				ViewUtils.setVisibility(holder.selected, View.INVISIBLE);
			return convertView;
		}

		class Holder {
			ImageView selected;
			TextView title;

			public Holder(View v) {
				selected = (ImageView) v.findViewById(R.id.selected);
				title = (TextView) v.findViewById(R.id.title);
			}
		}
	}
}
