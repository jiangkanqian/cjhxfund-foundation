package com.cjhxfund.foundation.util.io;

import java.awt.Desktop;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cjhxfund.foundation.util.data.DataUtil;
import com.thoughtworks.xstream.core.util.Base64Encoder;

/**
 * @author Jason
 * @date 2016年4月3日
 */
public class HtmlUtil {

	/** 
	 * 获取指定HTML标签的指定属性的值 
	 * @param source 要匹配的源文本 
	 * @param element 标签名称 
	 * @param attr 标签的属性名称 
	 * @return 属性值列表 
	 */
	public static List<String> match(String source, String element, String attr) {
		List<String> result = new ArrayList<String>();
		String reg = "<" + element + "[^<>]*?\\s" + attr + "=['\"]?(.*?)['\"]?\\s.*?>";
		Matcher m = Pattern.compile(reg).matcher(source);
		while (m.find()) {
			String r = m.group(1);
			result.add(r);
		}
		return result;
	}

	/**
	 * 根据正则表达式，获取指定内容
	 * @param regex 正则表达式 比如：<body>.*?</body>
	 * @param content 要用正则获取的原文
	 * @return
	 */
	public static String getRegexContent(String regex, String content) {
		content = content.replaceAll("\r\n", "换行符合").replaceAll("\\\\", "/");
		Pattern pt = Pattern.compile(regex);
		Matcher mt = pt.matcher(content);
		String result = "";
		while (mt.find()) {
			result = mt.group().replaceAll("换行符合", "\r\n");
		}
		return result;
	}

	/** 
	 * 解析base64，返回图片所在路径 
	 * @param base64Info 
	 * @return 
	 */
	public static String decodeBase64(String base64Info, String filePath) {
		if (DataUtil.isEmptyStr(base64Info)) {
			return null;
		}
		
		Base64Encoder decoder = new Base64Encoder();
		String[] arr = base64Info.split("base64,");
		// 数据中：data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABI4AAAEsCAYAAAClh/jbAAA ... 在"base64,"之后的才是图片信息
//		String picPath = filePath + "/" + UUID.randomUUID().toString() + ".png";
		try {
			byte[] buffer = decoder.decode(arr[1]);
			OutputStream os = new FileOutputStream(filePath);
			os.write(buffer);
			os.close();
		} catch (IOException e) {
			throw new RuntimeException();
		}

		return filePath;
	}
	
	
	/** 
     * 打开默认浏览器访问页面 
     */  
    public static void openDefaultBrowser(String url){  
        //启用系统默认浏览器来打开网址。  
        try {  
            URI uri = new URI(url);  
            Desktop.getDesktop().browse(uri);  
        } catch (URISyntaxException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  

	public static void main(String[] args) {
//		String content = ReadFileUtil.getFileContent("e:/dict.html").toLowerCase();
//		List<String> ids = HtmlUtil.match(content, "div", "id");
//		DataUtil.print(ids);
		openDefaultBrowser("http://localhost:8088/windData/logger.html");
	}
}
