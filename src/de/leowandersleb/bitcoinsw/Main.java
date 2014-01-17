package de.leowandersleb.bitcoinsw;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity implements StringResultReceiver {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TextView description = (TextView) findViewById(R.id.description);
		description.setText(Html.fromHtml((String) getText(R.string.description)));
		description.setMovementMethod(LinkMovementMethod.getInstance());
		EditText bitcoinaddress = (EditText) findViewById(R.id.editTextDonationaddress);
		bitcoinaddress.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("Bitcoins for Leo Wandersleb", Main.this.getText(R.string.bitcoindonationaddress));
				clipboard.setPrimaryClip(clip);
				Toast.makeText(Main.this, "Address copied ot clipboard", Toast.LENGTH_LONG).show();
			}
		});
		ExecutorService threadPool = Executors.newFixedThreadPool(4);
		new GetMtGoxRateTask(this, this).executeOnExecutor(threadPool);
		new GetBitstampRateTask(this, this).executeOnExecutor(threadPool);
		new GetBTCChinaRateTask(this, this).executeOnExecutor(threadPool);
		new GetHuobiRateTask(this, this).executeOnExecutor(threadPool);
	}

	@Override
	public void setResult(int resId, String result) {
		TextView tickerText = (TextView) findViewById(R.id.tickertext);
		tickerText.setText(tickerText.getText() + "\n\n" + result);
	}
}
