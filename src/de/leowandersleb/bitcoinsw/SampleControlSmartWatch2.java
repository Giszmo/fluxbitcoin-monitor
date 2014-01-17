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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.control.ControlObjectClickEvent;
import com.sonyericsson.extras.liveware.extension.util.control.ControlTouchEvent;

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
		Log.d(Constants.TAG, "SampleControlSmartWatch2.java::onStart ");
	}

	@Override
	public void onStop() {
		// Nothing to do. Animation is handled in onPause.
	}

	@Override
	public void onResume() {
		Log.d(Constants.TAG, "Starting");
		showLayout(R.layout.sample_control_2, new Bundle[] {});
		ExecutorService threadPool = Executors.newFixedThreadPool(4);
		new GetMtGoxRateTask(context, this).executeOnExecutor(threadPool);
		new GetBitstampRateTask(context, this).executeOnExecutor(threadPool);
		new GetBTCChinaRateTask(context, this).executeOnExecutor(threadPool);
		new GetHuobiRateTask(context, this).executeOnExecutor(threadPool);
	}

	@Override
	public void onPause() {
		Log.d(Constants.TAG, "SampleControlSmartWatch2.java::onPause ");
	}

	@Override
	public void onTouch(final ControlTouchEvent event) {
	}

	@Override
	public void onObjectClick(final ControlObjectClickEvent event) {
	}

	@Override
	public void setResult(int resId, String result) {
		sendText(resId, result);
	}
}
