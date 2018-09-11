 package com.cjhxfund.foundation.web.servlet;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cjhxfund.foundation.annotation.data.DocData;
import com.cjhxfund.foundation.annotation.data.ParamItem;
import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.util.data.DataUtil;
import com.cjhxfund.foundation.util.http.FileUploadUtil;
import com.cjhxfund.foundation.util.http.RequestMVCUtil;
import com.cjhxfund.foundation.util.http.RespDataUtil;
import com.cjhxfund.foundation.web.model.CodeTipMsg;
import com.cjhxfund.foundation.web.model.CtrlBean;
import com.cjhxfund.foundation.web.model.HttpCtrl;

/**
 * @author lanhan
 * @date 2015年11月22日
 */
@WebServlet(name="actionInvoker", urlPatterns="*.action") 
public class ActionInvoker extends HttpServlet {
 
	private static final long serialVersionUID = -730735902007038803L;

	private static JLogger logger = SysLogger.getLogger();

	

	@Override
    public void init() throws ServletException {
	    super.init();
//	    logger.info("init action invoker servlet……");
    }


	protected void service(HttpServletRequest request, HttpServletResponse response){

		HttpCtrl httpCtrl = new HttpCtrl(request, response);
		try {
			//如果配置了地址转发，则发送请求获取返回请求数据到页面
			if(request.getAttribute("requestType") != null){
				//如果是系统之间的API请求，则需要标识一下
				httpCtrl.setRequestType(request.getAttribute("requestType").toString());
			}
			String checkMsg = checkData(httpCtrl);// 验证是否为合理请求
			if (null != checkMsg) {
				// 返回错误消息
				httpCtrl.sendJson(CodeTipMsg.COMMON_FAIL, checkMsg);
			} else {
				// 调用接口，执行业务处理
				CtrlBean ctrlBean = httpCtrl.getCtrlBean();
				ctrlBean.invoke(httpCtrl);
			}
		}
		catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		finally{
			// 如果有返回值，则统一在这里发送
			if (DataUtil.isValidStr(httpCtrl.getResponseJson())) {
				RespDataUtil.respData(response, httpCtrl.getResponseJson());
			}
		}
	}


	/**
	 * 验证请求的数据是否符合目标注解要求
	 */
	public String checkData(HttpCtrl httpCtrl) {
		DocData checkdb = httpCtrl.getDocData();
		if (null == checkdb) {
			httpCtrl.getResponse().setStatus(404);
			return "invalid request url or method！ url:"+httpCtrl.getCheckKey();
		}

		//解析请求数据
		RequestMVCUtil.fillData(httpCtrl);
		//对于大数据量的字符串，就不要显示全部了
		String json = httpCtrl.getRequestJson();
		if(DataUtil.isValidStr(json) && json.length() > 2000){
			json = json.substring(0, 1000);
		}
		logger.info("request:"+checkdb.getApiName()+ "||" + httpCtrl.getRemoteIPAndPort()+ "||" + httpCtrl.getCheckKey() +"||"
		+  json);

		//获取待验证的请求参数信息
		List<ParamItem> paramList = checkdb.getParams();
		if (!DataUtil.isValidList(paramList)) {
			logger.info("no request params!");
			return null;
		}

		if (null!=checkdb.getSpecialApi()
				&&checkdb.getSpecialApi().equals("file")) {
			// 文件上传处理
			logger.info("The request is uploading file!");
			return FileUploadUtil.upload(this.getServletContext(), httpCtrl, paramList
					.get(0).getParamDesc());
		}

		if (httpCtrl.getRequestMethod().equalsIgnoreCase("delete")) {
			// delete 方式做特殊处理
			if (paramList.get(0).getParamType().equals("Array")
					&& !DataUtil.isValidStr(httpCtrl.getRequestJson())) {
				return "no data to delete!";
			}
		} else {
			String value = "";
			String result = null;
			for (ParamItem item : paramList) {
				value = "";
				if (item.getParamType().equals("Array")&&item.getField().startsWith("List")) {
					//如果是列表，并且有对象，则说明该请求数据就是一个列表集合
					value = httpCtrl.getRequestJson();
					// logger.info("value:"+value);
					// 如果接收的是数组，则只需验证数组是否有效即可
					if (!value.startsWith("[") || !value.endsWith("]") || value.length() < 3) {
						return "invalid array!";
					}
					else{
						//校验请求的Json数组
						result = httpCtrl.checkJsonArray();
					}
				} else if(!item.getParamType().equals("File")){
					value = httpCtrl.getAsString(item.getField());
					if (!DataUtil.isValidStr(value)) {
						// 如果是xml请求，数据会放到queryMapStr中
						value = httpCtrl.getQueryMapStr().get(item.getField());
					}
					result = RequestMVCUtil.checkVal(value,item);
				}
				if(DataUtil.isValidStr(result)){
					return result;
				}
			}

		}

		return null;
	}
	
	

}
