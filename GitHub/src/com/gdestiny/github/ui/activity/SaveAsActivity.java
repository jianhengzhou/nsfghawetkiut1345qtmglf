package com.gdestiny.github.ui.activity;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.egit.github.core.Blob;
import org.eclipse.egit.github.core.util.EncodingUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.abstracts.activity.BaseLoadFragmentActivity;
import com.gdestiny.github.abstracts.async.SimpleUpdateResultTask;
import com.gdestiny.github.adapter.SaveFileAdapter;
import com.gdestiny.github.ui.view.FocusedEditText;
import com.gdestiny.github.ui.view.TitleBar;
import com.gdestiny.github.utils.AndroidUtils;
import com.gdestiny.github.utils.CacheUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.ImageUtils;
import com.gdestiny.github.utils.ToastUtils;
import com.gdestiny.github.utils.ViewUtils;

public class SaveAsActivity extends BaseLoadFragmentActivity<Void, File[]> {

	private File currentFile;
	private ListView list;
	private TextView path;
	private TextView empty;
	private SaveFileAdapter adapter;

	private View editView;
	private FocusedEditText edit;

	private String name;
	private Blob blob;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.act_save_as, R.id.pull_refresh_layout);
	}

	@Override
	protected void initActionBar(TitleBar titleBar) {
		editView = LayoutInflater.from(context).inflate(R.layout.layout_search,
				null);
		titleBar.addView(editView);

		edit = (FocusedEditText) editView.findViewById(R.id.search);
		edit.setHint("Name");
		edit.setNeedClear(false);
		ImageButton ok = (ImageButton) editView
				.findViewById(R.id.search_rignt_btn);
		ok.setImageResource(R.drawable.common_btn_ok);
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				File file = new File(currentFile, edit.getText().toString());
				if (file.exists()) {
					ToastUtils.show(context, "Directory exists");
				} else {
					if (file.mkdirs()) {
						AndroidUtils.Keyboard.hideKeyboard(context);
						currentFile = file;
						execute();
					} else {
						ToastUtils.show(context, "Making error");
					}
					ViewUtils.setVisibility(editView, View.GONE,
							R.anim.slide_out_to_top);
				}
			}
		});

		ViewUtils.setVisibility(editView, View.GONE);

		super.initActionBar(titleBar);
		titleBar.showRightBtn();
		titleBar.getRightBtn().setImageResource(R.drawable.common_btn_ok);

	}

	@Override
	protected void initView() {
		path = (TextView) findViewById(R.id.path);
		list = (ListView) findViewById(R.id.list);
		empty = (TextView) findViewById(R.id.empty);
		adapter = new SaveFileAdapter(context);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				currentFile = adapter.getItem(position);
				execute();
			}
		});

		findViewById(R.id.new_folder).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						edit.requestFocus();
						AndroidUtils.Keyboard.showKeyboard(context, edit);
						ViewUtils.setVisibility(editView, View.VISIBLE,
								R.anim.slide_in_from_top);
					}
				});
	}

	private File[] showSubDirectory(File root) {
		File[] files = root.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		Arrays.sort(files, new Comparator<File>() {

			@Override
			public int compare(File lhs, File rhs) {
				return lhs.getName().compareToIgnoreCase(rhs.getName());
			}
		});
		return files;
	}

	@Override
	protected void initData() {
		name = getIntent().getExtras().getString(Constants.Extra.NAME);

		titlebar.setLeftLayout(null, "Save As", name);

		blob = (Blob) getIntent().getSerializableExtra(Constants.Extra.BLOB);
		if (AndroidUtils.FileManager.isExternalStorageValid()) {
			currentFile = new File(CacheUtils.DATA_PATH);
			path.setText(currentFile.getPath() + File.separator + name);
			adapter.setDatas(showSubDirectory(currentFile));
		}
	}

	@Override
	public File[] onBackground(Void params) throws Exception {
		return showSubDirectory(currentFile);
	}

	@Override
	public void onSuccess(File[] result) {
		super.onSuccess(result);
		if (result == null || result.length == 0) {
			ViewUtils.setVisibility(empty, View.VISIBLE);
		} else {
			ViewUtils.setVisibility(empty, View.GONE);
		}
		path.setText(currentFile.getPath() + File.separator + name);
		adapter.setDatas(result);
	}

	@Override
	protected void onRightBtn() {
		super.onRightBtn();
		new SimpleUpdateResultTask<Boolean>(
				new SimpleUpdateResultTask.UpdateListener<Boolean>() {

					@Override
					public void onPrev() {
						// TODO Auto-generated method stub
						showProgress();
					}

					@Override
					public Boolean onExcute() {
						try {
							return save();
						} catch (Exception e) {
							e.printStackTrace();
							return false;
						}
					}

					@Override
					public void onSuccess(Boolean result) {
						// TODO Auto-generated method stub
						dismissProgress();
						if (result) {
							ToastUtils.show(context,
									"Save In " + currentFile.getPath()
											+ File.separator + name);
							finish();
						} else
							ToastUtils.show(context, "Save Error ");
					}
				}).execute();
	}

	private boolean save() throws Exception {
		byte[] data = EncodingUtils.fromBase64(blob.getContent());
		File file = new File(currentFile, name);
		if (ImageUtils.isImage(name)) {
			if (ImageUtils.isGifFromName(name)) {
				return false;
			}
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			if (ImageUtils.isPng(name))
				return bitmap.compress(Bitmap.CompressFormat.PNG, 100,
						new FileOutputStream(file));
			else
				return bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
						new FileOutputStream(file));
		} else {
			return AndroidUtils.FileManager.save(file,
					new String(data, "utf-8"));
		}
	}

	@Override
	public void onBackPressed() {
		if (editView.getVisibility() == View.VISIBLE) {
			ViewUtils.setVisibility(editView, View.GONE,
					R.anim.slide_out_to_top);
			return;
		}
		if (!currentFile.getPath().equals(CacheUtils.SD_PATH)) {
			currentFile = currentFile.getParentFile();
			execute();
			return;
		}
		super.onBackPressed();
	}

	@Override
	public void onRefresh() {
		execute();
	}

	@Override
	protected void onleftLayout() {
		finish();
	}

}
