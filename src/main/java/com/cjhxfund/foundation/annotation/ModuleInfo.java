package com.cjhxfund.foundation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author JasonXie
 * @date 2015年7月3日
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModuleInfo {

	String name() default "";
	String value() default "";
	
	String nameCn() default "";
	
	String subModule() default "";
	
	String subModName() default "";
	
//	String jar() default "";
	
}
