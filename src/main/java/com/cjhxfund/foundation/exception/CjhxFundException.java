package com.cjhxfund.foundation.exception;

public class CjhxFundException extends Exception{

	private static final long serialVersionUID = 5001996876173959808L;

	public CjhxFundException() {
	}

	public CjhxFundException(String message) {
		super(message);
	}

	public CjhxFundException(String message, Throwable cause) {
		super(message, cause);
	}

	public CjhxFundException(Throwable cause) {
		super(cause);
	}

	//不需要记录异常的栈信息，影响性能
	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
	
	
}
