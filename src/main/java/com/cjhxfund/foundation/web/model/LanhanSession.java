package com.cjhxfund.foundation.web.model;

import java.util.HashMap;
import java.util.Map;

import com.cjhxfund.foundation.web.model.sys.UserInfo;

public class LanhanSession {

	private String id;// 访问主键，浏览器或者客户端的token
	private String webToken;// 访问主键，浏览器或者客户端的token
	private String userId;// 访问主键，用户主键
	private long createtime;// 创建时间，存放时间戳
	private long lastAccesstime;// 最后访问时间,存放时间戳
	private long maxInterval;// 最大非活跃时间，比如半个小时没有访问就去除
	private UserInfo user;
	private boolean isNew;

	private Map<String, Object> attribute = new HashMap<String, Object>();// 存放变量属性

	
	public LanhanSession(String id) {
		this.id = id;
		this.createtime = System.currentTimeMillis();
		this.lastAccesstime = System.currentTimeMillis();
		this.isNew = true;
	}
	
	public LanhanSession(String id,  UserInfo user) {
		this.id = id;
		this.createtime = System.currentTimeMillis();
		this.lastAccesstime = System.currentTimeMillis();
		this.user = user;
		this.userId = user.getUserId().toString();
		this.isNew = true;
	}

	public String getWebToken() {
		return webToken;
	}

	public void setWebToken(String webToken) {
		this.webToken = webToken;
	}

	/**
	 * 设置session过期时间，多少小时，多少分钟多少秒
	 * @param hour 小时
	 * @param minute 分钟
	 * @param secs 秒
	 */
	public void setMaxInterval(int hour, int minute, int secs){
		maxInterval = secs*1000 + minute * 1000 * 60 + hour*1000*60*60;
	}
	
	/**
	 * 如果最后访问时间 加上最大间隔时间 大于当前时间，则说明过期了；比如当前是11点，最后访问时间是10:30，如果设置的最大间隔时间为30分钟以内，则应该过期了，否则就是没过期；
	 */
	public synchronized boolean isInvalid(){
		long now = System.currentTimeMillis();
		return now >= lastAccesstime + maxInterval;
	}
	
	

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

	public long getLastAccesstime() {
		return lastAccesstime;
	}

	public void setLastAccesstime(long lastAccesstime) {
		this.lastAccesstime = lastAccesstime;
	}

	public Map<String, Object> getAttribute() {
		return attribute;
	}

	public void setAttribute(Map<String, Object> attribute) {
		this.attribute = attribute;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	@Override
	public String toString() {
		return "LanhanSession [id=" + id + ", userId=" + userId + ", createtime=" + createtime + ", lastAccesstime=" + lastAccesstime + ", maxInterval=" + maxInterval + ", user=" + user + ", isNew=" + isNew + "]";
	}
	
	
	
	


}
