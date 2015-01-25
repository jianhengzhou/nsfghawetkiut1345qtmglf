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
import com.gdestiny.github.utils.ImageLoaderUtils;
import com.gdestiny.github.utils.ViewUtils;

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
		menuPopup = new StatusPopUpWindow(getContext(), 350,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				R.style.titlebar_popupwindow_anim);
		menuPopup.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				menuPopup.resetSecondly();
				// menuBtn.setImageResource(R.drawable.selector_common_btn_more);
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

	public void setLeftLayout(String url, String text) {
		setTitleIcon(url);
		setTitleText(text);
	}

	public void setTitleIcon(String url) {
		ImageLoaderUtils.displayImage(url, titleBackIcon,
				R.drawable.default_avatar, R.drawable.ic_launcher, false);
	}

	public void setTitleText(String text) {
		titleBackText.setText(text);
	}

	public void setTitleText(int id) {
		titleBackText.setText(id);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.titlebar_menu_btn:
			// menuBtn.setImageResource(R.drawable.common_btn_more_pressed);
			showStatus();
			break;
		}
	}
}
