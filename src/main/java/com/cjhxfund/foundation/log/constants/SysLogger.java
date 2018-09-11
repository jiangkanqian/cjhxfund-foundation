package com.cjhxfund.foundation.log.constants;

import com.cjhxfund.foundation.log.strategy.JLoggerFactory;
import com.cjhxfund.foundation.log.utils.CommUtil;
import com.cjhxfund.foundation.web.context.SpringContext;

/**
 * 系统日志，记录系统操作日志, 如果有其他日志，则可以以同样的方式新建一个类，做专门的处理
 * @author xiejiesheng
 */
public class SysLogger extends JLoggerFactory{
	
	public SysLogger() {}
	
	public SysLogger(Class<?> clazz) {
		setClassName(clazz.getSimpleName());
//		logger.debug(className);
	}
	
	public static SysLogger getLogger(Class<?> clazz) {
		return new SysLogger(clazz);
	}
	
	public static SysLogger getLogger() {
		return new SysLogger();
	}

	/**
	 * 写日志
	 * @param logFileName 日志文件名
	 * @param level 日志级别
	 * @param logMsg 日志内容
	 */
	@Override
	protected void writeLog(String logFileName, int level, String logMsg) {
		
		StringBuffer sb = getStringBuffer(level, logMsg);
		FLogger.logManager.addLog(SysLogger.class.getSimpleName()+"-"+logFileName, sb);
		// 错误信息强制打印到控制台；若 CONSOLE_PRINT 配置为 true，也将日志打印到控制台
		// if (Constant.ERROR == level || Constant.FATAL == level || Constant.CONSOLE_PRINT) {
		if (SpringContext.isDebug) {
			try {
				System.out.print(new String(sb.toString().getBytes(Constant.CFG_CHARSET_NAME), Constant.CFG_CHARSET_NAME));
			} catch (Exception e) {
				System.out.print(CommUtil.getExpStack(e));
			}
		}
	}

	
	
}
