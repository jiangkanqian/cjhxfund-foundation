package com.cjhxfund.foundation.web.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cjhxfund.foundation.annotation.InitData;
import com.cjhxfund.foundation.web.mapper.XMLMapperLoader;
import com.cjhxfund.foundation.web.model.HttpCtrl;
import com.cjhxfund.foundation.web.service.BaseInit;

/**
 * @author xiejiesheng 初始化系统配置文件
 */
@InitData(name = "reloadXML", desc="初始化Mapper中的xml")
@Service("reloadXML")
public class ReloadXML extends BaseInit {

	
	@Autowired
	XMLMapperLoader xmlMapperLoader;
	
	@Override
	public void initData(HttpCtrl httpCtrl) {
		try {
			xmlMapperLoader.reload();
			httpCtrl.sendJson(1,"重新加载成功！");
		} catch (Exception e) {
			httpCtrl.sendJson(0,"加载失败！");
			e.printStackTrace();
		}
	}

}
