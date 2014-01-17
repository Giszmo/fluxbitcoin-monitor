package de.leowandersleb.bitcoinsw;

import android.content.Context;
import android.os.AsyncTask;

import com.sonymobile.smartconnect.extension.controlsample.R;

class GetHuobiRateTask extends AsyncTask<Void, Void, Float> {
	private Context context;
	private StringResultReceiver receiver;

	public GetHuobiRateTask(Context context, StringResultReceiver receiver) {
		this.context = context;
		this.receiver = receiver;
	}

	@Override
	protected Float doInBackground(Void... bla) {
		return null;
	}

	@Override
	protected void onPostExecute(Float result) {
		receiver.setResult(R.id.huobi_text, context.getString(R.string.huobi_price));
	}
}