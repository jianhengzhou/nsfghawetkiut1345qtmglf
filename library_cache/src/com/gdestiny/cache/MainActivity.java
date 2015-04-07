package com.gdestiny.cache;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.gdestiny.cache.disk.impl.BaseDiskCache;
import com.gdestiny.cache.utils.DirectNameGenerator;

public class MainActivity extends Activity {

	BaseDiskCache cache;

	EditText name;
	EditText contentEd;
	EditText sub;
	TextView content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		name = (EditText) findViewById(R.id.name);
		contentEd = (EditText) findViewById(R.id.wite_content);
		sub = (EditText) findViewById(R.id.sub);
		content = (TextView) findViewById(R.id.content);

		cache = new BaseDiskCache(android.os.Environment
				.getExternalStorageDirectory().getPath() + "/Cache_TEST",
				new DirectNameGenerator());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			try {
				if (TextUtils.isEmpty(sub.getText()))
					cache.save(name.getText().toString(), contentEd.getText()
							.toString());
				else
					cache.save(sub.getText().toString(), name.getText()
							.toString(), contentEd.getText().toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (id == R.id.read_content) {
			if (TextUtils.isEmpty(sub.getText()))
				content.setText(cache.getContent(name.getText().toString()));
			else
				content.setText(cache.getContent(sub.getText().toString(), name
						.getText().toString()));
		}
		return true;
	}
}
