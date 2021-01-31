package com.barros.bitcoinwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class BitcoinWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {}
    override fun onDisabled(context: Context) {}
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable, "Error")
    }

    val scope = CoroutineScope(Dispatchers.Default + exceptionHandler)

    scope.launch {
        val response = BitcoinService.getBitcoinService().getInfo()
        val price = response.body()!!.data[0].quote.currency.formatPrice()
        val views = RemoteViews(context.packageName, R.layout.bitcoin_widget)
        views.setTextViewText(R.id.bitcoin_text, price)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}