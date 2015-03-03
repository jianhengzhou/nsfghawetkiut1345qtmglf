package com.gdestiny.github.utils;

import java.io.Serializable;

import com.gdestiny.github.ui.activity.TestActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

/**
 * 2015.1.17
 * 
 * @author gdestiny
 * 
 */
public class IntentUtils {

	private IntentUtils() {
		throw new AssertionError();
	}

	public static void startTest(Context context, String str) {
		Intent intent = new Intent(context, TestActivity.class);
		intent.putExtra("test", str);
		context.startActivity(intent);
	}

	public static void start(Context context, Intent intent) {
		context.startActivity(intent);
	}

	public static void start(Context context, Intent intent, Bundle bundle) {
		context.startActivity(intent, bundle);
	}

	public static void start(Context context, Class<?> cls) {
		Intent intent = new Intent(context, cls);
		context.startActivity(intent);
	}

	public static void start(Context context, Class<?> cls, String name,
			Serializable data) {
		Intent intent = new Intent(context, cls);
		intent.putExtra(name, data);
		context.startActivity(intent);
	}

	public static IntentBuilder create(Context context, Class<?> cls) {
		return new IntentBuilder().create(context, cls);
	}

	public static IntentBuilder create(Context context) {
		return new IntentBuilder().create(context);
	}

	public static void setResultCancle(Activity context) {
		context.setResult(Activity.RESULT_CANCELED);
		context.finish();
	}

	public static class IntentBuilder {
		private Intent intent;
		private Context context;

		public IntentBuilder create(Context context, Class<?> cls) {
			this.intent = new Intent(context, cls);
			this.context = context;
			return this;
		}

		public IntentBuilder create(Context context) {
			this.intent = new Intent();
			this.context = context;
			return this;
		}

		public IntentBuilder putExtra(String name, boolean data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, boolean[] data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, Bundle data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, byte data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, byte[] data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, char data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, char[] data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, CharSequence data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, CharSequence[] data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, double data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, double[] data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, float data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, float[] data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, int data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, int[] data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, long data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, long[] data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, Parcelable data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, Parcelable[] data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, Serializable data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, short data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, short[] data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, String data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtra(String name, String[] data) {
			if (intent != null) {
				intent.putExtra(name, data);
			}
			return this;
		}

		public IntentBuilder putExtras(Bundle extras) {
			if (intent != null) {
				intent.putExtras(extras);
			}
			return this;
		}

		public IntentBuilder putExtras(Intent src) {
			if (intent != null) {
				intent.putExtras(src);
			}
			return this;
		}

		public IntentBuilder setResultOk() {
			((Activity) context).setResult(Activity.RESULT_OK, intent);
			return this;
		}

		public void start() {
			context.startActivity(intent);
		}

		public void finish() {
			((Activity) context).finish();
		}

		// public void startForResult(Object act_frag, int requestCode) {
		// if (act_frag instanceof Fragment)
		// ((Fragment) act_frag).startActivityForResult(intent,
		// requestCode);
		// else if (act_frag instanceof Activity)
		// ((Activity) act_frag).startActivityForResult(intent,
		// requestCode);
		// else
		// throw new IllegalArgumentException(
		// "the argument can only be fragment or fragment");
		// }

		public void startForResult(Fragment fragment, int requestCode) {
			fragment.startActivityForResult(intent, requestCode);
		}

		public void startForResult(Activity activity, int requestCode) {
			activity.startActivityForResult(intent, requestCode);
		}

	}
}
