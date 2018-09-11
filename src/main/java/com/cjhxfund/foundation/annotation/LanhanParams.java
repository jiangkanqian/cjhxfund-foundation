package com.cjhxfund.foundation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数说明，比如：{"name:名字","age:年龄"}
 * @author xiejiesheng 
 * @date 2013年5月5日
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LanhanParams {

	LanhanParam[] value() default {};
	
	/**用于描述统一参数说明*/
	String desc() default "";
	
	/**可以自定义请求对象模型*/
	String beanName() default "";

	
	

}
