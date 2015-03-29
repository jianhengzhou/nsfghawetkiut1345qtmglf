package com.gdestiny.github.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.gdestiny.github.R;
import com.gdestiny.github.adapter.StartUpAdapter;
import com.gdestiny.github.utils.PreferencesUtils;

public abstract class StartUpDialog {

	private MaterialDialog dialog;
	private Context context;

	public StartUpDialog(Context context) {
		this.context = context;
		dialog = new MaterialDialog(context);
		dialog.setTitle(R.string.start_up).setCanceledOnTouchOutside(true)
				.initListView();

		final StartUpAdapter adapter = new StartUpAdapter(context);
		adapter.setCurStartup(PreferencesUtils.getInt(context, "startup", 0));
		dialog.getmListView().setAdapter(adapter);
		dialog.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PreferencesUtils.putInt(StartUpDialog.this.context, "startup",
						position);
				adapter.setCurStartup(position);
				onSelected(adapter.getItem(position));
			}
		});
	}

	public abstract void onSelected(String name);

	public void show() {
		dialog.show();
	}
}
