package com.gdestiny.github.ui.view;

import com.gdestiny.github.utils.GLog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class FocusedEditText extends EditText {

	private boolean needClear = true;

	public FocusedEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean isNeedClear() {
		return needClear;
	}

	public void setNeedClear(boolean needClear) {
		this.needClear = needClear;
	}

	@Override
	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		InputMethodManager imm = (InputMethodManager) getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (needClear && imm.isActive(this)
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			clearFocus();
			GLog.sysout("clearFocus");
			imm.hideSoftInputFromWindow(getWindowToken(), 0);
			return true;
		}
		return super.dispatchKeyEventPreIme(event);
	}

}
