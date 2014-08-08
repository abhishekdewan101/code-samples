package com.example.widget;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RemoteViews;

import java.util.Random;


public class MyActivity extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {

        ComponentName componentName = new ComponentName(context,MyActivity.class);
        int [] allWidgetIds =appWidgetManager.getAppWidgetIds(componentName);
        for(int widgetId:allWidgetIds){
            int number = (new Random().nextInt(100));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.activity_my);

            Intent intent = new Intent(context,MyActivity.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);

            appWidgetManager.updateAppWidget(widgetId,remoteViews);
        }
    }
}
