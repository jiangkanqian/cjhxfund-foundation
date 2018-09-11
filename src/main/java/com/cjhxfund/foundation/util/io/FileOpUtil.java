package com.cjhxfund.foundation.util.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.log.utils.CommUtil;

/**
 * 纯Java文件操作工具，支持文件、文件夹的复制、删除、移动操作。
 * 
 * @author Xie Jason
 * 
 * @date 2014年11月24日
 */
public class FileOpUtil{

	private static  JLogger logger = SysLogger.getLogger(FileOpUtil.class);

	private FileOpUtil() {
		throw new Error("该类不能被实例化！");
	}

	public static void main(String args[]) throws IOException {
		// delete(new File("C:/aaa"));
		// copy(new File("E:/pics/1.jpg"), new File("E:"));
		// move(new File("E:/pics/1.jpg"), new File("E:"));
		List<File> fileList = getListFiles("e:/pages", ".html", true);
		for(File file : fileList){
			String[] fileStr = file.getParent().split("\\\\");
			String parentFolder = fileStr[fileStr.length -1];
			System.out.println(parentFolder);
			System.out.println(file.getName());
			System.out.println(file.getAbsolutePath());
		}

	}
	
	

	/**
	 * 追加文件内容
	 * @param file
	 * @param conent
	 */
	public static void appendContent(String file, String conent){
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
			out.write(conent);
			logger.info(" [FileOpUtil] append file:"+file);
		} catch (IOException e) {
			logger.error(" [FileOpUtil] "+CommUtil.getExpStack(e));
		}
		finally{
			try {
				if(out != null){
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 删除文件（夹），如果是文件夹，会先递归删除其中的文件，然后再删除其文件夹
	 * 
	 * @param file
	 *            文件（夹）
	 */
	public static void delete(File file) {
		if (!file.exists()) {
			logger.info("要删除的文件不存在！filepath:" + file.getPath());
			return;
		}

		if (file.isFile()) {
			file.delete();
		} else {
			for (File f : file.listFiles()) {
				delete(f);
			}
			file.delete();
			logger.info(file.getPath() + " 文件删除成功！");
		}
	}

	/**
	 * 复制文件夹到一个目标文件夹
	 * 
	 * @param fromFolder
	 *            源文件夹
	 * @param toFolder
	 *            目标文件夹
	 * @throws IOException
	 *             异常时抛出
	 */
	public static void copy(File fromFolder, File toFolder) throws IOException {
		String objFolder = toFolder.getPath() + File.separator + fromFolder.getName();
		File _objFolderFile = new File(objFolder);
		_objFolderFile.mkdirs();
		// 递归文件到目标文件夹
		for (File sf : fromFolder.listFiles()) {
			copy(sf, new File(objFolder), null);
		}

	}

	/**
	 * 复制文件到一个目标文件夹
	 * 
	 * @param fromFile
	 *            源文件
	 * @param toFile
	 *            目标文件夹
	 * @param fileName
	 *            目标文件名 如果为空，则保持原文件名
	 * @throws IOException
	 *             异常时抛出
	 */
	public static void copy(File fromFile, File toFile, String fileName) throws IOException {
		if (!fromFile.exists() || !fromFile.isFile()) { // 如果目标文件不存在，或者是文件夹
			logger.info(" [FileOpUtil] 要拷贝的源文件不存在！filePath:" + fromFile.getPath());
			return;
		}
		if (!toFile.exists()) {
			toFile.mkdirs();
		}
		File objFile = null;
		if (null == fileName || "".equals(fileName)) { // 如果目标文件为空，则保持原文件名称
			fileName = fromFile.getName();
		}
		objFile = new File(toFile.getPath() + File.separator + fileName);
		// 复制文件到目标地
		InputStream ins = new FileInputStream(fromFile);
		FileOutputStream outs = new FileOutputStream(objFile);
		byte[] buffer = new byte[1024 * 512];
		int length;
		while ((length = ins.read(buffer)) != -1) {
			outs.write(buffer, 0, length);
		}
		ins.close();
		outs.flush();
		outs.close();
		logger.info("文件 拷贝到 文件夹下面 from :" + fromFile.getPath() + ",to:" + toFile.getPath());
	}

	/**
	 * 将文件移动到另外一个文件夹
	 * 
	 * @param fromFile
	 *            源文件
	 * @param toFile
	 *            目标文件夹
	 * @param fileName
	 *            目标文件名
	 * @throws IOException
	 *             异常时抛出
	 */
	public static void move(File fromFile, File toFile, String fileName) throws IOException {
		copy(fromFile, toFile, fileName);
		delete(fromFile);
	}

	/**
	 * 将文件夹移动到另外一个文件夹
	 * 
	 * @param fromFolder
	 *            源文件夹
	 * @param toFolder
	 *            目标文件夹
	 * @throws IOException
	 *             异常时抛出
	 */
	public static void move(File fromFolder, File toFolder) throws IOException {
		copy(fromFolder, toFolder);
		delete(fromFolder);
	}

	/**
	 * @param path
	 *            文件路径
	 * @param suffix
	 *            后缀名, 为空则表示所有文件
	 * @param isdepth
	 *            是否遍历子目录
	 * @return list
	 */
	public static List<File> getListFiles(String path, String suffix, boolean isdepth) {
		List<File> lstFiles = new ArrayList<File>();
		File file = new File(path);
		return listFile(lstFiles, file, suffix, isdepth);
	}

	private static List<File> listFile(List<File> lstFiles, File f, String suffix, boolean isdepth) {
		// 若是目录, 采用递归的方法遍历子目录
		if (f.isDirectory()) {
			File[] t = f.listFiles();

			for (int i = 0; i < t.length; i++) {
				if (isdepth || t[i].isFile()) {
					listFile(lstFiles, t[i], suffix, isdepth);
				}
			}
		} else {
			String filePath = f.getAbsolutePath();
			if (!StringUtils.isEmpty(suffix) && filePath.endsWith(suffix)) {
				lstFiles.add(f);
			} else {
				lstFiles.add(f);
			}
		}
		return lstFiles;
	}

	/**
	 * 获取获取文件修改时间
	 * @param fileFolder
	 * @return
	 */
	public static List<File> sortByName(File fileFolder) {
		File[] files = fileFolder.listFiles();
		if (files == null) {
			return null;
		}
		List<File> fileList = new ArrayList<File>();
		for (File f : files) {
			fileList.add(f);
		}

		Collections.sort(fileList, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				// if (o1.isDirectory() && o2.isFile())
				// return -1;
				// if (o1.isFile() && o2.isDirectory())
				// return 1;
				// o1.getAbsoluteFile().lastModified()
				return o2.lastModified() > o1.lastModified() ? 1 : -1;
			}
		});

		return fileList;
	}
}
