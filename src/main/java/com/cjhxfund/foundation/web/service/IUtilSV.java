package com.cjhxfund.foundation.web.service;

import com.cjhxfund.foundation.web.model.HttpCtrl;

public interface IUtilSV {

	void init(HttpCtrl httpCtrl);

	void fileList(HttpCtrl httpCtrl);

	void getDoc(HttpCtrl httpCtrl);

	void uploadFile(HttpCtrl httpCtrl);

	void listThread(HttpCtrl httpCtrl);
	

	void getInitList(HttpCtrl httpCtrl);

	void loggerFile(HttpCtrl httpCtrl);



}
