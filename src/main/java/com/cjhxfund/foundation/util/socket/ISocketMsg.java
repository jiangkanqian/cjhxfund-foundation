package com.cjhxfund.foundation.util.socket;
/**
 * 处理socket消息请求，必须要实现该处理方法
 * @author xiejiesheng
 *
 */
public interface ISocketMsg {

	public void dealMsg(String msg);
	
	public String getSendMsg();
}
