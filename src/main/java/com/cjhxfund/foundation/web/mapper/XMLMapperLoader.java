package com.cjhxfund.foundation.web.mapper;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.util.data.DataUtil;
import com.cjhxfund.foundation.web.context.CommonCache;
import com.cjhxfund.foundation.web.context.SpringContext;

@Service("xmlMapperLoader")
public class XMLMapperLoader {

	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	private static  JLogger logger = SysLogger.getLogger();
	
	public void reload() throws Exception {
		if(null != CommonCache.configJson && null != CommonCache.configJson.get("mapperBasePackage")){
			String mapperBasePackage = CommonCache.configJson.get("mapperBasePackage").getAsString();
			//内容如：sqlSessionFactory:mapperConfig,testFactory:testConfig，可以有多个factory和mapper文件映射
			String[] mapperArr = mapperBasePackage.split(",");
			for(String mapper : mapperArr){
				String[] config = mapper.trim().split(":");
				reload(config[0], config[1]);
			}
		}
		else{
			//默认的配置
			reload("sqlSessionFactory", "mapperConfig");
		}
	}
	
	public void reload(String sqlSessionFactory, String configFolder ) throws Exception {
		// 获取baseMapper basePackage中的各个指定模块包名，比如：com.cjhxfund.web.sysmgr.mapper
		// String[] basePackages = StringUtils.tokenizeToStringArray(XMLMapperLoader.this.basePackage,
		// ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
		if(DataUtil.isEmptyStr(sqlSessionFactory) || DataUtil.isEmptyStr(configFolder)){
			logger.info("sqlSessionFactory or mapperconfig 为空！");
			return;
		}
		//获取所有class
//		Resource[] resArr = resourcePatternResolver.getResources("classpath*:com/cjhxfund/**/*.class");
		String XML_RESOURCE_PATTERN = "classpath*:"+configFolder+"/*.xml";
		Resource[] resources = resourcePatternResolver.getResources(XML_RESOURCE_PATTERN);
//		String resourcePattern = "**/*.class";
		if (resources != null && resources.length > 0) {
			SqlSessionFactory factory = (SqlSessionFactory) SpringContext.getSpringBean(sqlSessionFactory);
			if(null == factory){
				logger.info("sqlSessionFactory 的ID 不对，找不到对应的bean！");
				return;
			}
			Configuration configuration = factory.getConfiguration();

			removeConfig(configuration);

			for (Resource res : resources) {
				XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(res.getInputStream(), configuration, res.toString(),
						configuration.getSqlFragments());
				xmlMapperBuilder.parse();
			}
		}
		
//		ClassLoader ccLoader = ClasspathHelper.contextClassLoader();
////		Resource[] resArr = resourcePatternResolver.getResources("classpath*:com/cjhxfund/**/*.class");
//		
//		Enumeration<URL> resUrls = ccLoader.getResources("com/cjhxfund/**/*.class");
//		Set<Resource> result = new LinkedHashSet<Resource>(16);
//		while(resUrls.hasMoreElements()){
//			URL url = (URL) resUrls.nextElement();
//			result.add(new UrlResource(url));
//		}
//		Resource[] resArr = (Resource[])result.toArray(new Resource[result.size()]);
//		String resourcePattern = "**/*.class";
//		for (Resource res : resArr) {
//			System.out.println(res.getFilename());
//		}
////		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);  
////		Set<BeanDefinition> beanDefinitions = provider.findCandidateComponents("com/cjhxfund");  
////		for(BeanDefinition beanDefinition : beanDefinitions) {  
////		    System.out.println(beanDefinition.getBeanClassName()   
////		                    + "\t" + beanDefinition.getResourceDescription()  
////		                    + "\t" + beanDefinition.getClass());  
////		} 
	}

	private void removeConfig(Configuration configuration) throws Exception {
		Class<?> classConfig = configuration.getClass();
		clearMap(classConfig, configuration, "mappedStatements");
		clearMap(classConfig, configuration, "caches");
		clearMap(classConfig, configuration, "resultMaps");
		clearMap(classConfig, configuration, "parameterMaps");
		clearMap(classConfig, configuration, "keyGenerators");
		clearMap(classConfig, configuration, "sqlFragments");
		clearSet(classConfig, configuration, "loadedResources");

	}

	private void clearMap(Class<?> classConfig, Configuration configuration, String fieldName) throws Exception {
		Field field = classConfig.getDeclaredField(fieldName);
		field.setAccessible(true);
		Map<?, ?> mapConfig = (Map<?, ?>) field.get(configuration);
		mapConfig.clear();
	}

	private void clearSet(Class<?> classConfig, Configuration configuration, String fieldName) throws Exception {
		Field field = classConfig.getDeclaredField(fieldName);
		field.setAccessible(true);
		Set<?> setConfig = (Set<?>) field.get(configuration);
		setConfig.clear();
	}

}
