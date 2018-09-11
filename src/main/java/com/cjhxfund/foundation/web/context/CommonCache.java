package com.cjhxfund.foundation.web.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cjhxfund.foundation.util.data.InitBeanData;
import com.cjhxfund.foundation.web.model.CtrlBean;
import com.cjhxfund.foundation.web.model.WSSession;
import com.cjhxfund.foundation.web.model.sys.DictEntry;
import com.cjhxfund.foundation.web.model.sys.DictType;
import com.google.gson.JsonObject;

/**
 * 共同用到的缓存数据
 * 
 * @author xiejs
 * @date 2015年7月13日
 */
public final class CommonCache {

	/** 文件请求路径 */
	public static JsonObject configJson = null;

	/** bean缓存*/
	public static final Map<String, CtrlBean> ctrlBeanCache = new ConcurrentHashMap<String, CtrlBean>();
	
	/** 初始化bean信息*/
	public static final Map<String, InitBeanData> initBeanCache = new HashMap<String, InitBeanData>();

	/** 无需登录即可访问的接口 */
	public static final List<String> freePathHolder = new ArrayList<String>();

	/** 不登录但需要验证才可访问的接口 */
	public static final List<String> tokenPathHolder = new ArrayList<String>();

	/** 字典数据其key为:类型编号-字典key,value为dictValue */
	public static final Map<String, String> dictInfoCache = new HashMap<String, String>();

	public static final  Map<String,DictType> dictTypeCache = new HashMap<String, DictType>();
	
	public static final  Map<String, WSSession> sessionMap = new ConcurrentHashMap<String, WSSession>();

	/**
	 * 检查相应的数据是否和相应的指定的字典值相等；
	 * @param key 字典key
	 * @param value 要比对的value
	 * @return
	 */
	public static boolean checkDict(String key, String value) {
		String val = CommonCache.dictInfoCache.get(key);
		if (null != val && val.equals(value)) {
			return true;
		}
		return false;
	}

	/**
	 * 检查系统配置文件中的指定key是否存在
	 * @param key
	 * @return
	 */
	public static boolean checkConfig(String key) {
		if (null != CommonCache.configJson && null != CommonCache.configJson.get(key)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * 获取key值
	 * @param key
	 * @return
	 */
	public static String getConfig(String key) {
		if(checkConfig(key)){
			return CommonCache.configJson.get(key).toString();
		}
	    return null;
    }
	

	/**
	 * 根据字典类型编号（别名）获取字典列表
	 * @param typeNo 类型编号
	 * @return 字典列表,如果没有匹配到则返回null
	 */
	public static List<DictEntry> getDictList(String typeNo) {
		DictType dt = CommonCache.dictTypeCache.get(typeNo);
		if(null != dt){
			return dt.getDictList();
		}
		return null;
	}

	

}
