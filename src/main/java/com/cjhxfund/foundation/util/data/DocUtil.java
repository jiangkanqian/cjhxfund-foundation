package com.cjhxfund.foundation.util.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.cjhxfund.foundation.annotation.data.DocData;
import com.cjhxfund.foundation.web.context.CommonCache;
import com.cjhxfund.foundation.web.model.APIMonitor;
import com.cjhxfund.foundation.web.model.CtrlBean;

/**
 * @author xiejs
 * @date 2015年7月20日
 */
public class DocUtil {

	public static List<APIMonitor> getMonitorList() {
		List<APIMonitor> apiList = new ArrayList<APIMonitor>();
		for (Entry<String, CtrlBean> entry : CommonCache.ctrlBeanCache.entrySet()) {
			DocData doc = entry.getValue().getDoc();
			apiList.add(entry.getValue().getMonitor().clone(doc.getMethod(), doc.getApiName(), doc.getUrl()));
		}
		return apiList;
	}
	
	public static void changeAPI(String qryMethod, String url, int state) {
		String method = qryMethod.split(",")[0];
		CtrlBean ctrlBean = CommonCache.ctrlBeanCache.get(url+"-"+method);
		ctrlBean.getMonitor().setStatus(state);
	}

	/**
	 * 过滤数据
	 * @param qryMethod 请求方法
	 * @param requestName 请求关键词
	 * @param sessionType 登录类型，1：必须登录，其他不需要登录，必须大于0
	 * @return
	 */
	public static List<DocData> getDocList(String qryMethod, String requestName, int sessionType) {
		List<DocData> docs = new ArrayList<DocData>();

		if (sessionType > 0) {
			for (Entry<String, CtrlBean> entry : CommonCache.ctrlBeanCache.entrySet()) {
				String url = entry.getKey();
				DocData doc = entry.getValue().getDoc();
				if (sessionType == 1) {
					if (doc.getSessionType().equals("COMPELLED")) {
						filterData(qryMethod, requestName, url, doc, docs);
					}
				} else {
					if (!doc.getSessionType().equals("COMPELLED")) {
						filterData(qryMethod, requestName, url, doc, docs);
					}
				}
			}
		} else {
			for (Entry<String, CtrlBean> entry : CommonCache.ctrlBeanCache.entrySet()) {
				String url = entry.getKey();
				DocData doc = entry.getValue().getDoc();
				filterData(qryMethod, requestName, url, doc, docs);
			}

		}
		return docs;

	}

	private static void filterData(String qryMethod, String requestName, String url, DocData doc, List<DocData> docs) {
		if (DataUtil.isValidStr(requestName)) {
			if (DataUtil.isValidStr(qryMethod)) {
				if (url.endsWith(qryMethod) && (doc.getApiName().contains(requestName) || doc.getUrl().contains(requestName))) {
					docs.add(doc);
				}
			} else {
				if (doc.getApiName().contains(requestName) || doc.getUrl().contains(requestName)) {
					docs.add(doc);
				}
			}
		} else {
			if (DataUtil.isValidStr(qryMethod)) {
				if (url.endsWith(qryMethod)) {
					docs.add(doc);
				}
			} else {
				docs.add(doc);
			}
		}
	}

}
