package com.cjhxfund.foundation.util.http;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.codec.Charsets;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.util.data.DataUtil;
import com.cjhxfund.foundation.util.data.NumberUtil;
import com.cjhxfund.foundation.util.io.InputStreamUtil;
import com.cjhxfund.foundation.util.io.PathUtil;
import com.cjhxfund.foundation.web.model.FileInfo;
import com.cjhxfund.foundation.web.model.HttpCtrl;

/**
 * @author lanhan
 * @date 2015年11月25日
 */
public class FileUploadUtil {
	// private static Logger log = LoggerFactory.getLogger(FileUploadUtil.class);
	private static JLogger logger = SysLogger.getLogger(FileUploadUtil.class);

	public static String upload(ServletContext context, HttpCtrl httpCtrl, String desc) {
		FileInfo returnData = new FileInfo();

		DiskFileItemFactory factory = new DiskFileItemFactory();
		FileCleaningTracker fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker(context);
		factory.setFileCleaningTracker(fileCleaningTracker);
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 设置文件上传进度监听器
		// FileProcessListener processListener = new FileProcessListener(request.getSession());
		// upload.setProgressListener(processListener);

		// log.info("文件上传限制：{}",desc);
		String[] checkInfo = desc.split(",");
		upload.setFileSizeMax(Long.valueOf(checkInfo[0]));
		// upload.setFileSizeMax(10485760L);
		// 设置文件上传的头编码，如果需要正确接收中文文件路径或者文件名
		String[] extensionPermit = checkInfo[1].split("-");
		String saveDirectory = "";
		String fileName = "";

		if (!checkInfo[2].equals("noPath")) {// 如果没有路径标识，说明要上传到其他服务器
			if (checkInfo[2].equals("classPath")) {
				saveDirectory = PathUtil.getRealClassPath();
			} else if (checkInfo[2].equals("webRootPath")) {
				saveDirectory = PathUtil.getWebRootPath() + "/";
			}

			saveDirectory = saveDirectory + checkInfo[3] + "/";
			if (checkInfo[4].equals("fileName")) {
				fileName = httpCtrl.getQueryMap().get("fileName").toString();
			} else if (checkInfo[4].equals("genName")) {
				fileName = NumberUtil.getOrderNo(4).toString();
			}
		}

		// 这里需要设置对应的字符编码，为了通用这里设置为UTF-8
		upload.setHeaderEncoding(Charsets.UTF_8.toString());
		// 解析请求数据包
		try {
			List<FileItem> fileItems = upload.parseRequest(httpCtrl.getRequest());
			int fileSize = fileItems.size();
			logger.info("fileItems size : " + fileSize);
			if (fileSize == 1) {
				FileItem fileItem = fileItems.get(0);
				String fileExtension = FilenameUtils.getExtension(fileItem.getName());
				if (checkInfo[4].equals("defaultName")) {
					fileName = FilenameUtils.getName(fileItem.getName());
				} else if (DataUtil.isEmptyStr(fileName)) {
					fileName = fileItem.getName();
				} else {
					fileName = fileName + "." + fileExtension;
				}
				// log.info("fileName : " + fileName);

				// 有的浏览器上传文件时，附带了本地文件上传目录
				if (fileName.contains("/")) {
					String[] strArr = fileName.split("/");
					fileName = strArr[strArr.length - 1];
				} else if (fileName.contains("\\")) {
					String[] strArr = fileName.split("\\\\");
					fileName = strArr[strArr.length - 1];
				}

				returnData.setFileName(fileName);
				returnData.setSize(fileItem.getSize());
				returnData.setGenName(fileName);
				returnData.setExt(fileExtension);
				returnData.setPath(saveDirectory + fileName);

				logger.info("file path:" + saveDirectory + ", " + fileName);
				// 如果为上传文件数据
				if (!fileItem.isFormField()) {
					// log.debug("upload file fieldName:{} fileName:{},fileSize:{}",fileItem.getName(),
					// fileItem.getName(),fileItem.getSize());
					if (fileItem.getSize() > 0) {
						if (!checkInfo[1].trim().equals("*") && !ArrayUtils.contains(extensionPermit, fileExtension)) {
							return fileExtension + "文件类型不支持!";
						}
						if (!checkInfo[2].equals("noPath")) { // 如果没有路径标识，说明要上传到其他服务器
							FileUtils.copyInputStreamToFile(fileItem.getInputStream(), new File(saveDirectory, fileName));
						} else {
							httpCtrl.setFileBytes(InputStreamUtil.InputStreamTOByte(fileItem.getInputStream()));
						}
					}
				} else { // Form表单数据
//					String value = fileItem.getString(Charsets.UTF_8.toString());
//					log.debug("form'data fieldName:{} fieldValue:{}", fileItem.getName(), value);
				}
				httpCtrl.getQueryMap().put("fileInfo", returnData);
			} else {

				// 遍历解析完成后的Form数据和上传文件数据
				for (Iterator<FileItem> iterator = fileItems.iterator(); iterator.hasNext();) {
					FileItem fileItem = iterator.next();
					// 如果为上传文件数据
					if (!fileItem.isFormField()) {
//						log.debug("upload file fieldName:{} fileName:{}", fileItem.getName(), fileItem.getName());
						if (fileItem.getSize() > 0) {
							String fileExtension = FilenameUtils.getExtension(fileItem.getName());
							if (!ArrayUtils.contains(extensionPermit, fileExtension)) {
								return fileExtension + "文件类型不支持!";
							}
							fileName = FilenameUtils.getName(fileItem.getName());
							FileUtils.copyInputStreamToFile(fileItem.getInputStream(), new File(saveDirectory, fileName));
						}
					} else { // Form表单数据
//						String value = fileItem.getString(Charsets.UTF_8.toString());
//						log.debug("form'data fieldName:{} fieldValue:{}", fileItem.getName(), value);
					}
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
