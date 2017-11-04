package de.leowandersleb.bitcoinsw;

import java.security.InvalidParameterException;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class PriceWidgetIntentReceiver extends BroadcastReceiver implements ResultReceiver {
	private static PriceWidgetIntentReceiver INSTANCE = null;
	private static RemoteViews views;
	private static AppWidgetManager manager;
	private static Context context;

	private static synchronized PriceWidgetIntentReceiver getInstance(Context context) {
		PriceWidgetIntentReceiver.context = context;
		if (INSTANCE == null) {
			INSTANCE = new PriceWidgetIntentReceiver();
		}
		return INSTANCE;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		PriceWidgetIntentReceiver.context = context;
		if ("de.leowandersleb.bitcoinsw.REFRESH".equals(intent.getAction())) {
			updatePriceWidgets(context);
		}
	}

	@Override
	public void setResult(int resId, float result) {
		String text = "";
		switch (resId) {
		case R.id.widget_bitstamp:
			text = context.getString(R.string.dollar_price_short, result);
			break;
		case R.id.widget_kraken:
			text = context.getString(R.string.euro_price_short, result);
			break;
		case R.id.widget_bitfinex:
			text = context.getString(R.string.dollar_price_short, result);
			break;
		case R.id.widget_bittrex:
			text = context.getString(R.string.dollar_price_short, result);
			break;
		default:
			throw new InvalidParameterException(resId + " is not a valid resId");
		}
		views.setTextViewText(resId, text);
		manager.updateAppWidget(new ComponentName(context, PriceWidgetProvider.class), views);
	}

	public static void updatePriceWidgets(Context context) {
		views = new RemoteViews(context.getPackageName(), R.layout.price_widget_content);
		views.setTextViewText(R.id.widget_bitstamp, context.getText(R.string.bitstamp));
		views.setTextViewText(R.id.widget_kraken, context.getText(R.string.kraken));
		views.setTextViewText(R.id.widget_bitfinex, context.getText(R.string.bitfinex));
		views.setTextViewText(R.id.widget_bittrex, context.getText(R.string.bittrex));

		// TODO: Jan 29, 2014 Leo: clicking the widget should initialize a refresh
		// Intent intent = new Intent();
		// intent.setAction("android.intent.action.VIEW");
		// intent.setComponent(ComponentName.unflattenFromString("de.leowandersleb.fluxcards/de.leowandersleb.fluxcards.FluxCardsActivity"));
		// views.setOnClickPendingIntent(R.id.widget_frame, PendingIntent.getActivity(context, 0, intent, 0));

		// REMEMBER TO ALWAYS REFRESH YOUR BUTTON CLICK LISTENERS!!!
		views.setOnClickPendingIntent(R.id.icon, PriceWidgetProvider.buildButtonPendingIntent(context));

		manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(new ComponentName(context, PriceWidgetProvider.class), views);

		Tools.getRatesAsync(getInstance(context));

		PriceWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), views);
	}
}