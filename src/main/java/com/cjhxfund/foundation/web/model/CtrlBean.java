package com.cjhxfund.foundation.web.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.cjhxfund.foundation.annotation.data.DocData;
import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.web.context.SpringContext;
import com.cjhxfund.foundation.web.controller.BaseController;

/**
 * 缓存请求方法和请求的controller，加快处理速度
 * @author xiejiesheng
 * 20170328
 */
public class CtrlBean {

	private static JLogger logger = SysLogger.getLogger();
	
	private String methodName;
	private Method method;
	private BaseController controllerBean;
	private BeanInfo beanInfo;
	private DocData doc;
	private APIMonitor monitor;
//	private BaseController cls;
//	private CtMethod cmd;
	

	public void invoke(HttpCtrl httpCtrl){
		if(monitor.getStatus() == 0){
			httpCtrl.sendJson(400,"'"+httpCtrl.getCheckKey() + "' API is stopped!");
			return;
		}
		monitor.addTimes();
		monitor.setLastReqIP(httpCtrl.getRemoteIP());
		long startTime = System.currentTimeMillis();
		monitor.setLastReqTime(startTime);
		//两种方式调用：
		//1.通过生成的字节码调用，性能优于反射
		if(!SpringContext.isDebug){
			controllerBean.action(httpCtrl, methodName);
		}
		else{
			//2.通过缓存的bean和method，反射调用，第一版本是这样滴，性能也不差，肯定每一种好哈
			try {
				method.invoke(controllerBean, httpCtrl);
				monitor.setRespTime(System.currentTimeMillis() - startTime);
			}
			catch (IllegalAccessException e) {
				logger.error(e);
				e.printStackTrace();
				httpCtrl.sendJson(500,"invoker error!");
			} catch (InvocationTargetException e) {
				logger.error(e);
				e.printStackTrace();
				httpCtrl.sendJson(500,"invoker error!");
			}
		}
	}
	

	public CtrlBean(DocData doc, BeanInfo info, Method method) {
		this.doc = doc;
		this.method = method;
		this.methodName = method.getName();
		this.beanInfo = info; //new BeanInfo(controllerBean, beanName);
		this.controllerBean = (BaseController)info.getBean();
	}
	
	public BaseController getControllerBean() {
		return controllerBean;
	}
	
	public void setControllerBean(Object controllerBean) {
		this.controllerBean = (BaseController)controllerBean;
	}
	
	public void setControllerBean() {
		this.controllerBean = (BaseController)beanInfo.getBean();
	}


	public void setDoc(DocData doc) {
		this.doc = doc;
	}

	public DocData getDoc() {
		return doc;
	}


	public APIMonitor getMonitor() {
		return monitor;
	}

	public void setMonitor(APIMonitor monitor) {
		this.monitor = monitor;
	}

	public BeanInfo getBeanInfo() {
		return beanInfo;
	}

	
	public void setBeanInfo(BeanInfo beanInfo) {
		this.beanInfo = beanInfo;
	}



	



	
	
	
	
}
