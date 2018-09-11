package com.cjhxfund.foundation.web.context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

import com.cjhxfund.foundation.annotation.InitData;
import com.cjhxfund.foundation.util.data.APIDocUtil;
import com.cjhxfund.foundation.util.data.InitBeanData;
import com.cjhxfund.foundation.web.controller.BaseController;
import com.cjhxfund.foundation.web.model.BaseModel;
import com.cjhxfund.foundation.web.service.BaseInit;
import com.cjhxfund.foundation.web.service.BaseSchedule;
import com.cjhxfund.foundation.web.service.IBaseSV;

@Service("lanhanProcessor")
public class LanhanProcessor implements BeanPostProcessor {

//	Set<String> beanList = new HashSet<String>();

	static {
		//容器初始化，编码出现null情况
		List<Object> initList = new ArrayList<Object>();
		APIDocUtil.beanHolder.put("BaseInit", initList);
		List<Object> taskList = new ArrayList<Object>();
		APIDocUtil.beanHolder.put("BaseSchedule", taskList);
		List<Object> controllerList = new CopyOnWriteArrayList<Object>();
		APIDocUtil.beanHolder.put("controller", controllerList);
	}
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		//必须标识是否context初始化完成，否则每次刷新bean，都会调用这两个方法
		if (SpringContext.isIniting) {
			//init必须在前面，否则在任务中调用初始化参数会出问题
			if (bean instanceof BaseInit) {
//				 System.out.println("BaseInit:" + beanName);
				//缓存初始化请求的bean信息，方便统一初始和监控
				InitData order = bean.getClass().getAnnotation(InitData.class);
				InitBeanData beanInfo = new InitBeanData(order.value(), bean);
				beanInfo.setDesc(order.desc());
				beanInfo.setBeanName(order.name());
				CommonCache.initBeanCache.put(order.name(), beanInfo);
				
				List<Object> beanList = APIDocUtil.beanHolder.get("BaseInit");
				beanList.add(bean);
			}
			else if (bean instanceof BaseSchedule) {
				List<Object> beanList = APIDocUtil.beanHolder.get("BaseSchedule");
				beanList.add(bean);
			}
			else if (bean instanceof BaseController) {
//				APIDocUtil.loadCtrlAnno(bean, beanName);
				List<Object> beanList = APIDocUtil.beanHolder.get("controller");
				beanList.add(bean);
			}
			else if (bean instanceof IBaseSV) {
				@SuppressWarnings("unchecked")
				IBaseSV<BaseModel> sv = (IBaseSV<BaseModel>) bean;
				sv.initMapper();
			}
//			else if (bean instanceof IBaseMapper) {
//				// // System.out.println("IBaseMapper:" + beanName);
//			}
		}
		return bean;
	}

}
