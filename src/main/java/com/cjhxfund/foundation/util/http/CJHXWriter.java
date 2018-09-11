package com.cjhxfund.foundation.util.http;

import java.io.PrintWriter;

public class CJHXWriter extends PrintWriter {

	private StringBuilder buffer;

	public CJHXWriter(PrintWriter out) {
		super(out);
		buffer = new StringBuilder();
	}

	@Override
	public void write(char[] buf, int off, int len) {
//		super.write(buf, off, len);//需要注释到，不要让其写入到reponse里面，write内容是不断追加模式，而不是覆盖模式
		char[] dest = new char[len];
		System.arraycopy(buf, off, dest, 0, len);
		buffer.append(dest);
	}

	@Override
	public void write(char[] buf) {
		super.write(buf);
	}

	@Override
	public void write(int c) {
		super.write(c);
	}

	@Override
	public void write(String s, int off, int len) {
		super.write(s, off, len);
	}

	@Override
	public void write(String s) {
//		super.write(s);//需要注释到，不要让其写入到reponse里面，write内容是不断追加模式，而不是覆盖模式
		buffer.append(s);
//		System.out.println(s);
	}
	
	public void rewrite(String s) {
//		super.write(s);
		buffer = new StringBuilder();
		buffer.append(s);
	}

	public String getContent() {
		return buffer.toString();
	}

	public void flush() {
//		super.flush();
//		System.out.println("flush no");
	}

	public void close() {
//		super.close();
	}

}
