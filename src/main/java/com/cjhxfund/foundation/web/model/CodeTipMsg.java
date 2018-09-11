package com.cjhxfund.foundation.web.model;

import org.apache.commons.lang3.StringUtils;


/**
 * 提示码管理
 * 正常为偶数，错误 为奇数,0-100为系统异常
 * @author xiejs
 * @date 2015年6月18日
 */
public enum CodeTipMsg {

	/**0,"O,No!"*/
	COMMON_FAIL(0,"O,No!"),
	
	/**1,"ok"*/
	COMMON_SUCCESS(1,"ok"),
	
	/**40001,"找不到session，无法获取菜单！"*/
	LOAD_SESSION_FAIL(40001,"no session found!"),
	
	
	/**41001,"invalid param!" 不合法的参数值*/
	CHECK_FAIL(41001,"invalid param!"),
	
	/**41000,"lack required param!" 缺少必填字段*/
	REQUIRED_FAIL(41000,"lack required param!"),
	
	/**41002,"批量发送数据太大!"*/
	DATA_FAIL(41002,"request data too big!"),
	
	/**41003,"批量发送数据太大!"*/
	REQUESTURL_FAIL(41003,"invalid request url or method！"),
	
	/** 40002,"添加失败！" */
	ADD_FAIl(40002,"fail to add!"),
	
	/** 40003,"修改失败！" */
	UPDATE_FAIl(40003,"fail to update!"),
	
	/** 40004,"删除失败！" */
	DELETE_FAIl(40004,"fail to delete!"),
	
	/** 41004,"验证码输入不正确" */
	IMGCODE_FAIL(42004,"check code error!"),
	/** 40008,"删除失败！" */
	SYNC_FAIL(40005,"synchronization to delete!"),
	
	/** 41005,"用户名或密码错误，登陆失败！" */
	LOGIN_FAIL(42005,"fail to login, wrong username or password!"),
	
	/**42006，多点登录！*/
	LOGIN_FAIL2(42006,"fail to login, your account is logined!");
	
	
	
	
	private int code;
	private String msg;
	
	private CodeTipMsg(int code,String msg){
		this.code=code;
		this.msg=msg;
	}
	
	public int getCode(){
		return this.code;
	}
	
	public String getMsg(){
		return this.msg;
	}
	
	
	/**返回的提示信息，可以做出相应的修改*/
	public void setMsg(String msg){
		this.msg=msg;
	}
	
	/**拼接成json格式：{"status":"Y","msg":"123"}*/
	public String toString(){
		return "{\"code\":"+this.code+",\"msg\":\""+this.msg+"\"}";
	}
	
	public String toString(String msg){
		if(StringUtils.isEmpty(msg)){
			return toString();
		}
		return "{\"code\":"+this.code+",\"msg\":\""+msg+"\"}";
	}
	
	
	
}
