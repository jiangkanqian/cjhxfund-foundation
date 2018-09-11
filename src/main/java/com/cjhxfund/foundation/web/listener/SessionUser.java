package com.cjhxfund.foundation.web.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.web.context.SpringContext;
import com.cjhxfund.foundation.web.model.BaseModel;
import com.cjhxfund.foundation.web.model.sys.UserInfo;
import com.cjhxfund.foundation.web.service.IBaseSV;

/**
 * @author xiejs
 * @date 2015年10月15日
 */
@WebListener()
public class SessionUser implements HttpSessionListener{
	
	private static JLogger logger = SysLogger.getLogger(SessionUser.class);
	
	// key为sessionId，value为HttpSession，使用static，定义静态变量，使之程序运行时，一直存在内存中。
		private final static Map<String, HttpSession> sessionMap = new ConcurrentHashMap<String, HttpSession>();
		
		public final static String USER_INFO = "userInfo";
		public  static int sessionTime = 60*60;//session过期时间 60分钟

		/**
		 * HttpSessionListener中的方法，在创建session
		 */
		public void sessionCreated(HttpSessionEvent event) {
			logger.info("created session id:"+event.getSession().getId());
		}

		/**
		 * HttpSessionListener中的方法，回收session时,删除sessionMap中对应的session
		 */
		public void sessionDestroyed(HttpSessionEvent event) {
			String sessionId = event.getSession().getId();
			UserInfo user =getUserInfo(sessionId);
			if(null != user){
				logger.info("destroyed session id:"+ sessionId + ",userName:"+user.getUserName());
			}
			getSessionMap().remove(sessionId);
			Object bean = SpringContext.getSpringBean("loginRecordSV");
			if(null != bean){
				@SuppressWarnings("unchecked")
				IBaseSV<BaseModel> b = (IBaseSV<BaseModel>) bean;
				Map<String, Object> params = new HashMap<String,Object>();
				params.put("sessionId", sessionId);
				b.callBack(params);
			}
		}

		/**
		 * 得到在线用户会话集合
		 */
		public static List<UserInfo> getSessionUsers() {
			List<UserInfo> list = new ArrayList<UserInfo>();
			for(Entry<String, HttpSession> entry : sessionMap.entrySet()){
				HttpSession session = entry.getValue();
				UserInfo user = (UserInfo) session.getAttribute(USER_INFO);
				if (user != null) {
					list.add(user);
				}
			}
			return list;
		}
		
		/**
		 * 根据userId获取SessionUser
		 */
		public static UserInfo getUserInfo(Long userId) {
			for(Entry<String, HttpSession> entry : sessionMap.entrySet()){
				HttpSession session = entry.getValue();
				UserInfo user = (UserInfo) session.getAttribute(USER_INFO);
				if (user != null && user.getUserId().equals(userId)) {
					return user;
				}
			}
			return null;
		}
		
		/**
		 * 得到在线用户会话集合
		 */
		public static List<HttpSession> getUserSessions() {
			List<HttpSession> list = new ArrayList<HttpSession>();
			for(Entry<String, HttpSession> entry : sessionMap.entrySet()){
				HttpSession session = entry.getValue();
				if (session != null ) {
					list.add(session);
				}
			}
			return list;
		}

		/**
		 * 得到用户对应会话的userInfo
		 */
		public static UserInfo getUserInfo(HttpSession session) {
			return getUserInfo(session.getId());
		}
		/**
		 * 得到用户对应会话的userInfo，key为用户ID,value为会话ID
		 */
		public static UserInfo getUserInfo(String sessionId) {
			HttpSession session = getSessionMap().get(sessionId);
			if(session == null){
				return null;
			}
			UserInfo user = (UserInfo) session.getAttribute(USER_INFO);
			if (user != null) {
				return user;
			}
			return null;
		}
		
		/**
		 * 得到用户对应会话的sessionName
		 */
		public static Object getSessionAttr(HttpSession session,String USER_INFO) {
			return getSessionAttr(session.getId(), USER_INFO);
		}
		/**
		 * 得到用户对应会话的sessionName
		 */
		public static Object getSessionAttr(String sessionId, String USER_INFO) {
			HttpSession session = getSessionMap().get(sessionId);
			if(session == null){
				return null;
			}
			return session.getAttribute(USER_INFO);
		}
		
		/**
		 * 得到用户对应会话map，key为用户ID,value为会话ID
		 */
		public static Map<Long, String> getUserSessionMap() {
			Map<Long, String> map = new HashMap<Long, String>();
			for(Entry<String, HttpSession> entry : sessionMap.entrySet()){
				HttpSession session = entry.getValue();
				UserInfo user = (UserInfo) session.getAttribute(USER_INFO);
				if (user != null) {
					map.put(user.getUserId(), entry.getKey());
				}
			}
			return map;
		}

		

		/**
		 * 增加用户到session集合中
		 */
		public static void addUser(HttpSession session,Object user,String USER_INFO) {
			session.setAttribute(USER_INFO, user);
			getSessionMap().put(session.getId(), session);
		}
		
		/**
		 * 增加用户到session集合中
		 */
		public static void addUser(HttpSession session, UserInfo user) {
			session.setAttribute(USER_INFO, user);
			getSessionMap().put(session.getId(), session);
			user.setLoginTime(System.currentTimeMillis());
			logger.info("添加Session, userName:"+user.getUserName());
		}
		
		/**
		 * 增加用户到session集合中
		 */
		public static void addUser(HttpSession session) {
			getSessionMap().put(session.getId(), session);
		}

		/**
		 * 移除用户Session
		 */
		public synchronized static void removeSession(Long userId) {
			for(Entry<String, HttpSession> entry : sessionMap.entrySet()){
				HttpSession session = entry.getValue();
				UserInfo user = (UserInfo) session.getAttribute(USER_INFO);
				if (user != null && user.getUserId().equals(userId)) {
					String sessionId = session.getId();
					logger.info("主动退出……userName:"+user.getUserName());
					getSessionMap().get(sessionId).invalidate();
					getSessionMap().remove(sessionId);
					return;
				}
			}
		}
		
		/**
		 * 移除一个session
		 */
		public static void removeSession(String sessionId) {
			if(containsKey(sessionId)){
				UserInfo user = getUserInfo(sessionId);
				logger.info("主动退出……userName:"+user.getUserName());
				getSessionMap().get(sessionId).invalidate();
				getSessionMap().remove(sessionId);
			}
		}

		public static boolean containsKey(String key) {
			return getSessionMap().containsKey(key);
		}

		/**
		 * 判断该用户是否已重复登录，使用
		 * 同步方法，只允许一个线程进入，才好验证是否重复登录
		 * @param user
		 * @return
		 */
		public synchronized static UserInfo checkIfHasLogin(Long userId) {
			for(Entry<String, HttpSession> entry : sessionMap.entrySet()){
				HttpSession session = entry.getValue();
				UserInfo user = (UserInfo) session.getAttribute(USER_INFO);
				if (user != null && user.getUserId().equals(userId)) {
					return user;
				}
			}
			return null;
		}

		/**
		 * 获取在线的sessionMap
		 */
		public static Map<String, HttpSession> getSessionMap() {
			return sessionMap;
		}

		public static HttpSession setLastAccesstime(String sessionId) {
			HttpSession session = sessionMap.get(sessionId);
			session.setMaxInactiveInterval(SessionUser.sessionTime);
			return session;
		}

//		/**
//		 * 获取在线sessionMap中的SessionId
//		 */
//		public static Iterator<String> getSessionMapKeySetIt() {
//			return getSessionMap().keySet().iterator();
//		}
}
