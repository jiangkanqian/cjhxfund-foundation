package com.cjhxfund.foundation.exception;

/**
 * @author xiejs
 * @date 2015年7月11日
 */
public class MyErrorContext {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
	private static final ThreadLocal<MyErrorContext> LOCAL = new ThreadLocal<MyErrorContext>();
	private MyErrorContext stored;
	private String resource;
	private String activity;
	private String object;
	private String message;
	private Throwable cause;

	public static MyErrorContext instance() {
		MyErrorContext context = (MyErrorContext) LOCAL.get();
		if (context == null) {
			context = new MyErrorContext();
			LOCAL.set(context);
		}
		return context;
	}

	public MyErrorContext store() {
		this.stored = this;
		LOCAL.set(new MyErrorContext());
		return (MyErrorContext) LOCAL.get();
	}

	public MyErrorContext recall() {
		if (this.stored != null) {
			LOCAL.set(this.stored);
			this.stored = null;
		}
		return (MyErrorContext) LOCAL.get();
	}

	public MyErrorContext message(String message) {
		this.message = message;
		return this;
	}

	public MyErrorContext cause(Throwable cause) {
		this.cause = cause;
		return this;
	}

	public MyErrorContext reset() {
		this.resource = null;
		this.activity = null;
		this.object = null;
		this.message = null;
		this.cause = null;
		LOCAL.remove();
		return this;
	}

	public String toString() {
		StringBuffer description = new StringBuffer();

		if (this.message != null) {
			description.append(LINE_SEPARATOR);
			description.append("### ");
			description.append(this.message);
		}

		if (this.resource != null) {
			description.append(LINE_SEPARATOR);
			description.append("### The error may exist in ");
			description.append(this.resource);
		}

		if (this.object != null) {
			description.append(LINE_SEPARATOR);
			description.append("### The error may involve ");
			description.append(this.object);
		}

		if (this.activity != null) {
			description.append(LINE_SEPARATOR);
			description.append("### The error occurred while ");
			description.append(this.activity);
		}

		if (this.cause != null) {
			description.append(LINE_SEPARATOR);
			description.append("### Cause: ");
			description.append(this.cause.toString());
		}

		return description.toString();
	}
}
