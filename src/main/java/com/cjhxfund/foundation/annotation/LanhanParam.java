package com.cjhxfund.foundation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数说明
 * @author JasonXie
 * @date 2013年5月5日
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LanhanParam {

	/**
     * 参数名称，如username
     */
    String field();
    
    /**
     * 参数名称，如:用户名
     */
    String name();
    
	/**
     * 参数类型
     */
	ParamType type() default ParamType.String;
	
	/**需要验证的请求方法，当请求路径的方法名在其中时，就会检查，设置格式为，{方法名:是否必传}*/
	String[] checkMethod() default "";
	
	/**当该参数的value是一个json对象，则可以用这个来校验json对象里面的数据，可是为：paramName:paramType:required，比如：userId:Long:true**/
	String[] subParam() default "";
	
	 /**
     * 该参数是否是必须要传的，默认必须传
     */
    boolean required() default false;

    /**
     * 如果不是必须传，需要在这里说明参数的默认值
     */
    String defaultValue() default "";
    /**字符串最大长度，默认为0*/
    int maxLength() default -1;
    
    /**字符串最小长度，默认为0*/
    int minLength() default -1;
    
    /**数值最大值，默认为-1,不做判断*/
    int maxValue() default -1;
    
    /**数值最小值，默认为-1,不做判断*/
    int minValue() default -1;
    
    /**匹配模式：0：严格全部内容匹配，1：部分内容匹配*/
    int matchType() default 0;
    
    /**正则表达式*/
    String regex() default "";
    
    /** 和required 连用，可以选择性的限制传递参数，比如：links="id,name" required=true, 表示当前的参数  和id, name,必须传一个**/
    String links() default "";

    /**
     * 参数描述，用于详细说明参数格式
     */
    String[] desc() default "";

	/**
	 * 请求参数值的数据类型
	 */
	public static enum ParamType {
		String("字符串","String"), 
		Array("数组","Array"), 
		Integer("整数","Integer"), 
		Long("长整数","Long"), 
		Number("数字","Number"), 
		PositiveInt("正整数(>0整数)","PositiveInt"), 
		NegativeInt("负整数(<0整数)","NegativeInt"), 
		NaturalInt("自然数(>=0整数)","NaturalInt"),
		Float("浮点数","Float"),
		PositiveFloat("正浮点数","PositiveFloat"),
		NegativeFloat("负浮点数","NegativeFloat"), 
		Boolean("BOOL类型(仅true/y/1为真)","Boolean"), 
		File("文件","File"), 
		JsonObject("json对象","JsonObject"), 
		Enum("枚举类型","Enum");

		private String detail;
		private String name;

		private ParamType(String detail,String name) {
			this.detail = detail;
			this.name = name;
		}
		
		public String getName() {
			return name;
		}

		public String getDetail() {
			return detail;
		}
		
		public String toString(){
			return this.name+"["+this.detail+"]";
		}

	}
}
