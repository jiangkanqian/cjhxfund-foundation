package com.cjhxfund.foundation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * api注解类型，主要针对controller，service和mapper
 * @author JasonXie 
 * @date 2015年5月5日
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface APIType {

	public enum Type {
		/**springmvc 调用的controller接口*/
		CONTROLLER, 
		/**系统内部的service接口*/
		SERVICE, 
		/**mybatis对应xml的mapper接口*/
		MAPPER
	};

	Type value() default Type.CONTROLLER;// 默认是controller的api
}
