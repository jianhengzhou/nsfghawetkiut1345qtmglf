package com.gdestiny.github.async;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.gdestiny.github.abstracts.async.BaseAsyncTask;
import com.gdestiny.github.utils.CacheUtils;
import com.gdestiny.github.utils.GLog;
import com.gdestiny.github.utils.ToastUtils;

public abstract class ContributionWebTask {

	private Context context;
	private Handler handler;

	private boolean exception = false;
	private boolean reloadFlag = false;
	private boolean loadFromCache = true;

	private BaseAsyncTask<String, Integer, String> task;

	public ContributionWebTask(Context context) {
		this.context = context;
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				ContributionWebTask.this.onException((Exception) msg.obj);
			}

		};
	}

	public abstract void onPrev();

	public abstract void onCancelled();

	public abstract void onProgress(int progress);

	public abstract void onSuccess(String result);

	public void onException(Exception ex) {
		if (exception) {
			String message = ex.getMessage();
			if (!TextUtils.isEmpty(message))
				ToastUtils.show(context, message);
			exception = false;
		}
	}

	public void cancle() {
		if (task != null && !task.isCancelled())
			GLog.sysout("ask.cancel(true);");
		task.cancel(true);
	}

	public void reload(String params) {
		reloadFlag = true;
		cancle();
		execute(params);
	}

	public void execute(String params) {
		task = new BaseAsyncTask<String, Integer, String>() {

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				reloadFlag = false;
				ContributionWebTask.this.onSuccess(result);
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				ContributionWebTask.this.onPrev();
				exception = false;
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
				int v = values[0];
				if (v >= 100)
					v = 99;
				ContributionWebTask.this.onProgress(v);
				// System.out.println("progress:" +v);
			}

			@Override
			protected void onCancelled() {
				super.onCancelled();
				if (!reloadFlag)
					ContributionWebTask.this.onCancelled();
			}

			@Override
			protected String doInBackground(String... params) {
				synchronized (this) {
					if (loadFromCache) {
						loadFromCache = false;
						String contribution = CacheUtils
								.getContent(CacheUtils.NAME.CONTRIBUTION);
						if (!TextUtils.isEmpty(contribution)) {
							return contribution;
						}
					}
					HttpClient client = new DefaultHttpClient();
					try {
						HttpGet get = new HttpGet("https://github.com/"
								+ params[0]);
						// 设计参数
						HttpParams httpParameters = new BasicHttpParams();
						int timeoutConnection = 1000 * 20;
						HttpConnectionParams.setConnectionTimeout(
								httpParameters, timeoutConnection);
						HttpConnectionParams.setSoTimeout(httpParameters,
								30 * 1000);
						HttpContext localContext = new BasicHttpContext();
						// 响应
						HttpResponse response = client.execute(get,
								localContext);
						HttpEntity entity = response.getEntity();
						// long totalLength = entity.getContentLength();
						long totalLength = 66666;// 未知，页面变化不大，设为一个大概值
						InputStream is = entity.getContent();
						System.out.println("totalLength:" + totalLength);
						// other
						// java.net.URL url = new
						// java.net.URL("https://github.com/"
						// + params[0]);
						// java.net.HttpURLConnection connection =
						// (java.net.HttpURLConnection) url
						// .openConnection();
						// connection
						// .setRequestProperty("Accept-Encoding", "identity");
						// connection.setRequestMethod("GET");
						// connection.setReadTimeout(5000);
						// connection.connect();
						// totalLength = connection.getContentLength();
						// System.out.println("totalLength2:" + totalLength);
						// 读取数据
						ByteArrayOutputStream data = new ByteArrayOutputStream();// 新建一字节数组输出流
						byte[] buffer = new byte[1024];// 在内存中开辟一段缓冲区，接受网络输入流
						int len = 0;
						int progress = 0;
						while ((len = is.read(buffer)) != -1) {
							if (isCancelled())
								throw new Exception();
							progress += len;
							// (int) ((progress / (float) totalLength) * 100)
							publishProgress((int) ((progress / (float) totalLength) * 100));
							data.write(buffer, 0, len); // 缓冲区满了之后将缓冲区的内容写到输出流
						}
						publishProgress(100);
						System.out.println("progress:" + progress);
						is.close();
						// 结果parse
						String result = new String(data.toByteArray(), "utf-8");
						String viewposrt = "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no\" />";
						int start = result
								.lastIndexOf("<div class=\"boxed-group flush\">");
						int end = result
								.indexOf("<div class=\"activity-listing contribution-activity js-contribution-activity\">");
						final String sub = "<html><body><link href=\"file:///android_asset/contribution.css\" media=\"all\" rel=\"stylesheet\" /><div style=\"width:721;height:110\">"
								+ viewposrt
								+ result.substring(start, end)
								+ "</div></body></html>";
						sub.replace("<a href=", "<a ");
						// System.out.println(sub);
						// throw new Exception("test exception");
						CacheUtils.cacheString(CacheUtils.NAME.CONTRIBUTION,
								sub);
						return sub;
					} catch (Exception e) {
						e.printStackTrace();
						exception = true;
						Message msg = handler.obtainMessage();
						msg.obj = e;
						handler.sendMessage(msg);
					} finally {
						client.getConnectionManager().shutdown();
					}
					return null;
				}
			}
		};
		task.execute(params);
	}
}
