package test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;





import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.cjhxfund.foundation.util.io.CmdUtil;
import com.cjhxfund.foundation.util.io.PathUtil;
import com.cjhxfund.foundation.util.io.XMLUtil;

public class TestXml {

	public static void main(String[] args) throws Exception {
//		String content = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><ns2:test xmlns:ns2=\"http://testAPI.com.cjhxfund.agent.ws/\"><userName>cjhxTrader</userName><password>202cb962ac59075b964b07152d234b70</password></ns2:test></soap:Body></soap:Envelope>";
		
//		Map<String,String> data = parseXML2(content);
//		System.out.println(data);
		
		String path = "E:/svn/src/IPS/cjhxfund-ips-deploy/src/main/resources/applicationContext.xml";
		NodeList nodes = XMLUtil.getNodeList(path, "context:component-scan");
		Element element = (Element) nodes.item(0);
		String basePackage = element.getAttribute("base-package");
		basePackage = basePackage.replace(".", "/").replace("//", "/").replace("*", "");
		System.out.println(basePackage);
		
			
	}
	
	public static Map<String,String> parseXML2(String xml) throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException {
		Map<String,String> container = new HashMap<String,String>();
		if(xml.startsWith("<") && xml.endsWith(">")){
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder builder  = factory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
			NodeList nodes = doc.getChildNodes();
//			NodeList nodes = doc.getElementsByTagName("body");
			System.out.println(nodes.getLength());
			System.out.println(nodes.item(0).getNodeName());
			System.out.println(nodes.item(0).getNodeValue());
			System.out.println(nodes.item(0).getTextContent());
			nodes = nodes.item(0).getChildNodes();
			if(null == nodes || nodes.getLength() == 0){
				return null;
			}
			System.out.println(nodes.item(0).getNodeName());
			System.out.println(nodes.item(0).getNodeValue());
			System.out.println(nodes.getLength());
			System.out.println(nodes.item(0).getTextContent());
			
			for(int i = 0; i < nodes.getLength(); i++) {
				Element element=(Element) nodes.item(i);
				NamedNodeMap attrs=element.getAttributes();
				for(int j = 0; j < attrs.getLength(); j++) {
					Node node=attrs.item(j);
					String name=node.getNodeName();
					String value=node.getNodeValue();
					container.put(name, value);
				}
			}
		}
		return container;
	}
	
//	public static Map<String,String> parseXML(String xml) throws DocumentException, UnsupportedEncodingException{
//		Map<String,String> container = new HashMap<String,String>();
//		if(xml.startsWith("<") && xml.endsWith(">")){
//			SAXReader reader = new SAXReader();
//			Document document = reader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
//			Element root = document.getRootElement();
//			// 得到根元素的所有子节点
//			@SuppressWarnings("unchecked")
//			List<Element> elementList = root.elements();
//			Element body = elementList.get(0).getDocument().getRootElement();
//			@SuppressWarnings("unchecked")
//			List<Element>  elemList = body.elements();
//			// 遍历所有子节点
//			for (Element e : elemList){
//				container.put(e.getName(), e.getText());
//			}
//		}
//		return container;
//	}
}
