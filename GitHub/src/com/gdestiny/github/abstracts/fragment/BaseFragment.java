package com.gdestiny.github.abstracts.fragment;

import java.util.LinkedHashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gdestiny.github.abstracts.activity.BaseFragmentActivity;
import com.gdestiny.github.ui.dialog.StatusPopUpWindow;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.GLog;
import com.umeng.analytics.MobclickAgent;

/**
 * @author Lifeix
 * 
 */
public abstract class BaseFragment extends Fragment {

	protected final String mClassName;
	protected Activity context;
	protected StringBuilder mBuffer = new StringBuilder();
	private View currentView;

	protected LinkedHashMap<Integer, Integer> itemmap;
	protected LinkedHashMap<Integer, Integer> itemmapSecondly;
	protected StatusPopUpWindow.StatusPopUpWindowItemClickListener menuListener;

	abstract protected void setCurrentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);

	abstract protected void initView();

	abstract protected void initData();

	public abstract void initStatusPopup(final TitleBar title);

	public void refreshPopup() {
		if (itemmap != null && menuListener != null)
			((BaseFragmentActivity) context).getTitlebar().setStatusItem(
					context, itemmap, menuListener);
	}

	public BaseFragment() {
		mClassName = getClass().getSimpleName();
	}

	public void onShowRepeat(Activity activity) {
	}

	public void onShowInParentActivity(Activity activity) {
	}

	public View findViewById(int id) {
		if (this.currentView == null) {
			GLog.sysout("this.currentView == null");
			return null;
		}
		return currentView.findViewById(id);
	}

	/**
	 * called once the fragment is associated with its activity.
	 * 
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onAttach()").toString());
		context = activity;
	}

	/**
	 * called to do initial creation of the fragment.
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onCreate()").toString());
	}

	/**
	 * creates and returns the view hierarchy associated with the fragment.
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onCreateView()").toString());
		setCurrentView(inflater, container, savedInstanceState);
		initView();
		initStatusPopup(((BaseFragmentActivity) context).getTitlebar());
		initData();
		return this.currentView;
	}

	public void setContentView(LayoutInflater inflater, int id) {
		this.currentView = inflater.inflate(id, null);
	}

	public View getCurrentView() {
		return currentView;
	}

	/**
	 * tells the fragment that its activity has completed its own
	 * Activity.onCreaate.
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onActivityCreated()").toString());
	}

	/**
	 * makes the fragment visible to the user (based on its containing activity
	 * being started).
	 * 
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onStart()").toString());
	}

	/**
	 * makes the fragment interacting with the user (based on its containing
	 * activity being resumed). As a fragment is no longer being used, it goes
	 * through a reverse series of callbacks:
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onResume()").toString());
		MobclickAgent.onPageStart(mClassName);
	}

	/**
	 * @see android.support.v4.app.Fragment#onHiddenChanged(boolean)
	 */
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onHiddenChanged(").append(hidden).append(")")
						.toString());
		if (!hidden) {
			initStatusPopup(((BaseFragmentActivity) context).getTitlebar());
			GLog.d(Constants.GlobalTag, "initStatusPopup");
		}
	}

	/**
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onSaveInstanceState()").toString());
	}

	/**
	 * @see android.support.v4.app.Fragment#setRetainInstance(boolean)
	 */
	@Override
	public void setRetainInstance(boolean retain) {
		super.setRetainInstance(retain);
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".setRetainInstance(").append(retain)
						.append(")").toString());
	}

	/**
	 * fragment is no longer interacting with the user either because its
	 * activity is being paused or a fragment operation is modifying it in the
	 * activity.
	 * 
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onPause()").toString());
		super.onPause();
		MobclickAgent.onPageEnd(mClassName);
	}

	/**
	 * fragment is no longer visible to the user either because its activity is
	 * being stopped or a fragment operation is modifying it in the activity.
	 * 
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onStop()").toString());
		super.onStop();
	}

	/**
	 * allows the fragment to clean up resources associated with its View.
	 * 
	 * @see android.support.v4.app.Fragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onDestroyView()").toString());
		super.onDestroyView();
	}

	/**
	 * called to do final cleanup of the fragment's state.
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onDestroy()").toString());
		super.onDestroy();
	}

	/**
	 * called immediately prior to the fragment no longer being associated with
	 * its activity.
	 * 
	 * @see android.support.v4.app.Fragment#onDetach()
	 */
	@Override
	public void onDetach() {
		GLog.d(Constants.GlobalTag,
				mBuffer.delete(0, mBuffer.length()).append(mClassName)
						.append(".onDetach()").toString());
		super.onDetach();
	}

	@Override
	public String toString() {
		return getClass().getName();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			onResultOk(requestCode, data);
		} else {
			onResultCancle(requestCode, data);
		}
	}

	public void onResultOk(int requestCode, Intent data) {

	}

	public void onResultCancle(int requestCode, Intent data) {

	}
}
