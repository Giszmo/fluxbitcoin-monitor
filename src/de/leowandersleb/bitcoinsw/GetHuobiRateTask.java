package de.leowandersleb.bitcoinsw;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

class GetHuobiRateTask extends AsyncTask<Void, Void, Float> {
	private Context context;
	private StringResultReceiver receiver;

	public GetHuobiRateTask(Context context, StringResultReceiver receiver) {
		this.context = context;
		this.receiver = receiver;
	}

	@Override
	protected Float doInBackground(Void... bla) {
		String url = "https://detail.huobi.com/staticmarket/detail.html";
		Float retVal = -2f;
		JsonReader reader = null;
		try {
			HttpClient hc = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			HttpResponse response = hc.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				InputStream inputStream = response.getEntity().getContent();
				inputStream.skip(12);
				reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
				reader.setLenient(true);
				reader.beginObject();
				while (reader.hasNext()) {
					String name = reader.nextName();
					if ("p_new".equals(name)) {
						retVal = (float) reader.nextDouble();
						break;
					} else {
						reader.skipValue();
					}
				}
				reader.close();
				inputStream.close();
			}
		} catch (IOException e) {
			Log.e(Constants.TAG, "SampleControlSmartWatch2.java::doInBackground ");
		}
		return retVal;
	}

	@Override
	protected void onPostExecute(Float result) {
		receiver.setResult(R.id.huobi_text, context.getString(R.string.huobi_price, result));
	}
}