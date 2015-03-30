package com.gdestiny.github.ui.view;

import java.util.LinkedHashMap;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow.StatusPopUpWindowItemClickListener;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.ViewUtils;

public class TitleBar extends FrameLayout implements View.OnClickListener {

	public static final int ICON = 0;
	public static final int MAIN = 1;
	public static final int SECONDLY = 2;

	private StatusPopUpWindow menuPopup;
	private ImageButton rightBtn;
	private ImageButton menuBtn;
	private ImageView titleBackIcon;
	private TextView titleBackText;
	private TextView titleBackTextSecondly;

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
		titleBackTextSecondly = (TextView) findViewById(R.id.titlebar_back_text_secondly);
		menuBtn.setOnClickListener(this);
	}

	private void initStatusPopup() {
		if (menuPopup != null)
			return;
		menuPopup = new StatusPopUpWindow(getContext(), AndroidUtils.dpToPxInt(
				getContext(), 180), ViewGroup.LayoutParams.WRAP_CONTENT,
				R.style.titlebar_popupwindow_anim);
		menuPopup.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				menuPopup.resetSecondly();
				menuBtn.setBackgroundResource(R.drawable.selector_on_click);
			}
		});
	}

	public void setStatusItem(Context context,
			LinkedHashMap<Integer, Integer> itemmap,
			StatusPopUpWindowItemClickListener mOnitemclicklistener) {
		showStatusBtn();
		if (menuPopup != null) {
			menuPopup.setItem(context, itemmap, mOnitemclicklistener);
		}
	}

	public void initSecondly() {
		if (menuPopup != null) {
			menuPopup.initSecondlyStatus();
		}
	}

	public void setSecondlyStatusItem(Context context,
			LinkedHashMap<Integer, Integer> itemmap) {
		if (menuPopup != null) {
			menuPopup.setSecondlyItem(context, itemmap);
		}
	}

	public void showStatus() {
		if (menuPopup != null)
			menuPopup.showAsDropDown(menuBtn);
	}

	public void showSecondly() {
		if (menuPopup != null) {
			menuPopup.showSecondlyStatus();
		}
	}

	public void dissmissStatus() {
		if (menuPopup != null && menuPopup.isShowing())
			menuPopup.dismiss();
	}

	public void hideRight() {
		ViewUtils.setVisibility(rightBtn, View.GONE);
		ViewUtils.setVisibility(menuBtn, View.GONE);
	}

	public void hideLeft() {
		ViewUtils.setVisibility(titleBackIcon, View.GONE);
		ViewUtils.setVisibility(titleBackText, View.GONE);
		ViewUtils.setVisibility(titleBackTextSecondly, View.GONE);
	}

	public void showStatusBtn() {
		ViewUtils.setVisibility(rightBtn, View.GONE);
		ViewUtils.setVisibility(menuBtn, View.VISIBLE);
	}

	public void showRightBtn() {
		ViewUtils.setVisibility(rightBtn, View.VISIBLE);
		ViewUtils.setVisibility(menuBtn, View.GONE);
	}

	public StatusPopUpWindow getStatusPopup() {
		return menuPopup;
	}

	public ImageButton getRightBtn() {
		return rightBtn;
	}

	public ImageButton getMenuBtn() {
		return menuBtn;
	}

	public ImageView getTitleBackIcon() {
		return titleBackIcon;
	}

	public TextView getTitleBackText() {
		return titleBackText;
	}

	public TitleBar setLeftLayout(String url, String mainText) {
		setTitleIcon(url);
		setTitleText(mainText);
		return this;
	}

	public TitleBar setLeftLayout(String url, String mainText,
			String textSecondly) {
		setTitleIcon(url);
		setTitleText(mainText);
		setTitleTextSecondly(textSecondly);
		return this;
	}

	public TitleBar setTitleIcon(String url) {
		if (TextUtils.isEmpty(url)) {
			ViewUtils.setVisibility(titleBackIcon, View.GONE);
		} else {
			ViewUtils.setVisibility(titleBackIcon, View.VISIBLE);
			ImageLoaderUtils.displayImage(url, titleBackIcon,
					R.drawable.default_avatar, R.drawable.ic_launcher, false);
		}
		return this;
	}

	public TitleBar setTitleIcon(int id) {
		if (id <= 0) {
			ViewUtils.setVisibility(titleBackIcon, View.GONE);
		} else {
			ViewUtils.setVisibility(titleBackIcon, View.VISIBLE);
			titleBackIcon.setImageResource(id);
		}
		return this;
	}

	public TitleBar setTitleText(String text) {
		if (TextUtils.isEmpty(text)) {
			ViewUtils.setVisibility(titleBackText, View.GONE);
		} else {
			ViewUtils.setVisibility(titleBackText, View.VISIBLE);
			titleBackText.setText(text);
		}
		return this;
	}

	public TitleBar setTitleText(int id) {
		if (id <= 0) {
			ViewUtils.setVisibility(titleBackText, View.GONE);
		} else {
			ViewUtils.setVisibility(titleBackText, View.VISIBLE);
			titleBackText.setText(id);
		}
		return this;
	}

	public TitleBar setTitleTextSecondly(String text) {
		if (TextUtils.isEmpty(text)) {
			ViewUtils.setVisibility(titleBackTextSecondly, View.GONE);
		} else {
			ViewUtils.setVisibility(titleBackTextSecondly, View.VISIBLE);
			titleBackTextSecondly.setText(text);
		}
		return this;
	}

	public TitleBar setTitleTextSecondly(int id) {
		if (id <= 0) {
			ViewUtils.setVisibility(titleBackTextSecondly, View.GONE);
		} else {
			ViewUtils.setVisibility(titleBackTextSecondly, View.VISIBLE);
			titleBackTextSecondly.setText(id);
		}
		return this;
	}

	public TitleBar setLeftVisibility(int which, int visibility) {
		switch (which) {
		case ICON:
			ViewUtils.setVisibility(titleBackIcon, visibility);
			break;
		case MAIN:
			ViewUtils.setVisibility(titleBackText, visibility);
			break;
		case SECONDLY:
			ViewUtils.setVisibility(titleBackTextSecondly, visibility);
			break;
		}
		return this;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.titlebar_menu_btn:
			// menuBtn.setImageResource(R.drawable.common_btn_more_pressed);
			menuBtn.setBackgroundResource(R.color.common_title_back_layout);
			showStatus();
			break;
		}
	}
}
