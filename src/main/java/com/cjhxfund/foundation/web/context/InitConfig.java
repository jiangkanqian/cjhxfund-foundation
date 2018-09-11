package com.cjhxfund.foundation.web.context;

import java.io.File;

import org.springframework.stereotype.Service;

import com.cjhxfund.foundation.annotation.InitData;
import com.cjhxfund.foundation.util.io.PathUtil;
import com.cjhxfund.foundation.util.io.ReadFileUtil;
import com.cjhxfund.foundation.util.str.JsonUtil;
import com.cjhxfund.foundation.web.service.BaseInit;
import com.google.gson.JsonObject;

/**
 * @author xiejiesheng 初始化系统配置文件
 */
@InitData(name = "initConfig", desc="初始化系统配置文件")
@Service("initConfig")
public class InitConfig extends BaseInit {

	@Override
	public void initData() {
		String path = PathUtil.getSrcPath("config/config.json");
		File file = new File(path);
		if(file.exists()){
			String content = ReadFileUtil.getFileContent(file);
			JsonObject jo = JsonUtil.decodeJson(content);
			if(jo.isJsonObject()){
				CommonCache.configJson = jo;
				logger.info("加载配置文件：config/config.json");
			}
		}
		else{
			path = PathUtil.getSrcPath("config.json");
			file = new File(path);
			if(file.exists()){
				String content = ReadFileUtil.getFileContent(file);
				JsonObject jo = JsonUtil.decodeJson(content);
				if(jo.isJsonObject()){
					CommonCache.configJson = jo;
					logger.info("加载配置文件：config.json");
				}
			}
			else{
				logger.info("无config配置文件！");
			}
		}
	}

}
