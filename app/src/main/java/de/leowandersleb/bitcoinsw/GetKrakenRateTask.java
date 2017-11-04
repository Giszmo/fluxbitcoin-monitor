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

class GetKrakenRateTask extends AsyncTask<Void, Void, Float> {
	private ResultReceiver receiver;

	public GetKrakenRateTask(ResultReceiver receiver) {
		this.receiver = receiver;
	}

	@Override
	protected Float doInBackground(Void... bla) {
		String url = "https://api.kraken.com/0/public/Ticker?pair=XBTEUR";
		// {"error":[],
		// "result":{"XXBTZEUR":{"a":["385.45670","1"],"b":["385.22000","4"],
		// "c":["385.22000","0.10161325"],"v":["199.10108517","1440.81752671"],"p":["389.88880","395.96287"],"t":[304,2734],"l":["385.00000","385.00000"],"h":["395.69400","403.84700"],"o":"394.89000"}}}
		Float retVal = -2f;
		JsonReader reader = null;
		try {
			HttpClient hc = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			HttpResponse response = hc.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				InputStream inputStream = response.getEntity().getContent();
				reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
				reader.beginObject();
				Log.d(Constants.TAG, "GetKrakenRateTask.java::doInBackground " + reader.toString());
				while (reader.hasNext()) {
					String name = reader.nextName();
					Log.d(Constants.TAG, "GetKrakenRateTask.java::doInBackground " + name);
					if ("result".equals(name)) {
						reader.beginObject();
						while (reader.hasNext()) {
							name = reader.nextName();
							Log.d(Constants.TAG, "GetKrakenRateTask.java::doInBackground " + name);
							if ("XXBTZEUR".equals(name)) {
								reader.beginObject();
								while (reader.hasNext()) {
									name = reader.nextName();
									if ("c".equals(name)) {
										reader.beginArray();
										retVal = (float) reader.nextDouble();
										break;
									} else {
										reader.skipValue();
									}
								}
								break;
							} else {
								reader.skipValue();
							}
						}
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
		receiver.setResult(R.id.widget_kraken, result);
	}
}