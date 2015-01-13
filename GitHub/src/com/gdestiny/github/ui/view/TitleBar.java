package com.gdestiny.github.ui.view;

import java.util.LinkedHashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow.StatusPopUpWindowItemClickListener;

public class TitleBar extends LinearLayout implements View.OnClickListener {

	private StatusPopUpWindow menuPopup;
	private ImageButton rightBtn;
	private ImageButton menuBtn;
	private ImageView titleBackIcon;
	private TextView titleBackText;

	public TitleBar(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.layout_titlebar, this);
		setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		initView();
		initStatusPopup();
	}

	private void initView() {
		rightBtn = (ImageButton) findViewById(R.id.titlebar_rignt_btn);
		menuBtn = (ImageButton) findViewById(R.id.titlebar_menu_btn);
		titleBackIcon = (ImageView) findViewById(R.id.titlebar_back_icon);
		titleBackText = (TextView) findViewById(R.id.titlebar_back_text);
		menuBtn.setOnClickListener(this);
	}

	private void initStatusPopup() {
		if (menuPopup != null)
			return;
		menuPopup = new StatusPopUpWindow(getContext(), 400,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				R.style.titlebar_popupwindow_anim);
		menuPopup.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				menuBtn.setImageResource(R.drawable.selector_common_btn_more);
			}
		});
		// test
		LinkedHashMap<Integer, Integer> itemmap = new LinkedHashMap<Integer, Integer>();
		itemmap.put(R.string.app_name, R.drawable.common_gists_pressed);
		itemmap.put(R.string.name, R.drawable.common_bookmarks_pressed);
		itemmap.put(R.string.news, R.drawable.common_follower_pressed);
		itemmap.put(R.string.followers, R.drawable.common_issue_pressed);
		itemmap.put(R.string.following, R.drawable.common_issue_pressed);
		setStatusItem(getContext(), itemmap,
				new StatusPopUpWindow.StatusPopUpWindowItemClickListener() {

					@Override
					public void onitemclick(int drawableid) {
						// TODO Auto-generated method stub

					}
				});
	}

	public void setStatusItem(Context context,
			LinkedHashMap<Integer, Integer> itemmap,
			StatusPopUpWindowItemClickListener mOnitemclicklistener) {
		if (menuPopup != null) {
			menuPopup.setItem(context, itemmap, mOnitemclicklistener);
		}
	}

	public void showStatus() {
		if (menuPopup != null)
			menuPopup.showAsDropDown(menuBtn);
	}

	public void hideRight() {
		rightBtn.setVisibility(View.GONE);
		menuBtn.setVisibility(View.GONE);
	}

	public void showStatusBtn() {
		rightBtn.setVisibility(View.GONE);
		menuBtn.setVisibility(View.VISIBLE);
	}

	public void showRightBtn() {
		rightBtn.setVisibility(View.VISIBLE);
		menuBtn.setVisibility(View.GONE);
	}

	public void setBackLayout(String bitmapUrl, String text) {
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.titlebar_menu_btn:
			menuBtn.setImageResource(R.drawable.common_btn_more_pressed);
			showStatus();
			break;
		}
	}
}
