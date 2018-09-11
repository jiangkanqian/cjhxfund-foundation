package com.cjhxfund.foundation.web.service;

import com.cjhxfund.foundation.web.model.HttpCtrl;

/**
 * 初始化数据 接口，凡是继承该接口的都会在项目启动时初始化加载相关数据
 * @author Jason
 * @date 2015年12月16日
 */
public interface IInitDataSV {

	 void initData(HttpCtrl httpCtrl);
}
