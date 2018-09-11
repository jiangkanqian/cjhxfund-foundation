package com.cjhxfund.foundation.util.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WindowsCmd {
	
	private static final String charset = "GBK";
	

	public static String cmd(String cmd, String flag) {
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
//					result.append(lineStr).append("\n");
					result.append(lineStr).append(flag);
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
	
	/**
	 * 解析windows 命令的头部，由于返回的值和头部是左对齐的，则可以记录头部的起始位置，既可以获取对应的值，然后转换为json对象
	 */
	public static List<WindowsTitle> getCmdTitle(String  titleStr){
		List<WindowsTitle> data = new ArrayList<WindowsTitle>();
		char[] charArr = titleStr.toCharArray();
		String title = "";
		int start = 0;
		int end = 0;
		int flag = 0;//0 表示开始，1：表示单词结束，进入空格，2：表示空格结束，进入单词；
		for(int i=0, length = charArr.length; i< length; i++){
			char c = charArr[i];
			title += String.valueOf(c);
			if(c != ' '){
				if(flag == 1){
					flag = 2;
					title = title.substring(0, title.length() - 1);
					end  = start + title.length();
					WindowsTitle d = new WindowsTitle(title, start);
					data.add(d);
					start = end;
					title = String.valueOf(c);
				}
				else{
					flag = 0;
				}
			}
			else if(c == ' '){
				flag = 1;
			}
			
		}
		WindowsTitle d = new WindowsTitle(title, start);
		data.add(d);
		
		return data;
	}
	
	
	/**
	 * 由于返回的值和头部是左对齐的，则可以记录头部的起始位置，既可以获取对应的值，然后转换为json对象
	 * @param cmd windows的命令行
	 * @return
	 */
	public static List<Map<String, String>> getSysInfo(String cmd){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String result = WindowsCmd.cmd(cmd, ";");
		String[] strArr = result.split(";");
		List<WindowsTitle> titleList = getCmdTitle(strArr[0]);
		String value = "";
		String info = "";
		for(int i=1, length = strArr.length; i < length; i++){
			Map<String, String> infoMap = new HashMap<String,String>();
			info = strArr[i];
			for(int j = 0, size = titleList.size(); j < size; j++){
				WindowsTitle title = titleList.get(j);
				if(j == size-1 && title.getEnd() != info.length()){
					//末尾的位置可能和头部不是一致
					value = info.substring(title.getStart(), info.length()).trim();
					infoMap.put(title.getName(), value);
				}
				else{
					value = info.substring(title.getStart(), title.getEnd()).trim();
					infoMap.put(title.getName(), value);
				}
			}
			list.add(infoMap);
		}
		
		return list;
	}
	
//	public static void main(String[] args) {
//		List<Map<String, String>> data = getSysInfo("wmic cpu");
////		DataUtil.print(data.get(0));
//		System.out.println(JsonUtil.toJson(data));
//	}
	
}
