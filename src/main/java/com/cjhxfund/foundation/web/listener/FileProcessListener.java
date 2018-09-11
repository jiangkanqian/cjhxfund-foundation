package com.cjhxfund.foundation.web.listener;

import java.text.NumberFormat;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;

/**
 * 文件进度监听器
 * 
 * @author lanhan
 * @date 2015年11月25日
 */
public class FileProcessListener implements ProgressListener {

	private static JLogger logger = SysLogger.getLogger(FileProcessListener.class);

	private HttpSession session;

	public FileProcessListener(HttpSession session) {
		this.session = session;
	}

	public void update(long pBytesRead, long pContentLength, int pItems) {
		double readByte = pBytesRead;
		double totalSize = pContentLength;
		if (pContentLength == -1) {
			logger.info("item index["+pItems+"] "+pBytesRead+" bytes have been read." );
		} else {
			logger.info("item index["+pItems+"] "+pBytesRead+" of "+pContentLength+" bytes have been read." );
			String p = NumberFormat.getPercentInstance().format(readByte / totalSize);
			session.setAttribute("fileUploadProcess", p);
		}
	}
}
