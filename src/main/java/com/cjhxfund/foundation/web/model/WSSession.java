package com.cjhxfund.foundation.web.model;

import java.io.IOException;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import com.cjhxfund.foundation.util.str.DateUtil;

public class WSSession {

	private String userName;
	private long accessTime;
	private Session session;
	
	
	
	public WSSession(String userName, Session session) {
		this.userName = userName;
		this.accessTime = System.currentTimeMillis();
		this.session = session;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public long getAccessTime() {
		return accessTime;
	}
	public void setAccessTime(long accessTime) {
		this.accessTime = accessTime;
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	
	public RemoteEndpoint.Basic getBasicRemote(){
		return session.getBasicRemote();
	}
	
	public synchronized void sendText(String msg) throws IOException{
		if(session.isOpen()){
			getBasicRemote().sendText(msg);
		}
	}
	
	public boolean isOpen(){
		return session.isOpen();
	}

	@Override
	public String toString() {
		return "{\"userName\":\"" + userName + "\",\"accessTime\":" + DateUtil.getDateStr(accessTime) +  "\"isOpen\":"+session.isOpen()+"}";
	}
	
	
	
}
