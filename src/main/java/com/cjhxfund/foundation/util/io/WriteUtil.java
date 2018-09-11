package com.cjhxfund.foundation.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.util.str.EncodeUtil;

public class WriteUtil{

	private WriteUtil() {
		throw new Error("该类不能被实例化！");
	}

	private static JLogger logger = SysLogger.getLogger(WriteUtil.class);

	/**
	 * @param url:文件所在的文件夹路径
	 * @param content:文件内容
	 * @param fileName:文件名称
	 *
	 */
	public static void writeFile(String url, String content, String fileName) {
		File file = null;

		File filePath = new File(url);
		FileWriter fw = null;
		try {
			file = new File(url, fileName);
			if (!filePath.exists()) {
				filePath.mkdirs();
				if (!file.exists() && filePath.mkdirs()) {
					file.createNewFile();
				}
			}
			fw = new FileWriter(file);

			fw.write(content);
			fw.flush();
//			logger.debug("写入成功！file=" + file.getPath());
		} catch (IOException e) {
			logger.debug("写入失败！file=" + fileName);
			e.printStackTrace();
		} finally {
			try {
				fw.close();
				logger.debug("写入 的IO关闭成功！file=" + fileName);
			} catch (IOException e) {
				logger.error("写入的IO关闭失败！file= " + fileName);
				e.printStackTrace();
			}
		}
	}
	
	public static void writeFile(String fileName, String content) {
		File file = null;
		FileWriter fw = null;
		try {
			file = new File(fileName);
			File folder = file.getParentFile();
			if(!folder.exists()){
				folder.mkdirs();
			}
			fw = new FileWriter(file);
			fw.write(content);
			fw.flush();
//			logger.debug("写入成功！file=" + file.getPath());
		} catch (IOException e) {
			logger.debug("写入失败！file=" + fileName);
			e.printStackTrace();
		} finally {
			try {
				fw.close();
				logger.debug("写入 的IO关闭成功！file=" + fileName);
			} catch (IOException e) {
				logger.error("写入的IO关闭失败！file= " + fileName);
				e.printStackTrace();
			}
		}
	}

	/**
	 * 下载文件
	 * @param fileFullName 文件所在目录
	 * @param fileName 下载时显示的文件名称
	 * @param response
	 */
	public static boolean sendLoadDownFile(String fileFullName, String fileName, HttpServletResponse response) {
		if (fileFullName == null || "".equals(fileFullName)) {
			return false;
		}
		File file = new File(fileFullName);
		if (!file.exists()) {
			return false;
		}
		ServletOutputStream os = null;
		FileInputStream fis = null;
		try {
			// System.out.println(fileName);
			// System.out.println("attachment;filename="+fileName);
			// HttpServletResponse response=ServletActionContext.getResponse();
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition", "attachment;filename=" + EncodeUtil.encodeUTF8(fileName));
			// response.addHeader("Content-Disposition", "attachment;filename="+fileName);
			response.addHeader("Content-Length", file.length() + ""); // 可以不写，但是写了就不能写错

			os = response.getOutputStream();
			fis = new FileInputStream(file);

			int size = 0;
			byte[] buffer = new byte[4096];
			while ((size = fis.read(buffer)) != -1) {
				os.write(buffer, 0, size);
			}
			logger.info("下载文件，数据发送成功……fiie={}" + fileName);
			return true;

		} catch (IOException e) {
			logger.error("下载文件，数据发送异常……fiie={}" + fileName);
			e.printStackTrace();
		} finally {
			try {
				os.flush();
				os.close();
				fis.close();
				logger.info("操作文件流关闭成功……");
			} catch (IOException e) {
				logger.error("操作文件流关闭失败……");
				e.printStackTrace();
			}

		}
		return false;
	}

	/**
	 * 下载文件
	 * @param fis 文件流
	 * @param fileName 下载时显示的文件名称
	 * @param response
	 */
	public static boolean sendLoadDownFile(InputStream fis, String fileName, HttpServletResponse response) {
		ServletOutputStream os = null;
		try {
			// System.out.println("attachment;filename="+fileName);
			// HttpServletResponse response=ServletActionContext.getResponse();
			response.setContentType("application/octet-stream");
			// response.addHeader("Content-Disposition", "attachment;filename="+DataUtil.urlEncodeUTF8(fileName));
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.addHeader("Content-Length", fis.available() + "");
			// response.setContentLength(fis.available());
			os = response.getOutputStream();
			int size = 0;
			byte[] buffer = new byte[4096];
			while ((size = fis.read(buffer)) != -1) {
				os.write(buffer, 0, size);
			}
			logger.info("下载文件，数据发送成功……fiie=" + fileName);
			return true;

		} catch (IOException e) {
			logger.error("下载文件，数据发送异常……fiie=" + fileName);
			e.printStackTrace();
		} finally {
			try {
				os.flush();
				os.close();
				fis.close();
				logger.info("操作文件流关闭成功……");
			} catch (IOException e) {
				logger.error("操作文件流关闭失败……");
				e.printStackTrace();
			}

		}
		return false;
	}

	public static void main(String[] args) {

		File folder = new File("D:/nginx-1.8.0/html/local/images/1hxhx2");
		List<File> listFiles = FileOpUtil.sortByName(folder);
		int size = listFiles.size();
		StringBuilder buf = new StringBuilder();
		buf.append("[");
		for (int i = 0; i < size; i++) {
			buf.append("\"" + listFiles.get(i).getName() + "\",");
			// System.out.println(listFiles.get(i).getName());
		}

		//
		// System.out.println(folder.exists());
		// String[] files = folder.list();
		// DataUtil.print(files);
		// StringBuffer buf = new StringBuffer();
		// buf.append("[");
		// for(int i = 0; i<files.length; i++){
		// buf.append("\""+files[i]+"\",");
		// }
		buf.append("]");
		System.out.println(buf.toString());

	}

}
