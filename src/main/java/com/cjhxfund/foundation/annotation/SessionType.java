package com.cjhxfund.foundation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口验证要求
 * @author JasonXie
 * @date 2013年5月5日
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SessionType {

    /** 登录态要求类型 ,默认为不需要登录*/
    SType value() default SType.NOT_COMPELLED;

    /** 对登录态的特殊描述，可以放到这里 */
    String desc() default "";

    public static enum SType {
        /** 必须要有登录态 */
        COMPELLED("必须要求有登录态"),
        /** 不需要登录态 */
        NOT_COMPELLED("不需要登录态"),
        /** 有登录态和没有登录态时都能正常使用，如果没有登录态会被当作游客处理 */
        DISPENSABLE("有登录态和没有登录态时都能正常使用，如果没有登录态会被当作游客处理"),
        /** 内部接口,需要IP认证 */
        INTERNAL_WITH_IP_AUTH("内部接口,需要IP认证"),
        /** 内部接口,需要签名认证 */
        INTERNAL_WITH_SIGN_AUTH("内部接口,需要签名认证"),
        
        /** 内部接口,需要token认证 */
        TOKEN_AUTH("内部接口,需要token认证");

        private String detail;

        private SType(String detail) {
            this.detail = detail;
        }

        @Override
        public String toString() {
            return detail;
        }
    }
}