package com.info_leaker;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class WidgetProvider extends AppWidgetProvider{
	private static final String TAG = "WidgetProvider";
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate");
		context.startService(new Intent(context, WidgetService.class));
    }

}
