package com.cjhxfund.foundation.web.service;

import com.cjhxfund.foundation.log.constants.DataLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.web.model.CodeTipMsg;
import com.cjhxfund.foundation.web.model.HttpCtrl;

/**
 * 初始化数据 基础类，凡是继承该类的都会在项目启动时初始化加载相关数据
 * 之所以没有弄成接口，是因为定义为接口时，没办法加载自定义的注解
 * @author Jason
 * @date 2015年12月16日
 */
public class BaseInit {

	protected static JLogger logger = DataLogger.getLogger();
	
	
	protected int delaySecs;
	
	
	public int getDelaySecs() {
		return delaySecs;
	}

	public void setDelaySecs(int delaySecs) {
		this.delaySecs = delaySecs;
	}

	/**
	 *  初始化定时任务参数
	 *  delaySecs 延迟几秒执行
	 */
	protected void initParams() {
		this.delaySecs = 0;
	}
	
	public void execInit() {
		initParams();
		if(delaySecs == 0){
			initData();
		}
		else{
			//延迟时间执行
			new DelayTaskThread().start();
		}
	}
	
	class DelayTaskThread extends Thread{
		@Override
		public void run() {
			long value = delaySecs*1000L;
			try {
				Thread.sleep(value);
				initData();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.info("延迟初始化执行完成……");
		}
	}

	/**
	 * 如果是手动执行，可以自定义返回提示消息，默认为："初始化成功！"
	 * @return
	 */
	protected String getTipMsg(){
		return "初始化成功！";
	}
	
	/**
	 * 手动执行时调用，默认会调用系统启动时的调用
	 * @param httpCtrl
	 */
	public void initData(HttpCtrl httpCtrl) {
		initData();
		httpCtrl.sendJson(CodeTipMsg.COMMON_SUCCESS, getTipMsg());
	}
	
	/**
	 * 系统启动时自动调用
	 */
	public void initData() {
		
	}
}
