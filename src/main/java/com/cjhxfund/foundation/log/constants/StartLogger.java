package com.cjhxfund.foundation.log.constants;

import com.cjhxfund.foundation.log.strategy.JLoggerFactory;
import com.cjhxfund.foundation.log.utils.CommUtil;
import com.cjhxfund.foundation.web.context.SpringContext;

/**
 * 系统启动时的日志输出
 * @author xiejiesheng
 */
public class StartLogger extends JLoggerFactory{
	
	public StartLogger() {}
	
	public StartLogger(Class<?> clazz) {
		setClassName(clazz.getSimpleName());
//		logger.debug(className);
	}
	
	public static StartLogger getLogger(Class<?> clazz) {
		return new StartLogger(clazz);
	}
	
	public static StartLogger getLogger() {
		return new StartLogger();
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
		FLogger.logManager.addLog("StartLogger-"+logFileName, sb);
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
	
	public void remove(){
		FLogger.logManager.removeStart();
	}

	
	
}
