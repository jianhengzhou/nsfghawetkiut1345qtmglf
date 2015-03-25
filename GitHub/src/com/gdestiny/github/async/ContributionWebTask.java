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

import com.gdestiny.github.utils.ToastUtils;

public abstract class ContributionWebTask {

	private Context context;
	private Handler handler;

	private boolean exception = false;

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

	public abstract void onProgress(int progress);

	public abstract void onSuccess(String result);

	public void onException(Exception ex) {
		if (exception) {
			ToastUtils.show(context, ex.getMessage());
			exception = false;
		}
	}

	public void execute(String params) {
		new BaseAsyncTask<String, Integer, String>() {

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
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
			protected String doInBackground(String... params) {
				HttpClient client = new DefaultHttpClient();
				try {
					HttpGet get = new HttpGet("https://github.com/" + params[0]);
					// ��Ʋ���
					HttpParams httpParameters = new BasicHttpParams();
					int timeoutConnection = 1000 * 20;
					HttpConnectionParams.setConnectionTimeout(httpParameters,
							timeoutConnection);
					HttpConnectionParams
							.setSoTimeout(httpParameters, 30 * 1000);
					HttpContext localContext = new BasicHttpContext();
					// ��Ӧ
					HttpResponse response = client.execute(get, localContext);
					HttpEntity entity = response.getEntity();
					// long totalLength = entity.getContentLength();
					long totalLength = 66666;// δ֪��ҳ��仯������Ϊһ�����ֵ
					InputStream is = entity.getContent();
					System.out.println("totalLength:" + totalLength);
					// other
					// java.net.URL url = new java.net.URL("https://github.com/"
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
					// ��ȡ����
					ByteArrayOutputStream data = new ByteArrayOutputStream();// �½�һ�ֽ����������
					byte[] buffer = new byte[1024];// ���ڴ��п���һ�λ���������������������
					int len = 0;
					int progress = 0;
					while ((len = is.read(buffer)) != -1) {
						progress += len;
						// (int) ((progress / (float) totalLength) * 100)
						publishProgress((int) ((progress / (float) totalLength) * 100));
						data.write(buffer, 0, len); // ����������֮�󽫻�����������д�������
					}
					publishProgress(100);
					System.out.println("progress:" + progress);
					is.close();
					// ���parse
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
		}.execute(params);
	}
}
