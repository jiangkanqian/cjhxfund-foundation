package com.cjhxfund.foundation.web.model;

public class BeanInfo {

	private Object bean;
	
	private String beanName;
	private String desc;
	
	public BeanInfo(Object bean, String beanName) {
		this.bean = bean;
		this.beanName = beanName;
	}
	public Object getBean() {
		return bean;
	}
	public void setBean(Object bean) {
		this.bean = bean;
	}
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		return "BeanInfo [beanName=" + beanName + ", desc=" + desc + "]";
	}
	
	
}
