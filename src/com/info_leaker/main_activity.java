package com.info_leaker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class main_activity extends Activity {
	//implements View.OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.info);
        ((Button)findViewById(R.id.info_done)).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
	        	main_activity.this.finish();
			}
        });
    }

}