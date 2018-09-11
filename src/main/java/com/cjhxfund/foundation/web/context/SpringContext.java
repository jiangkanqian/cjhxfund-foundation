package com.cjhxfund.foundation.web.context;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;


/**
 * @author Jason
 * @date 2015年12月25日
 */
public final class SpringContext {

	public static ApplicationContext appContext;
	
//	public static ConfigurableApplicationContext appConfigContext;
	
	/**如:/admin*/
	public static String root = "";
	public static String basePackage = "";
	
	
	public static boolean isDebug = true;//系统运行环境，true:开发/测试环境，false:生产环境
	public static String requestModel = "auth";//访问模式:free：免登陆，auth:需要登陆，在开发的时候会用到
	
	public static String suffixName = ".action";

	public static boolean JunitTest = false;//如果是单元测试，则很多东西没必要加载
	public static boolean isIniting = true;//是否初始化话完毕,启动时默认会初始化
	public static String JunitTestModule = "";//如果是单元测试,则需标明当前是哪一个模块的单元测试，可以避免加载其他模块的非必要数据
	
	/**存放tomcat和系统的一些信息
	 * localIP,webappPath,rootPath,port
	 * */
	public final static Map<String,String> SYS_INFO = new HashMap<String,String>();
	

	/**存放tomcat和系统的一些信息
	 * localIP,webappPath,rootPath,port
	 * */
	public final static String getTomcatInfo(String key){
		return SYS_INFO.get(key);
	}
	
	public final static String getLocalIp(){
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 根据bean名称获取Bean, 没有获取到bean时，spring会报异常，将其捕捉，不抛出异常，返回null值
	 * @author Jason
	 * @date 2016年4月1日
	 * @param beanName
	 * @return
	 */
	public static Object getSpringBean(String beanName) {
		if (!StringUtils.isEmpty(beanName)) {
			try{
				return appContext.getBean(beanName);
			}
			catch(BeansException e){
//				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	public static <T> T getSpringBean(Class<T> class1) {
		if(null != class1){
			return appContext.getBean(class1);
		}
		return null;
	}
	
	/**
	 * 重载Spring context，所有的bean都将
	 * @author Jason
	 * @date 2016年4月1日
	 */
	public static void refreshContext(){
		((AbstractRefreshableApplicationContext)SpringContext.appContext).refresh();
	}
	
	/**
	 * 获取配置文件的上下文
	 * @return ConfigurableApplicationContext
	 */
	public static ConfigurableApplicationContext getConfigContext(){
		return  (ConfigurableApplicationContext)SpringContext.appContext;
	}
	
	/**
	 * 获取bean工厂，然后可以动态注入bean
	 * @author Jason
	 * @date 2016年4月1日
	 * @return
	 */
	private static DefaultListableBeanFactory getBeanFactory() {
        ConfigurableApplicationContext context = (ConfigurableApplicationContext)SpringContext.appContext;
        return (DefaultListableBeanFactory) context.getBeanFactory();
    }
	
	/**
	 * 建立bean，并添加相关属性
	 * @author Jason
	 * @date 2016年4月1日
	 * @param property 注入bean中的属性，可参考Spring xml 配置的bean格式
	 * @param clazz 注入的bean类型
	 * @return
	 */
	private static BeanDefinition buildBean(Map<String,String> property, Class<?> clazz) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        if(property != null && property.size()>0){
        	String key = "";
        	for(Entry<String, String> entry : property.entrySet()){
        		key = entry.getKey().trim();
        		if(key.startsWith("ref")){
        			builder.addPropertyReference(key.replace("ref-", "").trim(), entry.getValue().trim());
        		}
        		else{
        			builder.addPropertyValue(key, entry.getValue());
        		}
        	}
        }
        return builder.getBeanDefinition();
    }
	
	/**
	 * 动态注册bean
	 * @author Jason
	 * @date 2016年4月1日
	 * @param beanName 注入的bean名称
	 * @param property 注入bean中的属性，可参考Spring xml 配置的bean格式
	 * @param clazz 注入的bean类型
	 */
	public static Object registerBean(String beanName, Map<String,String> property, Class<?> clazz){
		BeanDefinition beanDefinition = buildBean(property, clazz);
		getBeanFactory().registerBeanDefinition(beanName, beanDefinition);
		return getSpringBean(beanName);
	}
	
	/**
	 * 取消bean的注入
	 * @param beanName bean名称
	 */
	public static void removeBean(String beanName){
		DefaultListableBeanFactory acf = getBeanFactory();
		if(acf.containsBean(beanName)) {
		    acf.removeBeanDefinition(beanName);
//		    System.out.println("删除成功! beanName:" + beanName);
		}
	}
	
}
