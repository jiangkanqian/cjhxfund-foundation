package com.cjhxfund.foundation.util.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author Xie Jason
 * @date 2015年1月17日
 * @提供的服务： 读取和创建文档
 * 
 */
//*@引入jar包：freemarker-2.3.20
public class FreeMarkerUtil {
	private static JLogger logger = SysLogger.getLogger(FreeMarkerUtil.class);

	private FreeMarkerUtil(){
		throw new Error("该类不能被实例化！");
	}
	
	/**
	 * 服务：创建文档
	 * @param 必须 dataMap : 数据源
	 * @param 必须 tempFolder：模板文件所在的文件夹
	 * @param 必须 tempName：模板文件名
	 * @param 必须 outFile：输出文件
	 */
	public static void createDoc(Map<String, Object> dataMap, File tempFolder, String tempName, File outFile) {
		Configuration configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
		Template temp = null;
		Writer out = null;
		try {
			configuration.setDirectoryForTemplateLoading(tempFolder);
			temp = configuration.getTemplate(tempName);
//			System.out.println(outFile.getPath());
			if(!outFile.getParentFile().exists()){
				outFile.getParentFile().mkdirs();
			}
			if (!outFile.exists()) {
				outFile.createNewFile();
			}
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));
			temp.process(dataMap, out);
			out.flush();
			out.close();
			logger.info(""+outFile.getName()+" 生成文件");
		} catch (TemplateException e) {
			logger.error(e);
		} catch (FileNotFoundException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	/**
	 * 服务：创建文档
	 * @param 必须 dataMap : 数据源
	 * @param 必须 tempFolder：模板文件所在的文件夹
	 * @param 必须 tempName：模板文件名
	 * @param 必须 outFile：输出文件
	 */
	public static void createDoc(Map<String, Object> dataMap, String tempFolder, String tempName, String outFilePath) {
		Configuration configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
		Template temp = null;
		Writer out = null;
		try {
			configuration.setDirectoryForTemplateLoading(new File(tempFolder));
			temp = configuration.getTemplate(tempName);
//			System.out.println(outFile.getPath());
			File outFile = new File(outFilePath);
			if(!outFile.getParentFile().exists()){
				outFile.getParentFile().mkdirs();
			}
			if (!outFile.exists()) {
				outFile.createNewFile();
			}
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));
			temp.process(dataMap, out);
			out.flush();
			out.close();
			logger.info(""+outFile.getName()+" 生成文件");
		} catch (TemplateException e) {
			logger.error(e);
		} catch (FileNotFoundException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	
	public static void writeHtml(Map<String, Object> dataMap, File tempFolder, String tempName, HttpServletResponse response) {
		Configuration configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
		Template temp = null;
		try {
			configuration.setDirectoryForTemplateLoading(tempFolder);
			temp = configuration.getTemplate(tempName);
			response.setContentType("text/html; charset=" + temp.getEncoding());
	        PrintWriter out = response.getWriter();
			temp.process(dataMap, out);
			out.flush();
			out.close();
			logger.info(""+tempName+" 生成文件");
		} catch (TemplateException e) {
			logger.error("模板有问题哦！请检查模板！" + e.getMessage());
		} catch (FileNotFoundException e) {
			logger.error("文件找不到！请检查路径是否正确！" + e.getMessage());
		} catch (IOException e) {
			logger.error("IO出异常啦！" + e.getMessage());
		}
	}
	
	public static void sendLoadDownFile(Map<String, Object> dataMap, File tempFolder, String tempName, HttpServletResponse response) {
		Configuration configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
		Template temp = null;
		try {
			configuration.setDirectoryForTemplateLoading(tempFolder);
			temp = configuration.getTemplate(tempName);
			response.setContentType("application/octet-stream");
//          response.addHeader("Content-Disposition", "attachment;filename="+DataUtil.urlEncodeUTF8(fileName));
	  		response.addHeader("Content-Disposition", "attachment;filename="+temp.getName());
			PrintWriter out = response.getWriter();
			temp.process(dataMap, out);
			out.flush();
			out.close();
			logger.info(tempName+" 生成文件");
		} catch (TemplateException e) {
			logger.error("模板有问题哦！请检查模板！" + e.getMessage());
		} catch (FileNotFoundException e) {
			logger.error("文件找不到！请检查路径是否正确！" + e.getMessage());
		} catch (IOException e) {
			logger.error("IO出异常啦！" + e.getMessage());
		}
	}
	
	
//	/**
//	 * 转换文件
//	 * @param fromFileInputStream: 
//	 * @import jodconverter-2.2.1
//	 * */
//	public String doc2Html(InputStream fromFileInputStream, File toFileFolder){
//		String soffice_host = AppConfig.getProperty(AppConfig.SOFFICE_HOST_KEY);
//		String soffice_port = AppConfig.getProperty(AppConfig.SOFFICE_PORT_KEY);
//		logger.debug("soffice_host:"+soffice_host+",soffice_port:"+soffice_port);
//
////		Date date = new Date();
////		SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmmss");
////		String timesuffix = sdf.format(date);
//		String timesuffix = "fda";
//		String htmFileName = "htmlfile"+timesuffix+".html";
//		String docFileName = "docfile"+timesuffix+".doc";
//
//		File htmlOutputFile = new File(toFileFolder.toString()+File.separatorChar+htmFileName);		
//		File docInputFile = new File(toFileFolder.toString()+File.separatorChar+docFileName);
//		logger.debug("########htmlOutputFile："+toFileFolder.toString()+File.pathSeparator+htmFileName);
//		/**
//		 * 由fromFileInputStream构建输入文件
//		 * */
//		try {
//			OutputStream os = new FileOutputStream(docInputFile);
//			int bytesRead = 0;
//			 byte[] buffer = new byte[1024 * 8];
//			while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
//					os.write(buffer, 0, bytesRead);
//			}
//
//			os.close();
//			fromFileInputStream.close();
//		} catch (IOException e) {
//			logger.error(e.getMessage(), e);
//		}
//
//		OpenOfficeConnection connection = new SocketOpenOfficeConnection(soffice_host,Integer.parseInt(soffice_port));
//		try {
//			connection.connect();
//		} catch (ConnectException e) {
//			 System.err.println("文件转换出错，请检查OpenOffice服务是否启动。");  
//			 logger.error(e.getMessage(), e);
//		}
//		// convert
//		DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
//		converter.convert(docInputFile, htmlOutputFile);
//		connection.disconnect();
///*		File  htmlOutputFile_rn = new File
//		(htmlOutputFile.getAbsolutePath().substring(0,htmlOutputFile.getAbsolutePath().lastIndexOf("."))+".htm");
//		htmlOutputFile.renameTo(htmlOutputFile_rn);
//		return htmlOutputFile_rn.getName();*/
//
//		//转换完之后删除word文件
//		docInputFile.delete();
//		logger.debug("删除上传文件："+docInputFile.getName());
//		return htmFileName;
//	}
//	
	public static void main(String[] args) {
//		testWord1();
	}

}
