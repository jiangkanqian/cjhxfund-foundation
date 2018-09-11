package com.cjhxfund.foundation.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.cjhxfund.foundation.log.constants.StartLogger;
import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;


/**
 * css和js的文件过滤，在自己编写的js和css文件尽可能的不缓存，在测试的时候
 * @author xiejiesheng
 *
 */
public class StaticFilter implements Filter{

	private static JLogger logger = SysLogger.getLogger();

	@Override
	public void destroy() {
		logger.info("正在销毁 StaticFilter...");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
//		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
//		String requestURI = request.getRequestURI();
//		System.out.println(requestURI);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		//response.setContentType("text/html;charset=UTF-8");
		chain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		StartLogger.getLogger().info("init StaticFilter filter……");
	}
}
