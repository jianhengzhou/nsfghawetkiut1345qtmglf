/*
 * Copyright 2012 GitHub Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gdestiny.github.utils;

import android.annotation.SuppressLint;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Utilities for displaying source code in a {@link WebView}
 */
public class SourceEditor {

	public static final String SOURCE_EDITOR = "file:///android_asset/source-editor.html";

	private final WebView view;

	private boolean wrap;

	private String name;

	private String content;

	private boolean markdown;

	/**
	 * Create source editor using given web view
	 * 
	 * @param view
	 */
	@SuppressLint("SetJavaScriptEnabled")
	public SourceEditor(final WebView view) {
		WebViewClient client = new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (SOURCE_EDITOR.equals(url)) {
					view.loadUrl(url);
					return false;
				} else {
					ToastUtils.show(view.getContext(), url);
					return true;
				}
			}
		};
		view.setWebViewClient(client);

		WebSettings settings = view.getSettings();
		settings.setJavaScriptEnabled(true);
		view.addJavascriptInterface(this, "SourceEditor");
		view.setHorizontalScrollBarEnabled(false);

		this.view = view;
	}

	/**
	 * @return name
	 */
	@JavascriptInterface
	public String getName() {
		return name;
	}

	/**
	 * @return content
	 */
	@JavascriptInterface
	public String getRawContent() {
		return content;
	}

	/**
	 * @return content
	 */
	@JavascriptInterface
	public String getContent() {
		return getRawContent();
	}

	/**
	 * @return wrap
	 */
	@JavascriptInterface
	public boolean getWrap() {
		return wrap;
	}

	/**
	 * @return markdown
	 */
	public boolean isMarkdown() {
		return markdown;
	}

	/**
	 * Set whether lines should wrap
	 * 
	 * @param wrap
	 * @return this editor
	 */
	public SourceEditor setWrap(final boolean wrap) {
		this.wrap = wrap;
		loadSource();
		return this;
	}

	/**
	 * Sets whether the content is a markdown file
	 * 
	 * @param markdown
	 * @return this editor
	 */
	public SourceEditor setMarkdown(final boolean markdown) {
		this.markdown = markdown;
		return this;
	}

	/**
	 * Bind content to current {@link WebView}
	 * 
	 * @param name
	 * @param content
	 * @param encoded
	 * @return this editor
	 */
	public SourceEditor setSource(final String name, final String content) {
		this.name = name;
		this.content = content;
		loadSource();

		return this;
	}

	public SourceEditor setContent(final String content) {
		this.content = content;
		loadSource();

		return this;
	}

	private void loadSource() {
		if (name != null && content != null)
			if (markdown) {
				GLog.sysout("isMarkdown");
				view.loadDataWithBaseURL(null, content, "text/html", "utf-8",
						null);
			} else {
				GLog.sysout("not markdown");
				view.loadUrl(SOURCE_EDITOR);
			}
	}

	/**
	 * Toggle line wrap
	 * 
	 * @return this editor
	 */
	public SourceEditor toggleWrap() {
		return setWrap(!wrap);
	}

	/**
	 * Toggle markdown file rendering
	 * 
	 * @return this editor
	 */
	public SourceEditor toggleMarkdown() {
		return setMarkdown(!markdown);
	}
}
