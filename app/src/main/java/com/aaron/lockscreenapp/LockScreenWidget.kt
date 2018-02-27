package com.aaron.lockscreenapp

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.app.PendingIntent
import android.util.Log

/**
 * 桌面小插件，点击关闭屏幕
 */
class LockScreenWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        Log.d("LockScreenWidget","onUpdate"+appWidgetIds.size)
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        // 当第一个插件被添加的时候调用
        Log.d("LockScreenWidget","onEnabled")
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        // 当最后一个插件被删除的时候调用
        Log.d("LockScreenWidget","onDisabled")
    }

    companion object {
        // 刷新插件
        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {
            val startServiceIntent = Intent(context,LockService::class.java)
            startServiceIntent.putExtra("Tap","lock")
            val pIntent = PendingIntent.getService(context, 0, startServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val views = RemoteViews(context.packageName, R.layout.lock_screen_widget)
            views.setOnClickPendingIntent(R.id.imageView,pIntent)
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

