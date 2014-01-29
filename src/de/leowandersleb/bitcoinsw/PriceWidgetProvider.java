package de.leowandersleb.bitcoinsw;

import java.security.InvalidParameterException;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

public class PriceWidgetProvider extends AppWidgetProvider implements ResultReceiver {
	private RemoteViews views;
	private Context context;
	private String name;
	private AppWidgetManager appWidgetManager;

	@Override
	public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
		this.context = context;
		this.appWidgetManager = appWidgetManager;
		this.name = this.getClass().getName();
		this.views = new RemoteViews(context.getPackageName(), R.layout.price_widget_content);

		views.setTextViewText(R.id.widget_bitstamp, context.getText(R.string.bitstamp));
		views.setTextViewText(R.id.widget_mtgox, context.getText(R.string.mtgox));
		views.setTextViewText(R.id.widget_huobi, context.getText(R.string.huobi));
		views.setTextViewText(R.id.widget_btcchina, context.getText(R.string.btc_china));

		// TODO: Jan 29, 2014 Leo: clicking the widget should initialize a refresh
		// Intent intent = new Intent();
		// intent.setAction("android.intent.action.VIEW");
		// intent.setComponent(ComponentName.unflattenFromString("de.leowandersleb.fluxcards/de.leowandersleb.fluxcards.FluxCardsActivity"));
		// views.setOnClickPendingIntent(R.id.widget_frame, PendingIntent.getActivity(context, 0, intent, 0));

		appWidgetManager.updateAppWidget(new ComponentName(context.getPackageName(), name), views);

		Tools.getRatesAsync(this);
	}

	@Override
	public void setResult(int resId, float result) {
		int widgetId = 0;
		String text = "";
		switch (resId) {
		case R.id.bitstamp_text:
			widgetId = R.id.widget_bitstamp;
			text = context.getString(R.string.dollar_price_short, result);
			break;
		case R.id.mtgox_text:
			widgetId = R.id.widget_mtgox;
			text = context.getString(R.string.dollar_price_short, result);
			break;
		case R.id.huobi_text:
			widgetId = R.id.widget_huobi;
			text = context.getString(R.string.yuan_price_short, result);
			break;
		case R.id.btcchina_text:
			widgetId = R.id.widget_btcchina;
			text = context.getString(R.string.yuan_price_short, result);
			break;
		default:
			throw new InvalidParameterException(resId + " is not a valid resId");
		}
		views.setTextViewText(widgetId, text);
		appWidgetManager.updateAppWidget(new ComponentName(context.getPackageName(), name), views);
	}
}
