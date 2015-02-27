package com.gdestiny.github.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gdestiny.github.R;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.ImageLoaderUtils;

/**
 * Created by drakeet on 9/28/14.
 */
public class MaterialDialog {

	private final static int BUTTON_BOTTOM = 10;
	private final static int BUTTON_TOP = 10;

	private boolean mCancel;
	private Context mContext;
	private AlertDialog mAlertDialog;
	private MaterialDialog.Builder mBuilder;
	private View mView;
	private int mTitleResId;
	private CharSequence mTitle;
	private int mMessageResId;
	private CharSequence mMessage;
	private TextView mPositiveButton;
	private LinearLayout.LayoutParams mLayoutParams;
	private TextView mNegativeButton;
	private boolean mHasShow = false;
	private Drawable mBackgroundDrawable;
	private int mBackgroundResId;
	private View mMessageContentView;
	private DialogInterface.OnDismissListener mOnDismissListener;
	private AdapterView.OnItemClickListener onItemClickListener;
	private ListView mListView;

	public MaterialDialog(Context context) {
		this.mContext = context;
	}

	public View getContentView() {
		return mView;
	}

	public void show() {
		if (mHasShow == false)
			mBuilder = new Builder();
		else
			mAlertDialog.show();
		mHasShow = true;
	}

	public MaterialDialog setView(View view) {
		mView = view;
		if (mBuilder != null) {
			mBuilder.setView(view);
		}
		return this;
	}

	public MaterialDialog setContentView(View view) {
		mMessageContentView = view;
		if (mBuilder != null) {
			mBuilder.setContentView(mMessageContentView);
		}
		return this;
	}

	public MaterialDialog setBackground(Drawable drawable) {
		mBackgroundDrawable = drawable;
		if (mBuilder != null) {
			mBuilder.setBackground(mBackgroundDrawable);
		}
		return this;
	}

	public MaterialDialog setBackgroundResource(int resId) {
		mBackgroundResId = resId;
		if (mBuilder != null) {
			mBuilder.setBackgroundResource(mBackgroundResId);
		}
		return this;
	}

	public void dismiss() {
		mAlertDialog.dismiss();
	}

	private int dip2px(float dpValue) {
		final float scale = mContext.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	private static boolean isLollipop() {
		return false;
		// return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
	}

	public MaterialDialog setTitle(int resId) {
		mTitleResId = resId;
		if (mBuilder != null) {
			mBuilder.setTitle(resId);
		}
		return this;
	}

	public MaterialDialog setTitle(CharSequence title) {
		mTitle = title;
		if (mBuilder != null) {
			mBuilder.setTitle(title);
		}
		return this;
	}

	public MaterialDialog setMessage(int resId) {
		mMessageResId = resId;
		if (mBuilder != null) {
			mBuilder.setMessage(resId);
		}
		return this;
	}

	public MaterialDialog setMessage(CharSequence message) {
		mMessage = message;
		if (mBuilder != null) {
			mBuilder.setMessage(message);
		}
		return this;
	}

	public MaterialDialog setPositiveButton(int resId,
			final View.OnClickListener listener) {
		mPositiveButton = new TextView(mContext);

		int padding = dip2px(8);
		mPositiveButton.setPadding(padding * 2, padding, padding * 2, padding);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		mPositiveButton.setLayoutParams(params);
		mPositiveButton.setBackgroundResource(R.drawable.material_button);
		mPositiveButton.setTextColor(Color.argb(255, 35, 159, 242));
		mPositiveButton.setText(resId);
		mPositiveButton.setGravity(Gravity.CENTER);
		mPositiveButton.setTextSize(14);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(dip2px(2), dip2px(BUTTON_TOP), dip2px(12),
				dip2px(BUTTON_BOTTOM));
		mPositiveButton.setLayoutParams(layoutParams);
		mPositiveButton.setOnClickListener(listener);
		if (isLollipop()) {
			mPositiveButton.setBackgroundResource(android.R.color.transparent);
		}
		return this;
	}

	public MaterialDialog setPositiveButton(String text,
			final View.OnClickListener listener) {
		mPositiveButton = new TextView(mContext);

		int padding = dip2px(8);
		mPositiveButton.setPadding(padding * 2, padding, padding * 2, padding);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		mPositiveButton.setLayoutParams(params);
		mPositiveButton.setBackgroundResource(R.drawable.material_button);
		// mPositiveButton
		// .setBackgroundResource(R.drawable.common_blue_drawable_bk);
		mPositiveButton.setTextColor(Color.argb(255, 35, 159, 242));
		mPositiveButton.setText(text);
		mPositiveButton.setGravity(Gravity.CENTER);
		mPositiveButton.setTextSize(14);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(dip2px(2), dip2px(BUTTON_TOP), dip2px(12),
				dip2px(BUTTON_BOTTOM));
		mPositiveButton.setLayoutParams(layoutParams);
		mPositiveButton.setOnClickListener(listener);
		if (isLollipop()) {
			mPositiveButton.setBackgroundResource(android.R.color.transparent);
		}
		return this;
	}

	public MaterialDialog setNegativeButton(int resId,
			final View.OnClickListener listener) {
		mNegativeButton = new TextView(mContext);

		int padding = dip2px(8);
		mNegativeButton.setPadding(padding * 2, padding, padding * 2, padding);

		mLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		mNegativeButton.setLayoutParams(mLayoutParams);
		mNegativeButton.setBackgroundResource(R.drawable.material_button);
		mNegativeButton.setText(resId);
		mNegativeButton.setTextColor(Color.argb(222, 0, 0, 0));
		mNegativeButton.setTextSize(14);
		mNegativeButton.setGravity(Gravity.CENTER);
		mNegativeButton.setOnClickListener(listener);
		if (isLollipop()) {
			mNegativeButton.setBackgroundResource(android.R.color.transparent);
		}

		return this;
	}

	public MaterialDialog setNegativeButton(String text,
			final View.OnClickListener listener) {
		mNegativeButton = new TextView(mContext);

		int padding = dip2px(8);
		mNegativeButton.setPadding(padding * 2, padding, padding * 2, padding);

		mLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		mNegativeButton.setLayoutParams(mLayoutParams);
		mNegativeButton.setBackgroundResource(R.drawable.material_button);
		mNegativeButton.setText(text);
		mNegativeButton.setTextColor(Color.argb(222, 0, 0, 0));
		mNegativeButton.setTextSize(14);
		mNegativeButton.setGravity(Gravity.CENTER);
		mNegativeButton.setOnClickListener(listener);
		if (isLollipop()) {
			mNegativeButton.setBackgroundResource(android.R.color.transparent);
		}

		return this;
	}

	public MaterialDialog setCanceledOnTouchOutside(boolean cancel) {
		this.mCancel = cancel;
		if (mBuilder != null) {
			mBuilder.setCanceledOnTouchOutside(mCancel);
		}
		return this;
	}

	public MaterialDialog setOnDismissListener(
			DialogInterface.OnDismissListener onDismissListener) {
		this.mOnDismissListener = onDismissListener;
		return this;
	}

	private class Builder {

		private TextView mTitleView;
		private TextView mMessageView;
		private Window mAlertDialogWindow;
		private LinearLayout mButtonLayout;

		private Builder() {
			mAlertDialog = new AlertDialog.Builder(mContext).create();
			mAlertDialog.show();

			mAlertDialog.getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
							| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
			mAlertDialog.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

			mAlertDialogWindow = mAlertDialog.getWindow();
			View contv = LayoutInflater.from(mContext).inflate(
					R.layout.layout_material_dialog, null);
			contv.setFocusable(true);
			contv.setFocusableInTouchMode(true);

			mAlertDialogWindow
					.setBackgroundDrawableResource(R.drawable.material_dialog_window);

			mAlertDialogWindow.setContentView(contv);
			// mAlertDialogWindow.setContentView(R.layout.layout_materialdialog);

			// 7

			mTitleView = (TextView) mAlertDialogWindow.findViewById(R.id.title);
			mMessageView = (TextView) mAlertDialogWindow
					.findViewById(R.id.message);
			mButtonLayout = (LinearLayout) mAlertDialogWindow
					.findViewById(R.id.buttonLayout);
			if (mView != null) {
				LinearLayout linearLayout = (LinearLayout) mAlertDialogWindow
						.findViewById(R.id.contentView);
				linearLayout.removeAllViews();
				linearLayout.addView(mView);
			}
			if (mTitleResId != 0) {
				setTitle(mTitleResId);
			}
			if (mTitle != null) {
				setTitle(mTitle);
			}
			if (mTitle == null && mTitleResId == 0) {
				mTitleView.setVisibility(View.GONE);
			}
			if (mMessageResId != 0) {
				setMessage(mMessageResId);
			}
			if (mMessage != null) {
				setMessage(mMessage);
			}
			if (mPositiveButton != null) {
				mButtonLayout.addView(mPositiveButton);
				// fix
				LinearLayout linearLayout = (LinearLayout) mAlertDialogWindow
						.findViewById(R.id.contentView);
				linearLayout.setPadding(0, 0, 0, dip2px(42));
			}
			if (mLayoutParams != null && mNegativeButton != null) {
				if (mButtonLayout.getChildCount() > 0) {
					mLayoutParams.setMargins(dip2px(12), dip2px(BUTTON_TOP), 0,
							dip2px(BUTTON_BOTTOM));
					mNegativeButton.setLayoutParams(mLayoutParams);
					mButtonLayout.addView(mNegativeButton, 1);
				} else {
					mNegativeButton.setLayoutParams(mLayoutParams);
					mButtonLayout.addView(mNegativeButton);
					// fix
					LinearLayout linearLayout = (LinearLayout) mAlertDialogWindow
							.findViewById(R.id.contentView);
					linearLayout.setPadding(0, 0, 0, dip2px(42));
				}
			}
			if (mBackgroundResId != 0) {
				LinearLayout linearLayout = (LinearLayout) mAlertDialogWindow
						.findViewById(R.id.material_background);
				linearLayout.setBackgroundResource(mBackgroundResId);
			}
			if (mBackgroundDrawable != null) {
				LinearLayout linearLayout = (LinearLayout) mAlertDialogWindow
						.findViewById(R.id.material_background);
				linearLayout.setBackground(mBackgroundDrawable);
			}

			if (mMessageContentView != null) {
				this.setContentView(mMessageContentView);
			}
			mAlertDialog.setCanceledOnTouchOutside(mCancel);
			if (mOnDismissListener != null) {
				mAlertDialog.setOnDismissListener(mOnDismissListener);
			}
		}

		public void setTitle(int resId) {
			mTitleView.setText(resId);
		}

		public void setTitle(CharSequence title) {
			mTitleView.setText(title);
		}

		public void setMessage(int resId) {
			mMessageView.setText(resId);
		}

		public void setMessage(CharSequence message) {
			mMessageView.setText(message);
		}

		/**
		 * set positive button
		 * 
		 * @param text
		 *            the name of button
		 * @param listener
		 */
		@SuppressWarnings("unused")
		public void setPositiveButton(String text,
				final View.OnClickListener listener) {
			Button button = new Button(mContext);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			button.setLayoutParams(params);
			button.setBackgroundResource(R.drawable.material_card);
			button.setTextColor(Color.argb(255, 35, 159, 242));
			button.setText(text);
			button.setGravity(Gravity.CENTER);
			button.setTextSize(14);
			button.setPadding(dip2px(12), 0, dip2px(32), dip2px(BUTTON_BOTTOM));
			button.setOnClickListener(listener);
			mButtonLayout.addView(button);
		}

		/**
		 * set negative button
		 * 
		 * @param text
		 *            the name of button
		 * @param listener
		 */
		@SuppressWarnings("unused")
		public void setNegativeButton(String text,
				final View.OnClickListener listener) {
			Button button = new Button(mContext);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			button.setLayoutParams(params);
			button.setBackgroundResource(R.drawable.material_card);
			button.setText(text);
			button.setTextColor(Color.argb(222, 0, 0, 0));
			button.setTextSize(14);
			button.setGravity(Gravity.CENTER);
			button.setPadding(0, 0, 0, dip2px(8));
			button.setOnClickListener(listener);
			if (mButtonLayout.getChildCount() > 0) {
				params.setMargins(20, 0, 10, dip2px(BUTTON_BOTTOM));
				button.setLayoutParams(params);
				mButtonLayout.addView(button, 1);
			} else {
				button.setLayoutParams(params);
				mButtonLayout.addView(button);
			}
		}

		public void setView(View view) {
			LinearLayout l = (LinearLayout) mAlertDialogWindow
					.findViewById(R.id.contentView);
			l.removeAllViews();
			ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			view.setLayoutParams(layoutParams);

			view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					System.out.println("-->" + hasFocus);
					mAlertDialogWindow
							.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
					// show imm
					InputMethodManager imm = (InputMethodManager) mContext
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
							InputMethodManager.HIDE_IMPLICIT_ONLY);

				}
			});

			l.addView(view);

			if (view instanceof ViewGroup) {

				ViewGroup viewGroup = (ViewGroup) view;

				for (int i = 0; i < viewGroup.getChildCount(); i++) {
					if (viewGroup.getChildAt(i) instanceof EditText) {
						EditText editText = (EditText) viewGroup.getChildAt(i);
						editText.setFocusable(true);
						editText.requestFocus();
						editText.setFocusableInTouchMode(true);
					}
				}
				for (int i = 0; i < viewGroup.getChildCount(); i++) {
					if (viewGroup.getChildAt(i) instanceof AutoCompleteTextView) {
						AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) viewGroup
								.getChildAt(i);
						autoCompleteTextView.setFocusable(true);
						autoCompleteTextView.requestFocus();
						autoCompleteTextView.setFocusableInTouchMode(true);
					}
				}
			}
		}

		public void setContentView(View contentView) {
			ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			contentView.setLayoutParams(layoutParams);
			if (contentView instanceof ListView) {
				setListViewHeightBasedOnChildren((ListView) contentView);
			}
			LinearLayout linearLayout = (LinearLayout) mAlertDialogWindow
					.findViewById(R.id.message_content_view);
			if (linearLayout != null) {
				linearLayout.removeAllViews();
				linearLayout.addView(contentView);
			}
			for (int i = 0; i < linearLayout.getChildCount(); i++) {
				if (linearLayout.getChildAt(i) instanceof AutoCompleteTextView) {
					AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) linearLayout
							.getChildAt(i);
					autoCompleteTextView.setFocusable(true);
					autoCompleteTextView.requestFocus();
					autoCompleteTextView.setFocusableInTouchMode(true);
				}
			}
		}

		public void setBackground(Drawable drawable) {
			LinearLayout linearLayout = (LinearLayout) mAlertDialogWindow
					.findViewById(R.id.material_background);
			linearLayout.setBackground(drawable);
		}

		public void setBackgroundResource(int resId) {
			LinearLayout linearLayout = (LinearLayout) mAlertDialogWindow
					.findViewById(R.id.material_background);
			linearLayout.setBackgroundResource(resId);
		}

		public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
			mAlertDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
		}
	}

	/**
	 * 动态测量listview-Item的高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	private TextView loadingTextView;

	public MaterialDialog inProgress(String loadingText) {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.layout_material_loading, null);
		loadingTextView = (TextView) view.findViewById(R.id.loading_text);
		loadingTextView.setText(loadingText);
		setContentView(view);
		return this;
	}

	public MaterialDialog inProgress() {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.layout_material_loading, null);
		loadingTextView = (TextView) view.findViewById(R.id.loading_text);
		loadingTextView.setText("Loading");
		setContentView(view);
		return this;
	}

	public MaterialDialog setLoadingText(String loading) {
		loadingTextView.setText(loading);
		return this;
	}

	public MaterialDialog setLoadingText(int id) {
		loadingTextView.setText(id);
		return this;
	}

	public void initListView() {
		if (mListView == null) {
			mListView = new ListView(new ContextThemeWrapper(mContext,
					R.style.normal_listview));
			mListView
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							dismiss();
							if (onItemClickListener != null)
								onItemClickListener.onItemClick(parent, view,
										position, id);
						}
					});
			mListView.setSelector(R.drawable.selector_listviewitem);
			setContentView(mListView);
		}
	}

	/**
	 * 
	 * @param drawableId
	 *            none .9.png
	 * @param text
	 * @return
	 */
	public MaterialDialog addItem(int drawableId, String text) {
		return addItem("drawable://" + drawableId, text, true);
	}

	public MaterialDialog addItem(int drawableId, String text, boolean hasborder) {
		return addItem("drawable://" + drawableId, text, hasborder);
	}

	public MaterialDialog addItem(String drawableURL, String text) {
		addItem(drawableURL, text, true);
		return this;
	}

	public MaterialDialog addItem(String drawableURL, String text,
			boolean hasborder) {
		initListView();
		if (mListView.getAdapter() == null) {
			DialogListAdapter listAdapter = new DialogListAdapter(mContext);
			mListView.setAdapter(listAdapter);
			GLog.sysout("setAdapter");
		}
		((DialogListAdapter) mListView.getAdapter()).addItem(drawableURL, text,
				hasborder);
		return this;
	}

	public AdapterView.OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public MaterialDialog setOnItemClickListener(
			AdapterView.OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
		return this;
	}

	public ListView getmListView() {
		return mListView;
	}

	public class DialogListAdapter extends BaseAdapter {

		private Context context;
		private List<ListItem> items;

		public DialogListAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			if (items == null)
				return 0;
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_material_listitem, null);
				holder = new Holder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			if (items.get(position).hasborder) {
				holder.imageview
						.setBackgroundResource(R.drawable.common_image_bg);
			} else {
				holder.imageview.setBackgroundResource(R.color.transparent);
			}
			ImageLoaderUtils.displayImage(items.get(position).drawableURL,
					holder.imageview, R.drawable.default_avatar, true);
			holder.textview.setText(items.get(position).text);
			return convertView;
		}

		public void addItem(String drawableURL, String text) {
			if (items == null)
				items = new ArrayList<ListItem>();
			items.add(new ListItem(drawableURL, text));
			notifyDataSetChanged();
		}

		public void addItem(String drawableURL, String text, boolean hasborder) {
			if (items == null)
				items = new ArrayList<ListItem>();
			items.add(new ListItem(drawableURL, text, hasborder));
			notifyDataSetChanged();
		}

		public class Holder {
			ImageView imageview;
			TextView textview;

			public Holder(View v) {
				imageview = (ImageView) v.findViewById(R.id.icon);
				textview = (TextView) v.findViewById(R.id.name);
			}
		}

		public class ListItem {
			public ListItem(String drawableURL, String text) {
				this.drawableURL = drawableURL;
				this.text = text;
				hasborder = true;
			}

			public ListItem(String drawableURL, String text, boolean hasborder) {
				this.drawableURL = drawableURL;
				this.text = text;
				this.hasborder = hasborder;
			}

			public String drawableURL;
			public String text;
			public boolean hasborder = true;
		}
	}
}
