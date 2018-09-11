package com.cjhxfund.foundation.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.util.http.HTTP;
import com.cjhxfund.foundation.util.http.RequestUtil;


//@WebFilter(filterName = "webServiceFilter", urlPatterns = "*.ws")
public class WebServiceFilter  implements Filter {
	
	private static JLogger logger = SysLogger.getLogger();

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
//		HttpServletResponse response = (HttpServletResponse) servletResponse;
		logger.info("进入webservice请求！");
		String requestURI = request.getRequestURI();
		// String userAgent = request.getHeader("User-Agent");
		String remoteIp = HTTP.getIpAddr(request);
		System.out.println(requestURI);
		System.out.println(remoteIp);
		String requestBody = RequestUtil.getBodyString(request.getReader());
		System.out.println(requestBody);
		String contentType = request.getContentType();
		System.out.println(contentType);
		String accept = request.getHeader("Accept");
		System.out.println(accept);
//		String respJson = "{\"msg\":\"回话过期！请刷新页面重新登录!\",\"code\":400500}";
//		// response.sendRedirect(SpringContext.root);
//		logger.info(respJson);
//		RespDataUtil.respData(response, respJson);
//		Map<String, String> queryMapStr = new HashMap<String, String>();
//		String requestJson = RequestMVCUtil.parseXml(request, queryMapStr);
//		System.out.println(requestJson);
//		System.out.println(queryMapStr);
		chain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}
}
