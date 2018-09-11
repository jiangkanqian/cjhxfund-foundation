package com.cjhxfund.foundation.web.model;

public class AccessToken {

	private Integer accessAll = 1;
	private String ip;
	private String token;
	private Long time;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public Integer getAccessAll() {
		return accessAll;
	}
	public void setAccessAll(Integer accessAll) {
		this.accessAll = accessAll;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
}
