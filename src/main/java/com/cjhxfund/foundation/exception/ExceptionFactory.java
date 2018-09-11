package com.cjhxfund.foundation.exception;

public class ExceptionFactory {

	public static Exception wrapException(String message, Exception e) {
		return new CjhxFundException(MyErrorContext.instance().message(message).cause(e)
				.toString(), e);
	}
}
