package com.cjhxfund.foundation.util.io;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;

public class ReadFileUtil {
	
//	private static JLogger logger = SysLogger.getLogger(ReadFileUtil.class);

	protected static JLogger logger = SysLogger.getLogger();
	
	private ReadFileUtil() {
		throw new Error("该类不能被实例化！");
	}

	/**
	 * 读取class 文件下的文件内容
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getConfigFile(String fileName) {
		String content = null;
		String url = PathUtil.getSrcPath(fileName);
		content = getFileContent(new File(url));
		return content;
	}

	public static String getConfigFile(String fileName, String charset) {
		String content = null;
		String url = PathUtil.getSrcPath(fileName);
		content = getFileContent(new File(url), charset);
		return content;
	}

	/*
	 * 读取项目根目录下文件夹folder 下的fileName 文件
	 */
	public static String getBaseFile(String folder, String fileName) {
		String content = null;
		String pre = System.getProperty("user.dir");
		String path = pre + File.separator + folder + File.separator + fileName;
		content = getFileContent(new File(path));
		return content;
	}

	public static String getFileContent(String file) {
//		return getContent(new File(file), System.getProperty("file.encoding"));
		return getContent(new File(file), "UTF-8");
	}

	public static String getFileContent(String file, String charset) {
		return getContent(new File(file), charset);
	}

	public static String getFileContent(File file) {
//		return getContent(file, System.getProperty("file.encoding"));
		return getContent(file, "UTF-8");
	}

	public static String getFileContent(File file, String charset) {
		return getContent(file, charset);
	}

	/**
	 * 获取jarName jar包里面的文件
	 * @param jarName
	 * @param modelPath 如config/test.json
	 * @return
	 */
	public static String getJarContent(String jarName, String path) {
		InputStream is = null;
		ByteArrayOutputStream out = null;
		try {
			URL url = new URL("jar:file:" + jarName + "!/" + path + "");
//			System.out.println(url);
			is = url.openStream();
			out = new ByteArrayOutputStream(is.available());
			byte[] b = new byte[1000];
			int n;
			while ((n = is.read(b)) != -1) {
				out.write(b, 0, n);
			}

			return InputStreamUtil.byteTOString(out.toByteArray());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e1) {
				logger.error("reader关闭失败！");
			}
		}
		return null;
	}

	private static String getContent(File file, String charset) {
		// FileReader fr=null;
		if(!file.exists()){
			logger.error("Can not find the File:"+file.getAbsolutePath());
			return "";
		}
		StringBuffer js = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				js.append(tempString + "\r\n");
			}
			return js.toString();
			// System.out.println(js.toString());
		} catch (FileNotFoundException e) {
			logger.error("文件找不到！file:" + file.getName()+", msg:"+ e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					logger.error("reader关闭失败！");
				}
			}
		}
		return null;
	}

	/**
	 * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
	 */
	public static void readFileByBytes(String fileName) {
		File file = new File(fileName);
		InputStream in = null;
		try {
			System.out.println("以字节为单位读取文件内容，一次读一个字节：");
			// 一次读一个字节
			in = new FileInputStream(file);
			int tempbyte;
			while ((tempbyte = in.read()) != -1) {
				System.out.write(tempbyte);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			System.out.println("以字节为单位读取文件内容，一次读多个字节：");
			// 一次读多个字节
			byte[] tempbytes = new byte[100];
			int byteread = 0;
			in = new FileInputStream(fileName);
			ReadFileUtil.showAvailableBytes(in);
			// 读入多个字节到字节数组中，byteread为一次读入的字节数
			while ((byteread = in.read(tempbytes)) != -1) {
				System.out.write(tempbytes, 0, byteread);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	/**
	 * 以字符为单位读取文件，常用于读文本，数字等类型的文件
	 */
	public static void readFileByChars(String fileName) {
		File file = new File(fileName);
		Reader reader = null;
		try {
			System.out.println("以字符为单位读取文件内容，一次读一个字节：");
			// 一次读一个字符
			reader = new InputStreamReader(new FileInputStream(file));
			int tempchar;
			while ((tempchar = reader.read()) != -1) {
				// 对于windows下，\r\n这两个字符在一起时，表示一个换行。
				// 但如果这两个字符分开显示时，会换两次行。
				// 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
				if (((char) tempchar) != '\r') {
					System.out.print((char) tempchar);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			System.out.println("以字符为单位读取文件内容，一次读多个字节：");
			// 一次读多个字符
			char[] tempchars = new char[30];
			int charread = 0;
			reader = new InputStreamReader(new FileInputStream(fileName));
			// 读入多个字符到字符数组中，charread为一次读取字符数
			while ((charread = reader.read(tempchars)) != -1) {
				// 同样屏蔽掉\r不显示
				if ((charread == tempchars.length) && (tempchars[tempchars.length - 1] != '\r')) {
					System.out.print(tempchars);
				} else {
					for (int i = 0; i < charread; i++) {
						if (tempchars[i] == '\r') {
							continue;
						} else {
							System.out.print(tempchars[i]);
						}
					}
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	public static List<String> readLines(InputStream is) {
		List<String> lines = null;
		try {
			lines = IOUtils.readLines(is, Charset.defaultCharset());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return lines;
	}

	public static List<String> readLines(File file, String encoding) {
		try {
			return FileUtils.readLines(file, encoding);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static List<String> readLines(File file) {
		return readLines(file, "utf-8");
	}

	/**
	 * 随机读取文件内容
	 */
	public static void readFileByRandomAccess(String fileName) {
		RandomAccessFile randomFile = null;
		try {
			System.out.println("随机读取一段文件内容：");
			// 打开一个随机访问文件流，按只读方式
			randomFile = new RandomAccessFile(fileName, "r");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 读文件的起始位置
			int beginIndex = (fileLength > 4) ? 4 : 0;
			// 将读文件的开始位置移到beginIndex位置。
			randomFile.seek(beginIndex);
			byte[] bytes = new byte[10];
			int byteread = 0;
			// 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
			// 将一次读取的字节数赋给byteread
			while ((byteread = randomFile.read(bytes)) != -1) {
				System.out.write(bytes, 0, byteread);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	/**
	 * 显示输入流中还剩的字节数
	 */
	private static void showAvailableBytes(InputStream in) {
		try {
			System.out.println("当前字节输入流中的字节数为:" + in.available());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File[] getFolderFiles(String path) {
		File file = new File(path);
		File[] tempList = file.listFiles();
		// log.debug(path + "; 该目录下对象个数：" + tempList.length);
		return tempList;
	}

	public static void main(String[] args) throws IOException {
		String fileName = "G:/项目管理/开发所需材料/改进材料 600X400/爱吃凉菜";
		String fileName2 = "G:/项目管理/开发所需材料/改进材料 600X400/爱吃凉菜/temp";
		// ReadFileUtil.readFileByBytes(fileName);
		// ReadFileUtil.readFileByChars(fileName);
		// ReadFileUtil.readFileByLines(fileName);
		// ReadFileUtil.readFileByRandomAccess(fileName);

		File[] tempList = getFolderFiles(fileName);
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				System.out.println("文     件：" + tempList[i].getPath());
				FileOpUtil.copy(tempList[i], new File(fileName2), (i + 1) + ".jpg");
			}
			if (tempList[i].isDirectory()) {
				System.out.println("文件夹：" + tempList[i]);
			}
		}
		System.out.println(File.separator);
	}

	public static byte[] getBytesFromFile(File f) {
		if (f == null) {
			return null;
		}
		FileInputStream stream = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
		try {
			stream = new FileInputStream(f);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1) {
				out.write(b, 0, n);
			}

			stream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 获取clazz 所在的jar包里面的文件
	 * @param clazz
	 * @param modelPath 如config/test.json
	 * @return
	 */
	public static String getJarContent(Class<?> clazz, String modelPath) {
		String jar = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
		return getJarContent(jar, modelPath);
	}
}
