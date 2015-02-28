package com.gdestiny.github.utils;

import com.gdestiny.github.ui.activity.WebViewActivity;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class ViewUtils {

	private ViewUtils() {
		throw new AssertionError();
	}

	public static <V extends View> void setVisibility(V view, int visibility) {
		if (view != null && view.getVisibility() != visibility) {
			view.setVisibility(visibility);
		}
	}

	public static <V extends View> void setVisibility(V view, int visibility,
			int animId) {
		if (view != null && view.getVisibility() != visibility) {
			view.setAnimation(AnimationUtils.loadAnimation(view.getContext(),
					animId));
			view.setVisibility(visibility);
		}
	}

	public static void handleLink(TextView textview) {
		textview.setMovementMethod(LinkMovementMethod.getInstance());

		CharSequence text = textview.getText();
		if (text instanceof Spannable) {
			int end = text.length();
			Spannable sp = (Spannable) textview.getText();
			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
			ImageSpan[] images = sp.getSpans(0, end, ImageSpan.class);
			SpannableStringBuilder style = new SpannableStringBuilder(text);
			style.clearSpans();// should clear old spans
			for (URLSpan url : urls) {
				ClickSpan myURLSpan = new ClickSpan(url.getURL());
				style.setSpan(myURLSpan, sp.getSpanStart(url),
						sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			for (ImageSpan image : images) {
				style.setSpan(image, sp.getSpanStart(image),
						sp.getSpanEnd(image),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			textview.setText(style);
		}
	}

	private static class ClickSpan extends ClickableSpan {

		private String url;

		public ClickSpan(String url) {
			this.url = url;
		}

		@Override
		public void onClick(View widget) {
			System.out.println(url);
			if (url.contains("mailto:")) {
				String source = url.substring(url.indexOf("mailto:") + 7);
				System.out.println(source);
				if (AndroidUtils.isEmail(source)) {
					AndroidUtils.openEmail(widget.getContext(), url);
				}
			} else if (AndroidUtils.isEmail(url)) {
				AndroidUtils.openEmail(widget.getContext(), url);
			} else if (AndroidUtils.isHttpUrl(url)) {
				IntentUtils.create(widget.getContext(), WebViewActivity.class)
						.putExtra(Constants.Extra.URL, url).start();
			}

		}

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(0xff56abe4);
			ds.setUnderlineText(true);
		}

	}
}
