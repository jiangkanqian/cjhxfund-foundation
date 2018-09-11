package com.cjhxfund.foundation.util.io;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class PathUtil {

	private PathUtil(){
		throw new Error("该类不能被实例化！");
	}
	
	public static void getPath() {
		// 方式一
		System.out.println("简单获取项目根目录：" + System.getProperty("user.dir"));
		// 方式二
		File directory = new File("");// 设定为当前文件夹
		try {
			System.out.println("获取标准的项目根路径: " + directory.getCanonicalPath());// 获取标准的路径
			System.out.println("获取项目根绝对路径: " + directory.getAbsolutePath());// 获取绝对路径
			// 方式三
			System.out.println(PathUtil.class.getSimpleName() + "所在的classes根目录" + PathUtil.class.getResource("/").toURI().getPath());
			System.out.println(PathUtil.class.getSimpleName() + "所在包位置:" + PathUtil.class.getResource("").toURI().getPath());
			// 方式4
			System.out.println("classes加载的根目录" + PathUtil.class.getClassLoader().getResource("").toURI().getPath());
			// System.out.println(PathUtil.class.getClassLoader().getResource("").toURI().PathUtil());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * classes加载的根目录
	 */
	public static String getRealClassPath(Class<?> clazz) {
		// URL urlpath =PathUtil.class.getClassLoader().getResource("");
		String path = null;
		try {
			path = clazz.getClassLoader().getResource("").toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}
	
	/**
	 * 获取该应用目录
	 * @return
	 */
	public static String getAppPath() {
		// URL urlpath =PathUtil.class.getClassLoader().getResource("");
		String path = null;
		try {
			path = PathUtil.class.getClassLoader().getResource("").toURI().getPath();
			path = path.split("WEB-INF")[0];
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}
	
	/**
	 * classes加载的根目录
	 */
	public static String getRealClassPath() {
		// URL urlpath =PathUtil.class.getClassLoader().getResource("");
		String path = null;
		try {
			path = PathUtil.class.getClassLoader().getResource("").toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}

	/**
	 * 读取class下的文件，即在sourceFolder的文件
	 * @param name
	 *            文件名 不包含路径 比如：html/menu.json
	 */
	public static String getSrcPath(String name) {
		String result = null;
		result = getRealClassPath() + name;
		return result;
	}

	/**
	 * 读取项目目录下的 args下的filename 如getParallelPath("json.txt","config");
	 * 
	 * @param filename
	 * @param folder
	 * @return
	 */
	// filename 文件名 不包含路径
	// ...args 文件夹名 可以输入多个文件夹名参数
	public static String getBaseDir(String folder,String fileName) {
		String pre = System.getProperty("user.dir");
		String path = pre+File.separator+folder+File.separator+fileName;
		return path;
	}

	public static String getPackagePath() {
		String result = null;
		try {
			result = PathUtil.class.getResource("").toURI().getPath();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// 获取WebRoot目录
	public static String getWebRootPath() {
		URL urlpath = PathUtil.class.getResource("");
		String path = urlpath.toString();
		if (path.startsWith("file")) {
			path = path.substring(5);
		}
		if (path.indexOf("WEB-INF") > 0) {
			path = path.substring(0, path.indexOf("WEB-INF") - 1);
		}
		path.replace("/", File.separator);
		return path;
	}

	// webroot WebRoot目录
	// filename 文件名
	// ...args 文件名所在文件夹，多个参数输入
	public static String getWebRootFilepath(String webroot, String filename, String... args) {
		String pre = webroot;
		String path = pre;
		for (String arg : args) {
			path += File.separator + arg;
		}
		path += File.separator + filename;
		if (path.startsWith("file")) {
			path = path.substring(5);
		}
		path.replace("/", File.separator);
		return path;
	}

	/**
	 * @param args
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws URISyntaxException {
		// TODO Auto-generated method stub
		// System.out.println(PathUtil.getSrcPath("html/menu.json"));
		// String src=PathUtil.getSrcPath("json.txt");
		 System.out.println(getPackagePath());
		// String url=PathUtil.getParallelPath("json.txt", "config");
		// // System.out.println(ReadFromFile.getComFile(src));
		// String content=ReadFromFile.getFileContent("html/menu.json");
		// System.out.println(content);
		// PathUtil();
//		String content = "123";
//		String fileName = "html/menu.json";
//		String url = getRealClassPath() + fileName;
//		System.out.println(url);
//		url=getBaseDir("logFiles","test.log");
//		System.out.println(url);
//		 content=ReadFileUtil.getFileContent(url);
//		// content=getSrcPath(url);
//		 System.out.println("con:"+content);
		// WriteUtil.writeFile(url, content, fileName);

	}

}