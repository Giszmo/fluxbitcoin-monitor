package de.leowandersleb.bitcoinsw;

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

public class Main extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TextView description = findViewById(R.id.description);
		description.setText(Html.fromHtml((String) getText(R.string.description)));
		description.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
