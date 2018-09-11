package com.cjhxfund.foundation.log.constants;

import com.cjhxfund.foundation.log.strategy.LogManager;

/**
 * 日志工具类
 * @version 2015/10/31
 */
public class FLogger {

	private static FLogger instance;
	public static LogManager logManager;
	public static final String line = "\r\n";

	static {
		logManager = LogManager.getInstance();
	}

	public FLogger() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				close();
			}
		}));
	}

	private static synchronized FLogger initFlogger() {
		return new FLogger();
	}

	public static FLogger getInstance() {
		if (instance == null) {
			instance = initFlogger();
		}
		return instance;
	}


	/**
	 * 优雅关闭
	 */
	public void close() {
		logManager.close();
	}

}
