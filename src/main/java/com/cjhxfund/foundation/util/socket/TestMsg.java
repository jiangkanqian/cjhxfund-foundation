package com.cjhxfund.foundation.util.socket;

public class TestMsg implements ISocketMsg{

	@Override
	public void dealMsg(String msg) {
		System.out.println("Client:" + msg);
	}

	@Override
	public String getSendMsg() {
		return "";
	}

}
