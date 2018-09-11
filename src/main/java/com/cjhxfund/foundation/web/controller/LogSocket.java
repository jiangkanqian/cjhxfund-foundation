package com.cjhxfund.foundation.web.controller;

import java.io.IOException;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.util.data.DataUtil;
import com.cjhxfund.foundation.web.context.CommonCache;
import com.cjhxfund.foundation.web.model.WSSession;

/**
 * 订阅实时行情，rq: real-time quotes
 * @author xiejiesheng
 *
 */
@ServerEndpoint("/websocket/log.ws")
public class LogSocket  {

	private static JLogger logger = SysLogger.getLogger();
//	private static final String GUEST_PREFIX = "user";
	
	// 定义一个成员变量，记录WebSocket客户端的聊天昵称
	private  String nickname;
	// 定义一个成员变量，记录与WebSocket之间的会话
//	private Session session;


	@OnOpen
	public void open(Session session, EndpointConfig config) {
		// this.session = session;
		try {
//			HttpSession httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
			// 将WebSocket客户端会话添加到集合中
//			CommonCache.clientSet.add(this);
			List<String> indexIds = session.getRequestParameterMap().get("userName");
			String userName = "userName";
			if(DataUtil.isValidList(indexIds)){
				userName = indexIds.get(0);
			}
			this.nickname = userName;
			WSSession ws = new WSSession(userName, session);
			CommonCache.sessionMap.put(session.getId(), ws);
			String message = String.format("【%s %s】", nickname, "进入页面");
			logger.info(message);
		} catch (Exception e) {
			try {
				session.close();
			} catch (IOException e1) {
			}
		}
	}

	/**
	 * 出现错误时处理消息
	 * @param session
	 */
	@OnError
	public void error(Session session, Throwable t) {
		// this.session = session;
		try {
			logger.error(t);
		} catch (Exception e) {
			try {
				session.close();
			} catch (IOException e1) {
			}
		}
	}

	/**
	 * 接收信息时执行
	 * @param session
	 * @param msg
	 *            字符串信息
	 * @param last
	 */
	@OnMessage
	public void echoTextMessage(Session session, String msg, boolean last) {
		// 发送消息
		// 只做接收数据使用，不接收前端发过来的消息
		//logger.debug(nickname +":" +msg);
//		System.out.println(session.getContainer().getDefaultMaxSessionIdleTimeout());
		sendText(session, "收到消息："+msg);
	}

	@OnClose
	public void close(Session session, CloseReason cr) {
		try {
			CommonCache.sessionMap.remove(session.getId());
			String message = String.format("【%s %s】", nickname, "离开页面");
			logger.info(message);
			logger.info(cr.getReasonPhrase());
			// 发送消息
		} catch (Exception e) {
		}
	}

	/*
	 * 发送消息
	 */
	public synchronized void sendText(Session session, String data) {
		try {
			if (session.isOpen()) {
				session.getBasicRemote().sendText(data);
			}
		} catch (IOException e) {
			try {
				session.close();
			} catch (IOException e1) {
			}
		}
	}


}
