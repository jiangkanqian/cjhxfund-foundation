package com.cjhxfund.foundation.log.strategy;

import com.cjhxfund.foundation.log.constants.Constant;
import com.cjhxfund.foundation.log.constants.FLogger;
import com.cjhxfund.foundation.log.utils.CommUtil;
import com.cjhxfund.foundation.log.utils.TimeUtil;

public class JLoggerFactory implements JLogger{

	protected String className = "";
	
	protected static FLogger logger;
	

	static {
		logger = FLogger.getInstance();
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * 获取当前执行方法堆栈信息
	 * @param logMsg
	 * @param stackNum 往上的堆栈名称，必须大于0
	 * @return
	 */
	public String getStackInfo(String logMsg, int stackNum){
		StringBuffer sb = new StringBuffer();
		StackTraceElement elem = Thread.currentThread().getStackTrace()[stackNum];
		sb.append("-[");
		sb.append(elem.getFileName()).append(":");
		sb.append(elem.getLineNumber());
		sb.append("]-");
		sb.append(elem.getMethodName());
		sb.append("-");
		sb.append(logMsg);
		return sb.toString();
	}
	
	/**
	 * 写调试日志
	 * @param logMsg 日志内容
	 */
	public void debug(String logMsg) {
		writeLog("debug", Constant.DEBUG, logMsg);
	}

	/**
	 * 写普通日志
	 * @param logMsg 日志内容
	 */
	public void info(String logMsg) {
		
		writeLog("info", Constant.INFO, logMsg);
	}

	/**
	 * 写警告日志
	 * @param logMsg 日志内容
	 */
	public void warn(String logMsg) {
		writeLog("warn", Constant.WARN, logMsg);
	}

	/**
	 * 写错误日志
	 * @param logMsg 日志内容
	 */
	public void error(String logMsg) {
		writeLog("error", Constant.ERROR, logMsg);
	}

	/**
	 * 写严重错误日志
	 * @param logMsg 日志内容
	 */
	public void fatal(String logMsg) {
		writeLog("fatal", Constant.FATAL, logMsg);
	}

	

	
	

	@Override
	public void warn(String logMsg, Throwable ex) {
		warn(logMsg+"--"+CommUtil.getExpStack(ex));
	}

	@Override
	public void warn(Throwable ex) {
		warn(CommUtil.getExpStack(ex));
	}
	
	@Override
	public void warn(Exception ex) {
		warn(CommUtil.getExpStack(ex));
	}
	
	@Override
	public void warn(String logMsg, Exception ex) {
		warn(logMsg+"--"+CommUtil.getExpStack(ex));
	}
	
	@Override
	public void error(String logMsg, Throwable ex) {
		error(logMsg+"--"+CommUtil.getExpStack(ex));
	}
	
	@Override
	public void error(Throwable ex) {
		error(CommUtil.getExpStack(ex));
	}
	
	@Override
	public void error(Exception ex) {
		error(CommUtil.getExpStack(ex));
	}
	
	@Override
	public void error(String logMsg, Exception ex) {
		error(logMsg+"--"+CommUtil.getExpStack(ex));
	}
	
	@Override
	public void fatal(Throwable ex) {
		fatal(CommUtil.getExpStack(ex));
	}

	@Override
	public void fatal(Exception ex) {
		fatal(CommUtil.getExpStack(ex));
	}

	@Override
	public void fatal(String logMsg, Throwable ex) {
		fatal(logMsg+"--"+CommUtil.getExpStack(ex));
	}
	
	@Override
	public void fatal(String logMsg, Exception ex) {
		fatal(logMsg+"--"+CommUtil.getExpStack(ex));
	}
	

	/**
	 * 写系统日志
	 * @param level 日志级别
	 * @param logMsg 日志内容
	 */
	protected void writeLog(int level, String logMsg) {
		writeLog(Constant.LOG_DESC_MAP.get(String.valueOf(level)).toLowerCase(), level, logMsg);
	}

	protected StringBuffer getStringBuffer(int level, String logMsg){
		StringBuffer sb = new StringBuffer(logMsg.length() + 200);
		sb.append("[");
		sb.append(Constant.LOG_DESC_MAP.get(String.valueOf(level)));
		sb.append("]");
		sb.append("-[");
		sb.append(TimeUtil.getFullDateTime());
		sb.append("]");
		sb.append("-[");
		sb.append(Thread.currentThread().getName());
		sb.append("]");
//		sb.append("-[");
//		sb.append(getClassName());
//		sb.append("]-");
		//获取当前线程的堆栈信息，本方法对应是0，1应该是writeLog，2对应的是writeLog（本方法中的），3对应的是info或者debug等，4对应的是输出方法，依次类推
		StackTraceElement[] stackArr = Thread.currentThread().getStackTrace();
		StackTraceElement elem = stackArr[4];
//		sb.append(elem.getClassName()).append(".").append(elem.getMethodName());
		if(elem.getFileName().equals("HttpCtrl.java")){
			elem = stackArr[6];
			if(elem.getFileName().equals("BaseController.java")){
				elem = stackArr[7];
			}
		}
		if(elem.getFileName().equals("JLoggerFactory.java")){
			elem = stackArr[5];
		}
		//必须有一个空格才能定位
		sb.append("- (");
//		sb.append(elem.getFileName().replace(".java", "."+elem.getMethodName())).append(":");//获取文件名
		sb.append(elem.getFileName()).append(":");//获取文件名
		sb.append(elem.getLineNumber());//获取行号
		sb.append(")-");
		sb.append(elem.getMethodName());//获取输出的方法名
		sb.append("()-msg:");
		sb.append(logMsg);
		sb.append(FLogger.line);
		return sb;
	}
	
	/**
	 * 写日志
	 * @param logFileName 日志文件名
	 * @param level 日志级别
	 * @param logMsg 日志内容
	 */
	protected  void writeLog(String logFileName, int level, String logMsg) {
		
	}
	
	public  static JLogger getLogger(Class<?> clazz) {
		return null;
	}
	
	public static void close(){
		logger.close();
	}
	
}
