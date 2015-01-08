package com.gdestiny.github.ui.activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.gdestiny.github.R;
import com.gdestiny.github.ui.view.TitleBar;

public class BaseFragmentActivity extends SherlockFragmentActivity implements
		SwipeBackActivityBase {
	private SwipeBackActivityHelper mHelper;
	private TitleBar titlebar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHelper = new SwipeBackActivityHelper(this);
		mHelper.onActivityCreate();
		initActionBar();
	}

	protected void initActionBar() {
		//initMiBar();
		titlebar = new TitleBar(this);

		ActionBar actionbar = getSupportActionBar();
		actionbar.setCustomView(titlebar);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayUseLogoEnabled(false);
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private void initMiBar() {
		Window window = getWindow();
		window.setBackgroundDrawableResource(R.color.common_blue_bk);

		Class clazz = window.getClass();
		try {
			int tranceFlag = 0;
			int darkModeFlag = 0;
			Class layoutParams = Class
					.forName("android.view.MiuiWindowManager$LayoutParams");

			Field field = layoutParams
					.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
			tranceFlag = field.getInt(layoutParams);

			field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
			darkModeFlag = field.getInt(layoutParams);

			@SuppressWarnings("unchecked")
			Method extraFlagField = clazz.getMethod("setExtraFlags", int.class,
					int.class);
			// 只需要状态栏透明
			//extraFlagField.invoke(window, tranceFlag, tranceFlag);
			// 状态栏透明且黑色字体
			extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag
					| darkModeFlag);
			// 清除黑色字体
			//extraFlagField.invoke(window, 0, darkModeFlag);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mHelper.onPostCreate();
	}

	@Override
	public View findViewById(int id) {
		View v = super.findViewById(id);
		if (v == null && mHelper != null)
			return mHelper.findViewById(id);
		return v;
	}

	@Override
	public SwipeBackLayout getSwipeBackLayout() {
		return mHelper.getSwipeBackLayout();
	}

	@Override
	public void setSwipeBackEnable(boolean enable) {
		getSwipeBackLayout().setEnableGesture(enable);
	}

	@Override
	public void scrollToFinishActivity() {
		Utils.convertActivityToTranslucent(this);
		getSwipeBackLayout().scrollToFinishActivity();
	}

}
