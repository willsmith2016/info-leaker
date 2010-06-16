package com.info_leaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class WidgetInfoListenerHelper extends BroadcastReceiver{
	Context context;
    
    WidgetInfoListenerHelper listener;
       
    //construct    
    public WidgetInfoListenerHelper(Context c){   
        context = c;
        //to instance it
        listener = this;
    }   
       
    public void registerAction(String action){
        IntentFilter filter = new IntentFilter();
        filter.addAction(action);
           
        context.registerReceiver(listener,filter);
    }

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Bundle b = arg1.getExtras();
		
	}
}
