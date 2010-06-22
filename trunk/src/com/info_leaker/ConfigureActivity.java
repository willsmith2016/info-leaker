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
        private CheckBox mContact, mCall, mNewCall;
        private RadioButton mNam, mNum;

        private boolean mcontact = true;		// show contacts
        private boolean mcall = true;		// show call log
        private boolean mnewcall = true;	// show new call
        private boolean mshownam;	// show contact by name or number
        int mAppWidgetId;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //setResult(RESULT_CANCELED);
            setContentView(R.layout.configure);
            mCancel = (Button)findViewById(R.id.conf_cancel);
            mSave = (Button)findViewById(R.id.conf_save);
            mContact = (CheckBox)findViewById(R.id.conf_contact);
            mCall = (CheckBox)findViewById(R.id.conf_calllog);
            mNewCall = (CheckBox)findViewById(R.id.conf_remcall);
            mNam = (RadioButton)findViewById(R.id.conf_byname);
            mNum = (RadioButton)findViewById(R.id.conf_bynum);

            Intent intent = this.getIntent();
            Bundle bundle = intent.getBundleExtra(Common.CONF);
            // If restoring, read from bundle
            if (bundle != null) {
            	this.mcontact = bundle.getBoolean(Common.CONTACT);
            	this.mcall = bundle.getBoolean(Common.CALL);
            	this.mnewcall = bundle.getBoolean(Common.NEWCALL);
            	this.mshownam = bundle.getBoolean(Common.BYNAM);
            	
            	this.mContact.setChecked(mcontact);
            	this.mCall.setChecked(mcall);
            	this.mNewCall.setChecked(mnewcall);
            	if(mshownam)
            		this.mNam.setSelected(true);
            	else
            		this.mNum.setSelected(true);
            	//mAppWidgetId = bundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, 
                //		AppWidgetManager.INVALID_APPWIDGET_ID);
            }else{
            	this.mContact.setChecked(true);
            	this.mCall.setChecked(true);
            	this.mNewCall.setChecked(true);
            }
            mCancel.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					setConfigureResult(RESULT_CANCELED);
				}            	
            });
            mSave.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					setConfigureResult(RESULT_OK);
				}
            });
            mContact.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					mcontact = isChecked;
				}
            });
            mCall.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					mcall = isChecked;
				}
            });
            mNewCall.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					mnewcall = isChecked;
				}
            });

        }
        // return result
        public void setConfigureResult(int resultCode) {
        	Context context = getBaseContext();
	    	Intent intent = new Intent(context, WidgetService.class);
        	if(resultCode == RESULT_OK){
	            Bundle bundle = new Bundle();
	            bundle.putBoolean(Common.CONTACT, mcontact);
	            bundle.putBoolean(Common.CALL, mcall);
	            bundle.putBoolean(Common.NEWCALL, mnewcall);
	            bundle.putBoolean(Common.BYNAM, mshownam);
	            intent.putExtra(Common.CONF, bundle);
	    	}
        	// back to call service
        	context.startService(intent);
            setResult(resultCode, intent);
			ConfigureActivity.this.finish();
       }

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
			savedInstanceState.putBoolean(Common.SMS, mcontact);
			savedInstanceState.putBoolean(Common.CALL, mcall);
			savedInstanceState.putBoolean(Common.NEWSMS, mnewcall);
			savedInstanceState.putBoolean(Common.BYNAM, mshownam);
			super.onSaveInstanceState(savedInstanceState); 
		}
		
		@Override 
		public void onRestoreInstanceState(Bundle savedInstanceState) { 
			super.onRestoreInstanceState(savedInstanceState); 
			mcontact = savedInstanceState.getBoolean(Common.SMS);
			mcall = savedInstanceState.getBoolean(Common.CALL);
        	mnewcall = savedInstanceState.getBoolean(Common.NEWSMS);
        	mshownam = savedInstanceState.getBoolean(Common.BYNAM);
		}
}
