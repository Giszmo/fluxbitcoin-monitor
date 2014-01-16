/*
Copyright (c) 2011, Sony Ericsson Mobile Communications AB
Copyright (c) 2011-2013, Sony Mobile Communications AB

 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 * Neither the name of the Sony Ericsson Mobile Communications AB / Sony Mobile
 Communications AB nor the names of its contributors may be used to endorse or promote
 products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;

import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.control.ControlObjectClickEvent;
import com.sonyericsson.extras.liveware.extension.util.control.ControlTouchEvent;
import com.sonymobile.smartconnect.extension.controlsample.R;

/**
 * The sample control for SmartWatch handles the control on the accessory. This class exists in one instance for every supported host application that we have
 * registered to
 */
class SampleControlSmartWatch2 extends ControlExtension implements StringResultReceiver {
	private Context context;

	/**
	 * Create sample control.
	 * 
	 * @param hostAppPackageName
	 *            Package name of host application.
	 * @param context
	 *            The context.
	 * @param handler
	 *            The handler to use
	 */
	SampleControlSmartWatch2(final String hostAppPackageName, final Context context) {
		super(context, hostAppPackageName);
		this.context = context;
	}

	/**
	 * Get supported control width.
	 * 
	 * @param context
	 *            The context.
	 * @return the width.
	 */
	public static int getSupportedControlWidth(Context context) {
		return context.getResources().getDimensionPixelSize(R.dimen.smart_watch_2_control_width);
	}

	/**
	 * Get supported control height.
	 * 
	 * @param context
	 *            The context.
	 * @return the height.
	 */
	public static int getSupportedControlHeight(Context context) {
		return context.getResources().getDimensionPixelSize(R.dimen.smart_watch_2_control_height);
	}

	@Override
	public void onDestroy() {
		Log.d(Constants.TAG, "SampleControlSmartWatch onDestroy");
	};

	@Override
	public void onStart() {
	}

	@Override
	public void onStop() {
		// Nothing to do. Animation is handled in onPause.
	}

	@Override
	public void onResume() {
		Log.d(Constants.TAG, "Starting");
		showLayout(R.layout.sample_control_2, new Bundle[] {});
		new GetMtGoxRateTask(context, this).execute();
	}

	@Override
	public void onPause() {
		Log.d(Constants.TAG, "Stopping animation");
	}

	@Override
	public void onTouch(final ControlTouchEvent event) {
		Log.d(Constants.TAG, "onTouch() " + event.getAction());
		if (event.getAction() == Control.Intents.TOUCH_ACTION_RELEASE) {
			Log.d(Constants.TAG, "Toggling animation");
		}
	}

	@Override
	public void onObjectClick(final ControlObjectClickEvent event) {
		Log.d(Constants.TAG, "onObjectClick() " + event.getClickType());
		// if (event.getLayoutReference() != -1) {
		// mLayout.onClick(event.getLayoutReference());
		// }
	}

	private class GetMtGoxRateTask extends AsyncTask<Void, Void, Float> {
		private Context context;
		private StringResultReceiver receiver;

		public GetMtGoxRateTask(Context context, StringResultReceiver receiver) {
			this.context = context;
			this.receiver = receiver;
		}

		@Override
		protected Float doInBackground(Void... bla) {
			String url = "http://data.mtgox.com/api/2/BTCUSD/money/ticker_fast";
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
					Log.d(Constants.TAG, "SampleControlSmartWatch2.java::doInBackground " + reader.toString());
					reader.beginObject();
					while (reader.hasNext()) {
						String name = reader.nextName();
						if ("result".equals(name)) {
							String result = reader.nextString();
							if (!"success".equals(result)) {
								return -1f;
							}
						} else if ("data".equals(name) || "last".equals(name)) {
							reader.beginObject();
						} else if ("value".equals(name)) {
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
			receiver.setResult(context.getString(R.string.mtgox_price, result));
		}
	}

	@Override
	public void setResult(String result) {
		sendText(R.id.mtgox_text, result);
	}
}
