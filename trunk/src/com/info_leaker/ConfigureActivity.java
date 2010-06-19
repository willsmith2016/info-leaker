package com.info_leaker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ConfigureActivity extends Activity
    implements RadioGroup.OnCheckedChangeListener{

        private Button mCancel;
        private Button mSave;
        private CheckBox mSMS, mCall, mNewSMS;
        private RadioButton mNam, mNum;

        private boolean msms;		// show sms
        private boolean mcall;		// show call log
        private boolean mnewsms;	// show new sms
        private boolean mshownam;	// show contact by name or number
        int mAppWidgetId;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //setResult(RESULT_CANCELED);
            setContentView(R.layout.configure);
            mCancel = (Button)findViewById(R.id.conf_cancel);
            mSave = (Button)findViewById(R.id.conf_save);
            mSMS = (CheckBox)findViewById(R.id.conf_sms);
            mCall = (CheckBox)findViewById(R.id.conf_calllog);
            mNewSMS = (CheckBox)findViewById(R.id.conf_remsms);
            mNam = (RadioButton)findViewById(R.id.conf_byname);
            mNum = (RadioButton)findViewById(R.id.conf_bynum);

            Intent intent = this.getIntent();
            Bundle bundle = intent.getBundleExtra(Common.CONF);
            // If restoring, read from bundle
            if (bundle != null) {
            	this.msms = bundle.getBoolean(Common.SMS);
            	this.mcall = bundle.getBoolean(Common.CALL);
            	this.mnewsms = bundle.getBoolean(Common.NEWSMS);
            	this.mshownam = bundle.getBoolean(Common.BYNAM);
            	
            	this.mSMS.setChecked(msms);
            	this.mCall.setChecked(mcall);
            	this.mNewSMS.setChecked(mnewsms);
            	if(mshownam)
            		this.mNam.setSelected(true);
            	else
            		this.mNum.setSelected(true);
            	//mAppWidgetId = bundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, 
                //		AppWidgetManager.INVALID_APPWIDGET_ID);
            }else{
            	this.mSMS.setChecked(true);
            	this.mCall.setChecked(true);
            	this.mNewSMS.setChecked(true);
            }
            mCancel.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					setConfigureResult(RESULT_CANCELED);
				}            	
            });
            mSave.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					setConfigureResult(RESULT_OK);
				}
            });
            mSMS.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					msms = isChecked;
				}
            });
            mCall.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					mcall = isChecked;
				}
            });
            mNewSMS.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					mnewsms = isChecked;
				}
            });

        }
        // return result
        public void setConfigureResult(int resultCode) {
        	Context context = getBaseContext();
	    	Intent intent = new Intent(context, WidgetService.class);
        	if(resultCode == RESULT_OK){
	            Bundle bundle = new Bundle();
	            bundle.putBoolean(Common.SMS, msms);
	            bundle.putBoolean(Common.CALL, mcall);
	            bundle.putBoolean(Common.NEWSMS, mnewsms);
	            bundle.putBoolean(Common.BYNAM, mshownam);
	            intent.putExtra(Common.CONF, bundle);
	    	}
        	// back to call service
        	context.startService(intent);
            setResult(resultCode, intent);
			ConfigureActivity.this.finish();
       }

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch(checkedId){
			case R.id.conf_byname:
				mNam.setSelected(true);
				break;
			case R.id.conf_bynum:
				mNum.setSelected(true);
				break;
			}
		}
		
		@Override 
		public void onSaveInstanceState(Bundle savedInstanceState) { 
			savedInstanceState.putBoolean(Common.SMS, msms);
			savedInstanceState.putBoolean(Common.CALL, mcall);
			savedInstanceState.putBoolean(Common.NEWSMS, mnewsms);
			savedInstanceState.putBoolean(Common.BYNAM, mshownam);
			super.onSaveInstanceState(savedInstanceState); 
		}
		
		@Override 
		public void onRestoreInstanceState(Bundle savedInstanceState) { 
			super.onRestoreInstanceState(savedInstanceState); 
			msms = savedInstanceState.getBoolean(Common.SMS);
			mcall = savedInstanceState.getBoolean(Common.CALL);
        	mnewsms = savedInstanceState.getBoolean(Common.NEWSMS);
        	mshownam = savedInstanceState.getBoolean(Common.BYNAM);
		}
}
