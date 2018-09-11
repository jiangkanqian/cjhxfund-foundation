package com.cjhxfund.foundation.util.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 包装Response，主要是讲返回
 * 
 */
public class CJHXResponseWrapper extends HttpServletResponseWrapper {

	private CJHXWriter chjxWriter;
	private CJHXOutputStream myOutputStream;

	public CJHXResponseWrapper(HttpServletResponse response) {
		super(response);
		try {
//			myOutputStream = new CJHXOutputStream(response.getOutputStream());
			chjxWriter = new CJHXWriter(response.getWriter());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public CJHXWriter getCJHXWriter() {
		return chjxWriter;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
//		chjxWriter = new CJHXWriter(super.getWriter());
		return chjxWriter;
	}
	
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		myOutputStream = new CJHXOutputStream(super.getOutputStream());
		return myOutputStream;
	}

	public CJHXOutputStream getMyOutputStream() {
		return myOutputStream;
	}
}
