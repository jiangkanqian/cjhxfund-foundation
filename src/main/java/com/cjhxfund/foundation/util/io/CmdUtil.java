package com.cjhxfund.foundation.util.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * @author Jason
 * @date 2016年4月19日
 */
public class CmdUtil {

//	private static String charset = System.getProperty("file.encoding");
	private static String charset = "GBK";
	/**
	 * 打包指定模块
	 * 
	 * @author Jason
	 * @date 2016年4月11日
	 * @param module
	 *            指定模块路径，比如：E:/workspace/myproject/ueditor-util，打包为ueditor-util.jar
	 */
	public static void mvn(String module) {
		mvn(module,"mvn install");
	}
	
	public static void mvn(String module, String cmd){
		Runtime run = Runtime.getRuntime();
		cmd = "cmd /c "+cmd;// 打包当前运行模块
		File dir = new File(module);
		// File dir = new File("E:/workspace/myproject/ueditor-util");
		try {
			// run.exec("cmd /c mvn clean package");//默认打包当前运行模块
			Process p = run.exec(cmd, null, dir);// 打包指定路径
			BufferedInputStream in = new BufferedInputStream(p.getInputStream());
			BufferedReader inBr = new BufferedReader(new InputStreamReader(in, charset));
			String lineStr;
			while ((lineStr = inBr.readLine()) != null)
				// 获得命令执行后在控制台的输出信息
				System.out.println(lineStr);// 打印输出信息
			// 检查命令是否执行失败。
			if (p.waitFor() != 0) {
				if (p.exitValue() == 1)// p.exitValue()==0表示正常结束，1：非正常结束
					System.err.println("命令执行失败!");
			}
			inBr.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String cmd(String module, String cmd) {
		Runtime run = Runtime.getRuntime();
		String os = System.getProperty("os.name").toLowerCase();
		if(os.contains("windows")){
			cmd = "cmd /c " + cmd;
		}
		File dir = new File(module);
		StringBuilder result = new StringBuilder();
		// File dir = new File("E:/workspace/myproject/ueditor-util");
		try {
			Process p = run.exec(cmd, null, dir);//
			BufferedInputStream in = new BufferedInputStream(p.getInputStream());
			BufferedReader inBr = new BufferedReader(new InputStreamReader(in, charset));
			String lineStr;
			while ((lineStr = inBr.readLine()) != null){
				// 获得命令执行后在控制台的输出信息
				result.append(lineStr).append("\r\n");
//				System.out.println(lineStr);
			}
			// 检查命令是否执行失败。
			if (p.waitFor() != 0) {
				if (p.exitValue() == 1)// p.exitValue()==0表示正常结束，1：非正常结束
					System.err.println("命令执行失败!");
			}
			inBr.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public static String cmd(String cmd) {
		Runtime run = Runtime.getRuntime();
		String os = System.getProperty("os.name").toLowerCase();
		if(os.contains("windows")){
			cmd = "cmd /c " + cmd;
		}
		StringBuilder result = new StringBuilder();
		try {
//			System.out.println(cmd);
			// run.exec("cmd /c mvn clean package");//
			Process p = run.exec(cmd);// 
			BufferedInputStream in = new BufferedInputStream(p.getInputStream());
			BufferedReader inBr = new BufferedReader(new InputStreamReader(in, charset));
			String lineStr;
			while ((lineStr = inBr.readLine()) != null){
				// 获得命令执行后在控制台的输出信息
				if(lineStr.trim().length() > 0){
					result.append(lineStr).append("\n");
				}
//				System.out.println(lineStr);
			}
			// 检查命令是否执行失败。
			if (p.waitFor() != 0) {
				if (p.exitValue() == 1)// p.exitValue()==0表示正常结束，1：非正常结束
					System.err.println("命令执行失败!");
			}
			inBr.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	
//	public static void exeBash(String bashPath) {
//		try {
//			bashPath = "cmd.exe /c start "+ bashPath;
//			Runtime.getRuntime().exec(bashPath);//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void main(String[] args) {
		String result = CmdUtil.cmd("wmic memorychip list brief");
		System.out.println(result);
		System.out.println(4294967296D/1024/1024);
	}

}
