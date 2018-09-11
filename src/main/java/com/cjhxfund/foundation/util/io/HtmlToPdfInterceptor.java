package com.cjhxfund.foundation.util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/** 
 * 当java调用wkhtmltopdf时，用于获取wkhtmltopdf返回的内容 
 */
public class HtmlToPdfInterceptor extends Thread{
	
	private StringBuffer resultMsg;
	private InputStream is;

	public HtmlToPdfInterceptor(InputStream is) {
		this.is = is;
		resultMsg = new StringBuffer();
	}

	public String getResultMsg() {
		return resultMsg.toString();
	}




	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
//				System.out.println(line);
				resultMsg.append(line).append("\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
