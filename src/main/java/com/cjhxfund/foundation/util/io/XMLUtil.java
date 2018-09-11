package com.cjhxfund.foundation.util.io;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLUtil {

	/**
	 * 获取xml中的某个节点或某类节点
	 * @param fileName 文件路径
	 * @param nodeName 节点名称
	 * @return
	 */
	public static NodeList getNodeList(String fileName, String nodeName){
		File file=new File(fileName);
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc;
		NodeList nodes = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(file);
			nodes=doc.getElementsByTagName(nodeName);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nodes;
	}
	
	/**
	 * 获取xml中的某个节点或某类节点
	 * @param file 文件
	 * @param nodeName 节点名称
	 * @return
	 */
	public static NodeList getNodeList(File file, String nodeName){
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc;
		NodeList nodes = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(file);
			nodes=doc.getElementsByTagName(nodeName);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nodes;
	}
	
	
	
	
}
