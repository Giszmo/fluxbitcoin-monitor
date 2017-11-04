package de.leowandersleb.bitcoinsw;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

class GetBitTRexRateTask extends AsyncTask<Void, Void, Float> {
	private ResultReceiver receiver;

	public GetBitTRexRateTask(ResultReceiver receiver) {
		this.receiver = receiver;
	}

	@Override
	protected Float doInBackground(Void... bla) {
		String url = "https://bittrex.com/api/v1.1/public/getticker?market=USDT-BTC";
		Float retVal = -2f;
		JsonReader reader;
		try {
			HttpClient hc = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			HttpResponse response = hc.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				InputStream inputStream = response.getEntity().getContent();
				reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
				Log.d(Constants.TAG, "doInBackground " + reader.toString());
				reader.beginObject();
				while (reader.hasNext()) {
					String name = reader.nextName();
					if ("result".equals(name)) {
						reader.beginObject();
					} else if ("Last".equals(name)) {
						retVal = (float) reader.nextDouble();
						break;
					} else {
						reader.skipValue();
					}
				}
				reader.close();
				inputStream.close();
			}
		} catch (IOException | IllegalStateException e) {
			Log.e(Constants.TAG, "SampleControlSmartWatch2.java::doInBackground ");
		}
		return retVal;
	}

	@Override
	protected void onPostExecute(Float result) {
		receiver.setResult(R.id.widget_bittrex, result);
	}
}