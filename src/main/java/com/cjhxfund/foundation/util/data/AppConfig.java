package com.cjhxfund.foundation.util.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.cjhxfund.foundation.util.io.InputStreamUtil;

/**
 * @author xiejs
 * @date 2015年5月28日
 */
public class AppConfig {
//	private static AppConfig INSTANCE;
//	private Properties props = new Properties();
	
	private static final Map<String, Properties> PROP_POOL = new HashMap<String,Properties>();


//	public static AppConfig getInstance(String proName) {
//		if (INSTANCE == null) {
//			INSTANCE = new AppConfig(proName);
//		}
//		return INSTANCE;
//	}


	public static void load(String[] proFiles) {
		int length = proFiles.length;
		for(int i=0; i < length; i++){
			load(proFiles[i]);
		}
	}
	
	private static Properties load(String proFile) {
		InputStream is = null;
		Properties props = null;
		if(proFile.contains(":")){
			//硬盘目录，用户测试
			is = InputStreamUtil.fileToInputStream(proFile);
		}
		else{
			//直接在resources目录下
			is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(proFile);
		}
		try {
			props = new Properties();
			props.load(is);
			PROP_POOL.put(proFile, props);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return props;
	}
	
	/**
	 * 如果加载的文件不再class下，则proFile是完整的文件路径，则可以给该文件
	 * @param proFile 文件路径
	 * @param proName 文件缓存别名
	 */
	private static Properties load(String proFile, String proName) {
		InputStream is = null;
		Properties props = null;
		if(proFile.contains(":")){
			//硬盘目录，用户测试
			is = InputStreamUtil.fileToInputStream(proFile);
		}
		else{
			//直接在resources目录下
			is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(proFile);
		}
		try {
			props = new Properties();
			props.load(is);
			PROP_POOL.put(proName, props);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return props;
	}

//	/**
//	 * 默认去第一个，如果只有一个config加载了
//	 * @param key
//	 * @return
//	 */
//	public static String getProperty(String key) {
//		for(Entry<String, Properties> entry : PROP_POOL.entrySet()){
//			return entry.getValue().getProperty(key);
//		}
//		return "";
//	}
	
	/**
	 * 如果文件缓存存在，则从缓存中去，如果不存在，则会加载到缓存，然后返回key对应的属性值
	 * @param proFile 文件名或者文件缓存别名
	 * @param key 属性关键字
	 * @return
	 */
	public static String getProperty(String proFile, String key) {
		Properties props = PROP_POOL.get(proFile);
		String value = "";
		if(props != null){
			value = props.getProperty(key);
		}
		else{
			props = load(proFile);
			if(props != null){
				value = props.getProperty(key);
			}
		}
		return value;
	}
	
	/**
	 * 如果文件缓存存在，则从缓存中去，如果不存在，则会加载到缓存，然后返回key对应的属性值
	 * @param proFile 文件名
	 * @param proName 文件缓存别名
	 * @param key 属性关键字
	 * @return
	 */
	public static String getProperty(String proFile, String proName, String key) {
		Properties props = PROP_POOL.get(proName);
		String value = "";
		if(props != null){
			value = props.getProperty(key);
		}
		else{
			props = load(proFile,proName);
			if(props != null){
				value = props.getProperty(key);
			}
		}
		return value;
	}
}
