package com.gdestiny.github.ui.dialog;

import java.io.File;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.gdestiny.github.R;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateConfig;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class MaterialUpdateDialog {

	private Context context;

	private MaterialDialog dialog;
	private UpdateResponse updateInfo;
	private UmengUpdateListener updateListener;

	public interface UpdateListener {
		public void onReturned();

		public void onNew(String version);
	}

	public UmengUpdateListener getUpdateListener() {
		return updateListener;
	}

	public void setOnUpdateListener(UmengUpdateListener updateListener) {
		this.updateListener = updateListener;
	}

	public MaterialUpdateDialog(Context cxt) {
		this.context = cxt;

		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus,
					final UpdateResponse updateInfo) {
				if (updateListener != null)
					updateListener.onUpdateReturned(updateStatus, updateInfo);
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					MaterialUpdateDialog.this.setInfoDetail(updateInfo).show();

					// System.out.println("delta:" + updateInfo.delta);
					// System.out.println("display_ads:" +
					// updateInfo.display_ads);
					// System.out.println("hasUpdate:" + updateInfo.hasUpdate);
					// System.out.println("new_md5:" + updateInfo.new_md5);
					// System.out.println("origin:" + updateInfo.origin);
					// System.out.println("patch_md5:" + updateInfo.patch_md5);
					// System.out.println("path:" + updateInfo.path);
					// System.out.println("proto_ver:" + updateInfo.proto_ver);
					// System.out.println("size:" + updateInfo.size);
					// System.out.println("target_size:" +
					// updateInfo.target_size);
					// System.out.println("updateLog:" + updateInfo.updateLog);
					// System.out.println("version:" + updateInfo.version);
					break;
				case UpdateStatus.No: // has no update
					Toast.makeText(context, "No Update", Toast.LENGTH_SHORT)
							.show();
					break;
				case UpdateStatus.NoneWifi: // none wifi
					Toast.makeText(context, R.string.UMGprsCondition,
							Toast.LENGTH_SHORT).show();
					break;
				case UpdateStatus.Timeout: // time out
					Toast.makeText(context, R.string.UMBreak_Network,
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
	}

	public MaterialUpdateDialog setInfoDetail(UpdateResponse updateInfo) {
		this.updateInfo = updateInfo;
		return this;
	}

	public void show() {
		if (updateInfo == null) {
			throw new IllegalArgumentException("the updateInfo is null");
		}
		if (UmengUpdateAgent.isIgnore(context, updateInfo)) {
			return;
		}

		final File file = UmengUpdateAgent.downloadedFile(context, updateInfo);
		final boolean apkExist = file != null && file.exists();

		dialog = new MaterialDialog(context);
		View view = LayoutInflater.from(context).inflate(
				R.layout.layout_update_dialog, null);
		dialog.setContentView(view);
		TextView message = (TextView) view.findViewById(R.id.message);
		final CheckBox ignore = (CheckBox) view.findViewById(R.id.ignore);
		if (UpdateConfig.isUpdateForce())
			ignore.setVisibility(View.GONE);

		dialog.setTitle("Update To " + updateInfo.version)
				.setNegativeButton("update", new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						if (apkExist) {
							UmengUpdateAgent.startInstall(context, file);
						} else {
							Toast.makeText(context, "start download...",
									Toast.LENGTH_SHORT).show();
							UmengUpdateAgent.startDownload(context, updateInfo);
						}
						dialog.dismiss();
					}
				}).setPositiveButton("not now", new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (ignore.isChecked()) {
							Toast.makeText(context, "ignore this",
									Toast.LENGTH_SHORT).show();
							UmengUpdateAgent.ignoreUpdate(context, updateInfo);
						}
						dialog.dismiss();
					}
				}).setCanceledOnTouchOutside(true);
		if (apkExist) {
			message.setText(context.getResources().getString(
					R.string.UMDialog_InstallAPK)
					+ "\n"
					+ updateInfo.updateLog
					+ updateInfo.updateLog
					+ updateInfo.updateLog
					+ updateInfo.updateLog
					+ updateInfo.updateLog + updateInfo.updateLog);
		} else {
			message.setText(updateInfo.updateLog);
		}
		dialog.show();
	}

	public void update() {
		UmengUpdateAgent.update(context);
	}

	public static void setDefault() {
		UmengUpdateAgent.setDefault();
		UmengUpdateAgent.setUpdateAutoPopup(false);
	}
}
