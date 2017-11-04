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

class GetBitfinexRateTask extends AsyncTask<Void, Void, Float> {
	private ResultReceiver receiver;

	GetBitfinexRateTask(ResultReceiver receiver) {
		this.receiver = receiver;
	}

	@Override
	protected Float doInBackground(Void... bla) {
		String url = "https://api.bitfinex.com/v1/pubticker/btcusd";
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
				reader.setLenient(true);
				reader.beginObject();
				while (reader.hasNext()) {
					String name = reader.nextName();
					if ("last_price".equals(name)) {
						retVal = (float) reader.nextDouble();
						break;
					} else {
						reader.skipValue();
					}
				}
				reader.close();
				inputStream.close();
			}
		} catch (IOException ignore) {
		}
		return retVal;
	}

	@Override
	protected void onPostExecute(Float result) {
		receiver.setResult(R.id.widget_bitfinex, result);
	}
}