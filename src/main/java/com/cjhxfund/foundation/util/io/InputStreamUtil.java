package com.cjhxfund.foundation.util.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * @author xiejs
 * @date 2015年6月10日
 */
public class InputStreamUtil {

	private InputStreamUtil(){
		throw new Error("该类不能被实例化！");
	}
	
	final static int BUFFER_SIZE = 4096;

	/**
	 * 将InputStream转换成String 默认utf-8编码
	 * @param in
	 *            InputStream
	 * @return String
	 * @throws Exception
	 * 
	 */
	public static String InputStreamTOString(InputStream in) {
		return InputStreamTOString(in, "UTF-8");
	}
	

	/**
	 * 将InputStream转换成某种字符编码的String
	 * 
	 * @param in
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static String InputStreamTOString(InputStream in, String encoding) {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		try {
			while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
				outStream.write(data, 0, count);
			String result=new String(outStream.toByteArray(), encoding);
			in.close();
			outStream.flush();
			outStream.close();
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
		
	}

	/**
	 * 将String转换成InputStream
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static InputStream StringTOInputStream(String in) {
		ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes(Charset.forName("UTF-8")));
		return is;
	}

	/**
	 * 将InputStream转换成byte数组
	 * 
	 * @param in
	 *            InputStream
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] InputStreamTOByte(InputStream in) {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		try {
			while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
				outStream.write(data, 0, count);
		} catch (IOException e) {
			e.printStackTrace();
		}

		data = null;
		return outStream.toByteArray();
	}

	/**
	 * 将byte数组转换成InputStream
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static InputStream byteTOInputStream(byte[] in) {
		ByteArrayInputStream is = new ByteArrayInputStream(in);
		return is;
	}
	
	public static byte[]  fileToByte(File file){
		return InputStreamTOByte(fileToInputStream(file));
	}
	
	public static byte[]  fileToByte(String file){
		return InputStreamTOByte(fileToInputStream(file));
	}
	
	public static InputStream fileToInputStream(File file){
		if(!file.exists()){
			return null;
		}
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return is;
	}
	
	public static InputStream fileToInputStream(String filePath){
		return fileToInputStream(new File(filePath));
	}

	/**
	 * 将byte数组转换成String
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static String byteTOString(byte[] in){
		InputStream is = byteTOInputStream(in);
		return InputStreamTOString(is);
	}
	
	public static void streamToFile(InputStream is, File file){
		try {
//			if(!file.exists()){
//				file.createNewFile();
//			}
			FileOutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[4096];
			while ((bytesRead = is.read(buffer, 0, 4096)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void streamToFile(InputStream is,String folder, String fileName){
		try {
//			if(!file.exists()){
//				file.createNewFile();
//			}
			File folderFile = new File(folder);
			if(!folderFile.exists()){
				folderFile.mkdirs();
			}
			fileName = folder + byteTOString(fileName.getBytes(System.getProperty("file.encoding")));
			
			FileOutputStream os = new FileOutputStream(new File(fileName));
			int bytesRead = 0;
			byte[] buffer = new byte[4096];
			while ((bytesRead = is.read(buffer, 0, 4096)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public static void streamToFile(InputStream is,String fileName){
		try {
			fileName =  byteTOString(fileName.getBytes(System.getProperty("file.encoding")));
			
			FileOutputStream os = new FileOutputStream(new File(fileName));
			int bytesRead = 0;
			byte[] buffer = new byte[4096];
			while ((bytesRead = is.read(buffer, 0, 4096)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	

}
