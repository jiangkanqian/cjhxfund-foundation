package com.cjhxfund.foundation.log.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Properties;

import com.cjhxfund.foundation.log.constants.Constant;
import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.util.data.DataUtil;
import com.cjhxfund.foundation.util.str.EncodeUtil;

/**
 * 公用工具类
 * @version 2015/10/31
 */
public class CommUtil {
	
	/** 配置文件名 */
	private static final String CONFIG_FILE_NAME = "flogger.properties";
	
	/** 配置map */
	private static HashMap<String,Object[]> propsMap = new HashMap<String,Object[]>();
	
	/**
	 * 从配置文件中取得 String 值，若无则返回默认值
	 * @param keyName 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public static String getConfigByString(String keyName, String defaultValue){
		
		String value = "";
		if(DataUtil.isValidStr(keyName)){
			value = getConfig(keyName);
		}
//		System.out.println(keyName);
//		System.out.println(value);
//		System.out.println(System.getProperty("catalina.base"));
		if(DataUtil.isValidStr(value)){
			if(keyName.equalsIgnoreCase("LOG_PATH") && Constant.CFG_LOG_PREFIX.equalsIgnoreCase("tomcat")){
				if(!value.startsWith("/")){
					value = "/"+value.trim();
				}
				value = System.getProperty("catalina.base") == null ? "" : System.getProperty("catalina.base") + value;
			}
			return value.trim();
		}else{
			return defaultValue;
		}
	}
	
	/**
	 * 从配置文件中取得 int 值，若无（或解析异常）则返回默认值
	 * @param keyName 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public static int getConfigByInt(String keyName,int defaultValue){
		String value = getConfig(keyName);
		if(DataUtil.isValidStr(value)){
			try {
				int parseValue = Integer.parseInt(value.trim());
				return parseValue;
			} catch (Exception e) {
				return defaultValue;
			}
		}else{
			return defaultValue;
		}
	}
	
	/**
	 * 从配置文件中取得 long 值，若无（或解析异常）则返回默认值
	 * @param keyName 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public static long getConfigByLong(String keyName,long defaultValue) {
		String value = getConfig(keyName);
		if(DataUtil.isValidStr(value)){
			try {
				long parseValue = Long.parseLong(value.trim());
				return parseValue;
			} catch (Exception e) {
				return defaultValue;
			}
		}else{
			return defaultValue;
		}
	}
	
	/**
	 * 从配置文件中取得 boolean 值，若无则返回默认值
	 * @param keyName 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public static boolean getConfigByBoolean(String keyName,boolean defaultValue){
		String value = getConfig(keyName);
		if(DataUtil.isValidStr(value)){
			return Boolean.parseBoolean(value.trim());
		}else{
			return defaultValue;
		}
	}
	
	/**
	 * 从配置文件中读取字符串的值
	 * 配置文件查找顺序：
	 * 		1-项目根路径
	 * 		2-src/main/resources
	 * @param keyName 属性名
	 * @return 属性值
	 */
	private static String getConfig(String keyName) {
		Properties props = null;
		boolean bIsNeedLoadCfg = false;
		String filePath = CONFIG_FILE_NAME;
		File cfgFile = new File(filePath);
		//如果指定的文件名是绝对路径就直接获取
		if(!cfgFile.exists()){
			try{
				//获取不到则说明是系统的相对路径，class路径
				filePath = CommUtil.class.getClassLoader().getResource(CONFIG_FILE_NAME).getPath();
//				System.out.println(filePath);
				//如果目录有空格，则可能会有转码的情况
				if(filePath.contains("%20")){
					filePath = EncodeUtil.decodeUTF8(filePath);
				}
//				System.out.println(filePath);
				cfgFile = new File(filePath);
			}catch (Exception e) {
				JLogger logger = SysLogger.getLogger(CommUtil.class);
				logger.error(e);
//				FLogger.getInstance().close();
				return null;
			}
			if(!cfgFile.exists()){
				return null;
			}
			
		}
		
		Object[] arrs = propsMap.get(filePath);
		if(arrs == null){
			bIsNeedLoadCfg = true ;
		}else{
			Long lastModify = (Long)arrs[0];
			if(lastModify.longValue() != cfgFile.lastModified()){
				bIsNeedLoadCfg = true;
			}else{
				props = (Properties)arrs[1];
			}
		}
		if(bIsNeedLoadCfg){
			FileInputStream fis = null;
			try{
				fis = new FileInputStream(cfgFile);
				props = new Properties();		
				props.load(fis);
				propsMap.put(filePath, new Object[]{cfgFile.lastModified(),props});
			}catch (Exception e) {
				return "";
			}finally{
				try{
					if(fis != null){
						fis.close();
					}
				}catch(Exception e){;}
			}
		}
		
		return props.getProperty(keyName, "");
	}
	
	/**
	 * 将字符串转为字节数组
	 * @param str 源字符串
	 * @return 字节数组
	 */
	public static byte[] StringToBytes(String str) {
		try{
			if(str == null || str.length() <= 0){
				return new byte[0];
			}else{
				return str.getBytes(Constant.CFG_CHARSET_NAME);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将异常的堆栈信息转为字符串
	 * @param e 异常
	 * @return 异常的字符串描述
	 */
	public static String getExpStack(Exception e) {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(bo);
		e.printStackTrace(pw);
		pw.flush();
		pw.close();
		return bo.toString();
	}
	
	/**
	 * 将异常的堆栈信息转为字符串
	 * @param throwable 异常
	 * @return 异常的字符串描述
	 */
	public static String getExpStack(Throwable throwable) {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(bo);
		throwable.printStackTrace(pw);
		pw.flush();
		pw.close();
		return bo.toString();
	}
	
}
