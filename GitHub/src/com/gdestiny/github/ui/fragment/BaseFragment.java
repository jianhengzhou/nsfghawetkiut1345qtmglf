package com.gdestiny.github.ui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment implements
		DialogInterface.OnCancelListener {

	protected View currentView;
	private ProgressDialog progressDialog;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	protected void initView() {
		if (progressDialog == null)
			progressDialog = new ProgressDialog(getActivity());
		progressDialog.setCancelable(true);
		progressDialog.setOnCancelListener(this);
	}

	protected void initData() {

	}

	public void showProgress() {
		if (progressDialog != null && !progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	public void dismissProgress() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {

	}
}
