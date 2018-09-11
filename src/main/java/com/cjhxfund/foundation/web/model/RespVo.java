package com.cjhxfund.foundation.web.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiejs
 * @date 2015年8月2日
 */
public class RespVo {
	
	private int code = 1;
	private String msg = "OK";
	private String status;
	private Object data;
	
	
	
	public RespVo(Object data) {
		setData(data);
	}
	
	public RespVo(String msg) {
		this.msg = msg;
	}
	
	public RespVo(String msg, Object data) {
		this.msg = msg;
		setData(data);
	}

	public RespVo(String msg, Object data, int code) {
		this.msg = msg;
		this.code = code;
		setData(data);
	}

	public RespVo(String status, String msg, Object data, int code) {
		this.status = status;
		this.msg = msg;
		this.code = code;
		setData(data);
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		if(data != null){
			if(data instanceof List){
				if(((List<?>) data).size() == 0){
					this.msg = "Empty Data!";
					this.code = 0;
				}
				else{
					this.data = data;
				}
			}
			else{
				if(data instanceof Map && ((Map<?, ?>) data).size() == 0){
					this.msg = "Empty Data!";
					this.code = 0;
				}
				else{
					List<Object> list = new ArrayList<Object>();
					list.add(data);
					this.data = list;
				}
			}
		}
		else{
			this.msg = "the data of return is null!";
			this.code = 500;
		}
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	

}
