package com.cjhxfund.foundation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cjhxfund.foundation.web.model.CodeTipMsg;

/**
 * API返回的数据结构
 * @author Jason
 * @date 2013年5月5日
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ReturnData {

    String[] value() default "";
    
    String desc() default "";
    
    CodeTipMsg opFail() default CodeTipMsg.COMMON_FAIL;
    
    CodeTipMsg opOk() default CodeTipMsg.COMMON_SUCCESS;
    
    String okTip() default "";
    
    String failTip() default "";
    
    String dataType() default "json";
    
    String modelPath() default "";//返回字段说明
    
    String fileUrl() default "";//附件文件

}
