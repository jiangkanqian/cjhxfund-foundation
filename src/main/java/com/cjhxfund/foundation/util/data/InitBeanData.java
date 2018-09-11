package com.cjhxfund.foundation.util.data;

import com.cjhxfund.foundation.web.service.BaseInit;

/**
 * 初始化数据缓存的时候，需要将要初始化的Bean存放进来，然后根据要加载的顺序，按顺序加载 主要目的就是为了按顺序加载缓存数据
 * 
 * @author xiejiesheng
 *
 */
public class InitBeanData implements Comparable<InitBeanData> {

	private int orderNo;// 顺序编号
	private Object bean;// Spring 对应的Bean
	private String beanName;
	private String desc;

	public InitBeanData cloneValue() {
		InitBeanData data = new InitBeanData(this.orderNo, this.beanName, this.desc);
		return data;
	}
	
	public InitBeanData(int orderNo, String beanName, String desc) {
		this.orderNo = orderNo;
		this.beanName = beanName;
		this.desc = desc;
	}
	
	public InitBeanData(int orderNo, Object bean) {
		this.orderNo = orderNo;
		this.bean = bean;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public BaseInit getInitBean() {
		return (BaseInit) bean;
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
	public int compareTo(InitBeanData bean) {
		if (bean != null) {
			if (this.getOrderNo() > bean.getOrderNo()) {
				return 1;
			} else if (this.getOrderNo() == bean.getOrderNo()) {
				return 0;
			}
		}
		return -1;
	}

	@Override
	public String toString() {
		return "InitBeanData [orderNo=" + orderNo + ", beanName=" + beanName + ", desc=" + desc + "]";
	}
	
}
