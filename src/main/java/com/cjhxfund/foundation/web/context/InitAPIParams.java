package com.cjhxfund.foundation.web.context;

import org.springframework.stereotype.Service;

import com.cjhxfund.foundation.annotation.InitData;
import com.cjhxfund.foundation.util.data.APIDocUtil;
import com.cjhxfund.foundation.web.model.HttpCtrl;
import com.cjhxfund.foundation.web.service.BaseInit;

/**
 * @author xiejiesheng 初始化系统配置文件
 */
@InitData(name = "initAPIParams", desc="重新加载系统API参数")
@Service("initAPIParams")
public class InitAPIParams extends BaseInit {

	
	@Override
	public void initData(HttpCtrl httpCtrl) {
		APIDocUtil.reloadCtrlAnno();
		httpCtrl.sendJson(1,"API刷新成功！");
	}

}
