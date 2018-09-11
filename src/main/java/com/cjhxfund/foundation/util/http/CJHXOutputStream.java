package com.cjhxfund.foundation.util.http;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class CJHXOutputStream extends ServletOutputStream {

	private ServletOutputStream outputStream;

	public CJHXOutputStream(ServletOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	@Override
	public void write(int b) throws IOException {
		outputStream.write(b);
//		System.out.println("output1");
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		super.write(b, off, len);
//		System.out.println("output2");
	}

	@Override
	public void write(byte[] b) throws IOException {
		super.write(b);
//		System.out.println("output3");
	}
	

	@Override
	public void print(String arg0) throws IOException {
		super.print(arg0);
	}

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public void setWriteListener(WriteListener arg0) {
		
	}
	
	
}
