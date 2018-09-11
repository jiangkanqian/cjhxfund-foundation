package com.cjhxfund.foundation.log.strategy;

public interface JLogger {

//	JLogger getLogger(Class<?> clazz);
	
	void debug(String logMsg);

	void info(String logMsg);

	void warn(String logMsg);

	void warn(String logMsg, Throwable throwable);

	void warn(String logMsg, Exception ex);

	void warn(Throwable ex);
	
	void warn(Exception ex);

	void error(String logMsg);

	void error(Throwable throwable);
	
	void error(String logMsg, Throwable throwable);

	void error(Exception ex);

	void error(String logMsg, Exception ex);

	void fatal(String logMsg);

	void fatal(String logMsg,Throwable throwable);
	
	void fatal(Throwable throwable);

	void fatal(Exception ex);

	void fatal(String logMsg, Exception ex);
}
