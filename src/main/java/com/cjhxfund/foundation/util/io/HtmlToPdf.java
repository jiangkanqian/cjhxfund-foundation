package com.cjhxfund.foundation.util.io;

import java.io.File;

public class HtmlToPdf {

	//wkhtmltopdf在系统中的路径  
	private static final String toPdfTool = "D:/appinstall/wkhtmltopdf/bin/wkhtmltopdf.exe";

	/** 
	 * html转pdf 
	 * @param srcPath html路径，可以是硬盘上的路径，也可以是网络路径 
	 * @param destPath pdf保存路径 
	 * @return 转换成功返回true 
	 */
	public static boolean convert(String srcPath, String destPath) {
		File file = new File(destPath);
		File parent = file.getParentFile();
		//如果pdf保存路径不存在，则创建路径  
		if (!parent.exists()) {
			parent.mkdirs();
		}

		StringBuilder cmd = new StringBuilder();
		cmd.append(toPdfTool);
		cmd.append(" --outline-depth 10 --no-stop-slow-scripts --javascript-delay 5000");
		cmd.append(" ");
		cmd.append(srcPath);
		cmd.append(" ");
		cmd.append(destPath);

		boolean result = true;
		try {
			Process proc = Runtime.getRuntime().exec(cmd.toString());
			HtmlToPdfInterceptor error = new HtmlToPdfInterceptor(proc.getErrorStream());
			error.start();
			HtmlToPdfInterceptor output = new HtmlToPdfInterceptor(proc.getInputStream());
			output.start();
			proc.waitFor();
			System.out.println("----------------success:");
			System.out.println(output.getResultMsg());
			System.out.println("----------------error:");
			System.out.println(error.getResultMsg());
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}

		return result;
	}

	

}
