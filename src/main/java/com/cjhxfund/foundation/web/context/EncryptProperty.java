package com.cjhxfund.foundation.web.context;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;


/***
 * 解析加密的properties配置文件，并且重新赋值到spring配置文件
 * @author chenkai
 * date:2016-11-8
 */
public class EncryptProperty extends PropertyPlaceholderConfigurer implements InitializingBean {

	@Override
	protected String convertProperty(String name, String value) {
		if(name.equals("jdbc.username")){
			value = value.replace("123", "");
		}
		if(name.equals("jdbc.password")){
			value = value.replace("123", "");
		}
		return super.convertProperty(name, value);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}
