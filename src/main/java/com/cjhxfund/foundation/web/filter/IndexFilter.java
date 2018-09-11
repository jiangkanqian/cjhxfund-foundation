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
import javax.servlet.http.HttpSession;

import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.util.data.DataUtil;
import com.cjhxfund.foundation.util.http.CJHXResponseWrapper;
import com.cjhxfund.foundation.util.io.ReadFileUtil;
import com.cjhxfund.foundation.web.context.SpringContext;
import com.cjhxfund.foundation.web.listener.SessionUser;
import com.cjhxfund.foundation.web.model.sys.UserInfo;

public class IndexFilter implements Filter{

	private static JLogger logger = SysLogger.getLogger();

	@Override
	public void destroy() {
		logger.debug("正在销毁 IndexFilter...");
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
		if (requestURI.contains("/login")
				|| requestURI.endsWith(SpringContext.root)|| 
				requestURI.endsWith(SpringContext.root+"/")){
			chain.doFilter(servletRequest, servletResponse);
			return;
		}
		
		if(SessionUser.containsKey(sessionId)){
			HttpSession session = SessionUser.setLastAccesstime(sessionId);
			// 判断用户是否具有该权限
			UserInfo user = (UserInfo) session.getAttribute(SessionUser.USER_INFO);
			if (user.getRole() == null || user.getRole().getRoleType() == null) {
				// 如果没有用户角色，则可能是超级管理员
				chain.doFilter(servletRequest, servletResponse);
				return;
			}
//			String roleNo = user.getRole().getRoleNo();
//			String roleNo = user.getRole().getRoleNo();
			if(requestURI.endsWith("index.html")){
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("Pragma", "no-cache");
				response.setDateHeader("Expires", 0);
				response.setContentType("text/html;charset=UTF-8");
				CJHXResponseWrapper responseWrapper = new CJHXResponseWrapper(response);
				chain.doFilter(request, responseWrapper);
				response.setStatus(200);//刷新页面，不要304
				String content = ReadFileUtil.getConfigFile("html-template/"+user.getRole().getRoleNo()+".html", "UTF-8");
				if (DataUtil.isValidStr(content)) {
					response.getWriter().write(content);
				}
			}
			else{
				chain.doFilter(servletRequest, servletResponse);
			}
			return;
		}
		
		// 如果没有登录，则直接返回登录页面
		response.sendRedirect(SpringContext.root+"/");
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		logger.info("init index html filter……");
	}
}
