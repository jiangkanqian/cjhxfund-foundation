package com.cjhxfund.foundation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 启动时，按顺序加载，需要添加该注解，数字越大，越往后加载，
 * 如果没有，则说明和顺序无关，系统启动时自动加载
 * @author JasonXie
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InitData {

	/**加载顺序，默认-1，即和顺序无关*/
	int value() default -1;
	
	String name();
	
	String desc() default "";
}
