package com.cjhxfund.foundation.util.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.util.data.DataUtil;
import com.cjhxfund.foundation.util.str.JsonUtil;
import com.cjhxfund.foundation.web.model.CodeTipMsg;

public class RespDataUtil {

	private static JLogger logger = SysLogger.getLogger();

	private static RespDataUtil pu;

	private RespDataUtil() {

	}

	public static RespDataUtil getInstacne() {
		if (pu == null) {
			pu = new RespDataUtil();
			return pu;
		}
		return pu;
	}

	

	/**
	 *  将数据封装成json发送到前段页面 obj
	 * @author xiejs
	 * @date 2015年5月13日
	 * @param obj 数据对象
	 * @param resp
	 * @param total 选填，为 0 时发送json对象数据，其他则发送datagrid数据格式
	 */
	public static void sendJson(Object obj, HttpServletResponse resp, int total) {
		String json = null;
		// total为0，就意味着只是返回提示信息，如果不为0，则返回相关数据信息
		if (total == 0) {
			json = JsonUtil.toJson(obj);
		} 
		else {
			json = "{\"total\":" + total + " , \"rows\":" + JsonUtil.toJson(obj) + "}";
		}
//		System.out.println(json);
		respData(resp,json);
	}
	
	/**
	 * 直接发送json字符串
	 * @author xiejs
	 * @date 2015年6月25日
	 * @param resp
	 * @param data String json 
	 */
	public static void respData(HttpServletResponse resp,String data){
//		logger.info("response data：" + data);
//		if (data.length() > 500) {
//			logger.info("response：" + data.subSequence(0, 498) + "……");
//		} else {
//			logger.info("response：" + data);
//		}
		resp.setContentType("application/json;charset=utf-8");
		PrintWriter out = null;
		try {
			out = resp.getWriter();
		} catch (IOException e) {
			logger.error("数据发送失败!");
			e.printStackTrace();
		} finally {
			out.println(data);
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 发送页面提示信息
	 * @author xiejs
	 * @date 2015年7月22日
	 * @param status 状态：Y：操作成功，N：操作失败
	 * @param mes 提示语
	 * @param resp
	 */
	public static void sendTip(String status,String msg,HttpServletResponse resp) {
		String respJson="{\"msg\":\""+msg+"\",\"status\":\""+status+"\"}";
		respData(resp,respJson);
	}
	
	/**
	 * 发送页面提示信息
	 * @author xiejs
	 * @date 2015年7月22日
	 * @param status 状态：Y：操作成功，N：操作失败
	 * @param mes 提示语
	 * @param result 其他结果
	 * @param resp
	 */
	public static void sendTip(String code,String msg,String result,HttpServletResponse resp) {
		String respJson= "{\"result\":"+result+",\"msg\":\""+msg+"\",\"code\":\""+code+"\"}";
		respData(resp,respJson);
	}
	
	
	public static void sendTip(CodeTipMsg tip,String msg,HttpServletResponse resp) {
		respData(resp,getRespJson(tip,msg));
	}
	
	
	public static String getRespJson(String status,String msg){
		return  "{\"msg\":\""+msg+"\",\"status\":\""+status+"\"}";
	}
	
	public static String getRespJson(String code,String msg,String result){
		return  "{\"result\":"+result+",\"msg\":\""+msg+"\",\"code\":\""+code+"\"}";
	}
	
	public static String getRespJson(CodeTipMsg tip,String msg){
		if(!DataUtil.isValidStr(msg)){
			msg=tip.getMsg();
		}
		return  "{\"code\":"+tip.getCode()+",\"msg\":\""+msg+"\"}";
	}
	
	public static String getRespJson(Object obj, int total) {
		return  "{\"total\":" + total + " , \"rows\":" + JsonUtil.toJson(obj) + "}";
	}
	
	public static String getRespJson(Object obj) {
		return JsonUtil.toJson(obj);
	}
	
	
	public static void respXmlData(HttpServletResponse resp,String data){
		if (data.length() > 100) {
			logger.info("ajax xml data：" + data.subSequence(0, 98) + "……");
		} else {

			logger.info("ajax xml data：" + data + "……");
		}
		resp.setContentType("text/xml");
		resp.setHeader("encoding", "UTF-8");
		PrintWriter out = null;
		try {
			out = resp.getWriter();
		} catch (IOException e) {
			logger.error("数据发送失败!");
			e.printStackTrace();
		} finally {
			out.println(data);
			out.flush();
			out.close();
		}
	}
	
	public static void respData(HttpServletResponse resp,String data,String contentType){
//		if (data.length() > 100) {
//			logger.info("ajax data：" + data.subSequence(0, 98) + "……");
//		} else {
//			logger.info("ajax data：" + data + "……");
//		}
		resp.setContentType(contentType);
		resp.setHeader("encoding", "UTF-8");
		PrintWriter out = null;
		try {
			out = resp.getWriter();
		} catch (IOException e) {
			logger.error("数据发送失败!");
			e.printStackTrace();
		} finally {
			out.println(data);
			out.flush();
			out.close();
		}
	}
	
	


	

}
