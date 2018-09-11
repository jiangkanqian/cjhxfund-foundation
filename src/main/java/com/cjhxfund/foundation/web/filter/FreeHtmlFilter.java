package com.cjhxfund.foundation.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.web.context.SpringContext;
import com.cjhxfund.foundation.web.listener.SessionUser;

/**
 * 前后端分离后的页面过滤，只需验证是否登录即可
 * @author xiejiesheng
 *
 */
public class FreeHtmlFilter implements Filter{

	private static JLogger logger = SysLogger.getLogger(FreeHtmlFilter.class);

	@Override
	public void destroy() {
		logger.debug("正在销毁 FreeHtmlFilter...");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		if(SpringContext.requestModel.equals("free")){
			logger.info("免登陆模式访问HTML……");
			chain.doFilter(servletRequest, servletResponse);
			return;
		}
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String sessionId = request.getSession().getId();
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String requestURI = request.getRequestURI();
		if (SessionUser.containsKey(sessionId) || requestURI.contains("/login")
				|| requestURI.endsWith(SpringContext.root)|| requestURI.endsWith(SpringContext.root+"/")){
			chain.doFilter(servletRequest, servletResponse);
			return;
		}
		// 如果没有登录，则直接返回登录页面
		response.sendRedirect(SpringContext.root);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		logger.info("init free html filter……");
	}

	
	
}
