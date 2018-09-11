package com.cjhxfund.foundation.util.data;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cjhxfund.foundation.annotation.Author;
import com.cjhxfund.foundation.annotation.InitData;
import com.cjhxfund.foundation.annotation.LanhanParam;
import com.cjhxfund.foundation.annotation.LanhanParam.ParamType;
import com.cjhxfund.foundation.annotation.LanhanParams;
import com.cjhxfund.foundation.annotation.ReturnData;
import com.cjhxfund.foundation.annotation.SessionType;
import com.cjhxfund.foundation.annotation.SessionType.SType;
import com.cjhxfund.foundation.annotation.TaskSchedule;
import com.cjhxfund.foundation.annotation.data.DocData;
import com.cjhxfund.foundation.annotation.data.ParamConfig;
import com.cjhxfund.foundation.annotation.data.ParamItem;
import com.cjhxfund.foundation.log.constants.StartLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.util.io.PathUtil;
import com.cjhxfund.foundation.util.io.ReadFileUtil;
import com.cjhxfund.foundation.web.context.CommonCache;
import com.cjhxfund.foundation.web.context.MyClassLoader;
import com.cjhxfund.foundation.web.context.SpringContext;
import com.cjhxfund.foundation.web.controller.BaseController;
import com.cjhxfund.foundation.web.model.APIMonitor;
import com.cjhxfund.foundation.web.model.BeanInfo;
import com.cjhxfund.foundation.web.model.CtrlBean;
import com.cjhxfund.foundation.web.model.HttpCtrl;
import com.cjhxfund.foundation.web.service.BaseInit;
import com.cjhxfund.foundation.web.service.BaseSchedule;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author xiejs
 * @date 2015年7月3日
 */
public class APIDocUtil {

	private static JLogger logger = StartLogger.getLogger();

	/**key：beanType,value: bean*/
	public static final  Map<String, List<Object>> beanHolder = new ConcurrentHashMap<String, List<Object>>();
	
	public static void initCtrlAnno() throws Exception {
		List<Object> beans = APIDocUtil.beanHolder.get("controller");
		if(DataUtil.isValidList(beans)){
			String rootName = SpringContext.root.replace("/", "");
			String apiNo = "";
			int i = 1;
			boolean isReload = false;
			for(Object bean : beans){
				apiNo = getApiNo(rootName, i);
				String beanName = DataUtil.lowerFirestChar(bean.getClass().getSimpleName());
				BeanInfo beanInfo = loadCtrlAnno(bean, beanName, rootName, apiNo, isReload);
				i++;
				if(!SpringContext.isDebug){
					bean =  addAction(bean, beanName);
					//重新设置bean
					beanInfo.setBean(bean);
				}
			}
			
			if(!SpringContext.isDebug){
				//刷新controller
				for(Entry<String,CtrlBean> entry : CommonCache.ctrlBeanCache.entrySet()){
					entry.getValue().setControllerBean();
				}
			}
			logger.info("总共有controller:" + beans.size() + "个，有API：" + CommonCache.ctrlBeanCache.size() + "个");
		}
		else{
			logger.info("没有获取到controller!");
			System.exit(0);
		}
	}
	
	/** 重新加载控制层注解信息 */
	public static void reloadCtrlAnno() {
		long t1 = System.currentTimeMillis();
		List<Object> beans = new ArrayList<Object>(SpringContext.appContext.getBeansOfType(BaseController.class, false, true).values());
		int beanCount = beans.size();
		String rootName = SpringContext.root.replace("/", "");
		String apiNo = "";
		int i = 1;
		boolean isReload = true;
		try {
			for(Object bean : beans){
				//重新刷新controller 层的bean
				String beanName = DataUtil.lowerFirestChar(bean.getClass().getSimpleName());
				SpringContext.removeBean(beanName);
				SpringContext.registerBean(beanName, null, bean.getClass());
				apiNo = getApiNo(rootName, i);
				loadCtrlAnno(bean, null, rootName, apiNo,isReload);
				i++;
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		APIDocUtil.beanHolder.clear();
		logger.info("加载注解信息完成，消耗时间为：" + (System.currentTimeMillis() - t1) + "ms");
		logger.info("总共有controller:" + beanCount + "个，有API：" + CommonCache.ctrlBeanCache.size() + "个");
	}
	
	/**
	 * 启动时加载Controller层的注解信息
	 * @param bean spring 加载好的bean
	 * @param beanName bean名称
	 * @param rootName 系统根路径名称，比如 ips
	 * @param apiNo API编号， 默认为：数字
	 * @param isReload 是否重新加载
	 * @throws Exception 
	 */
	public static BeanInfo loadCtrlAnno(Object bean, String beanName, String rootName, String apiNo, boolean isReload) throws Exception {
		Class<?> clazz = bean.getClass();
		RequestMapping mapping = clazz.getAnnotation(RequestMapping.class);
		if (mapping == null) {
			// 如果没有获取到映射，则跳过，一般不会出现
			throw new Exception(clazz.getName()+"没有设置请求路径！");
		}
		String className = clazz.getSimpleName();
		
		String classPath = mapping.value()[0];//controller上请求路径
		BeanInfo beanInfo = null;
		//如果是重新加载则不必使用
		if(!isReload){
			 beanInfo = new BeanInfo(bean, beanName);
		}
		
		Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
		List<ParamConfig> paramConfig = getParamList(className);
		String methodRequest = "";
		for (Method m : methods) {
			DocData doc = new DocData();
			// 获取请求路径的映射，这个是最关键的
			RequestMapping request = m.getAnnotation(RequestMapping.class);//方法上的请求路径，注意和classPath要用/隔开
			//request.value()可以为空字符串，但不能没有
			if (request != null && request.value().length > 0) {
				 methodRequest = "";//默认没有，一般都需要设置，否则不合规
				int length = request.value().length;
				if(length == 1){
					methodRequest =  request.value()[0];
				}
				String value = SpringContext.root + classPath + methodRequest + SpringContext.suffixName;
				doc.setUrl(value);
				
				// 请求名称，即简单说明该请求时干嘛的
				doc.setApiName(request.name());
				//设置系统编号
				doc.setVersion(apiNo);
				//加载API请求参数说明
				getDocParam(m, paramConfig, doc);
				
				//其他非参数说明
				getDocData(clazz, m, doc);
				//设置系统编号
				doc.setProjName(rootName);
				// 缓存处理的请求的类和方法，在servlet中调用
				if(isReload){
					//重新加载注解文件
					reloadCtrlAPI(doc, request);
				}
				else{
					//初始化
					initCtrlAPI(doc, beanInfo, m);
				}
			}
		}
		return beanInfo;
	}

	/**
	 * 重新加载API说明
	 * @param doc
	 * @param request
	 */
	private static void reloadCtrlAPI(DocData doc, RequestMapping request){
		RequestMethod[] rmArr = request.method();
		String rm = "GET";//默认为get，如果没有的话
		if(rmArr.length > 0){
			rm = rmArr[0].name();
		}
		String key = doc.getUrl() + "-" + rm;
		CtrlBean ctrlBean = CommonCache.ctrlBeanCache.get(key);
		//直接刷新API文档
		ctrlBean.setDoc(doc);
		
		
		if (rmArr.length > 1) {
			// 默认一个API对于一个处理方法，可以多个，注意从1开始
			for(int rmFlag = 1; rmFlag < rmArr.length; rmFlag++){
				rm += ","+rmArr[rmFlag];
				key = doc.getUrl() + "-" + rmArr[rmFlag].name();
				ctrlBean = CommonCache.ctrlBeanCache.get(key);
				//直接刷新API文档
				ctrlBean.setDoc(doc);
			}
		}
		doc.setMethod(rm);
	}
	
	
	/**
	 * 启动时初始化，加载API说明
	 * @param doc
	 * @param beanInfo
	 * @param m
	 */
	private static void initCtrlAPI(DocData doc, BeanInfo beanInfo, Method m){
		RequestMapping request = m.getAnnotation(RequestMapping.class);
		CtrlBean ctrlBean = new CtrlBean(doc, beanInfo, m);
		if(SpringContext.isDebug){
			ctrlBean.setMonitor(new APIMonitor(2));
		}
		else{
			ctrlBean.setMonitor(new APIMonitor(1));
		}
		RequestMethod[] rmArr = request.method();
		String rm = "GET";//默认为get，如果没有的话
		if(rmArr.length > 0){
			rm = rmArr[0].name();
		}
		String key = doc.getUrl() + "-" + rm;
		CommonCache.ctrlBeanCache.put(key, ctrlBean);
		
		if (rmArr.length > 1) {
			// 默认一个API对于一个处理方法，可以多个，注意从1开始
			for(int rmFlag = 1; rmFlag < rmArr.length; rmFlag++){
				rm += ","+rmArr[rmFlag];
				key = doc.getUrl() + "-" + rmArr[rmFlag].name();
				CommonCache.ctrlBeanCache.put(key, ctrlBean);
			}
		}
		doc.setMethod(rm);
	}
	
	private static String getApiNo(String rootName, int i){
		String apiNo = "";
		if(i < 10){
			apiNo = "000"+i;
		}
		else if(i < 100){
			apiNo = "00"+i;
		}
		else if(i < 1000){
			apiNo = "0"+i;
		}
		else {
			apiNo = ""+i;
		}
		return rootName+"-"+apiNo;
	}
	

	/**
	 * 获取请求参数
	 * @param m 对于的业务处理方法
	 * @param className 对于的业务处理类
	 * @param doc 封装对象
	 */
	private static void getDocParam(Method m, List<ParamConfig> paramConfig, DocData doc) {
		// Method m = doc.getAction();
		LanhanParams params = m.getAnnotation(LanhanParams.class);
		// 参数列表
		if (params != null) {
			doc.setSpecialApi("0");
			List<ParamItem> paramList = new ArrayList<ParamItem>();
			LanhanParam[] paramsArr = m.getAnnotation(LanhanParams.class).value();
			for (int i = 0; i < paramsArr.length; i++) {
				ParamItem item = getParam(paramsArr[i]);
				paramList.add(item);
				if (item.getParamType().equals(ParamType.File.getName())) {
					doc.setSpecialApi("file");
				}
			}
			if (paramList.size() > 0) {
				doc.setParams(paramList);
			}
			doc.setParamsDesc(params.desc());
			if (DataUtil.isValidStr(params.beanName())) {
				List<ParamConfig> paramConfig2 = getParamList(params.beanName());
				if(DataUtil.isValidList(paramConfig2)){
					// 如果有明确指定的参数对象，则直接获取
					if (loadBeanAnno(paramConfig2, m.getName(), doc) == 0) {
						logger.warn("没有获取到该请求参对象：beanName:" + params.beanName() + ", url:" + doc.getUrl());
					}
				}
			}
		}
		
		if(DataUtil.isValidList(paramConfig)){
			// 如果没有指定参数模型，则说明可能是默认的形式，比如：UserInfoController，对应的请求参数可能就则UserInfo里面
			loadBeanAnno(paramConfig, m.getName(), doc);
		}
	}

	
	private static List<ParamConfig> getParamList(String className){
		className = className.replace("Controller", "");
		String filePath = PathUtil.getSrcPath("actionConfig/"+className+".json");
		File file = new File(filePath);
		if(file.exists() && file.length() > 10){
			String content = ReadFileUtil.getFileContent(file,"UTF-8");
			Gson gson = new Gson();
			Type type = new TypeToken<List<ParamConfig>>() {}.getType();
			List<ParamConfig> paramList = gson.fromJson(content, type);
			return paramList;
		}
		return null;
	}
	/**
	 * 加载自定义对象请求参数
	 * @param beanName
	 * @param data
	 * @return 用于说明是否获取到bean，是指定了Bean参数对象，而不是默认的形式，则应该提示出来，打印warn log
	 */
	private static int loadBeanAnno(List<ParamConfig> paramList, String methodName, DocData data) {
		for(ParamConfig config : paramList){
			List<String> cmList = config.getCmList();
			if(DataUtil.isValidList(cmList)){
				for(String cms : cmList){
					if (null != cms && cms.contains(":")) {
						String[] cm = cms.split(":");
						if (methodName.equals(cm[0])) {
							// 指定规则，指定的处理方法
							ParamItem item = config.clone();// 必须是从新开启一个
							if (cm[1].equals("1")) {
								item.setRequired(1);
							} else {
								item.setRequired(0);
							}
							data.addParams(item);
						}
					}
				}
			}
		}
		
		return 1;
	}

	/**
	 * 获取自定义的注解,开发者，返回参数类型，session类型，如果是junit测试，则无需加载
	 * @param clazz 
	 * @param m 
	 * @param doc
	 */
	private static void getDocData(Class<?> clazz, Method m, DocData doc) {
		// 作者
		Author authorInfo = m.getAnnotation(Author.class);
		if (authorInfo != null) {
			String authorV = "";
			String dateV = "";
			for (int i = 0, length = authorInfo.value().length; i < length; i++) {
				if (i == 0) {
					authorV = authorInfo.value()[i];
				} else {
					authorV = authorV + "," + authorInfo.value()[i];
				}
			}
			for (int i = 0, length = authorInfo.date().length; i < length; i++) {
				if (i == 0) {
					dateV = authorInfo.date()[i];
				} else {
					dateV = dateV + "," + authorInfo.date()[i];
				}
			}
			doc.setAuthor(authorV);
			doc.setEditDate(dateV);
		}

		// 返回参数
		ReturnData returns = m.getAnnotation(ReturnData.class);
		if (returns != null) {
			String desc = returns.desc().replace("<", "&lt;").replace(">", "&gt;");
			String value = arrToStr(returns.value());

			if (!DataUtil.isValidStr(value)) {
				value = returns.opOk().toString(returns.okTip());
			}
			if (DataUtil.isValidStr(returns.fileUrl())) {
				doc.setFileUrl(SpringContext.root+"/"+returns.fileUrl());
			}
			doc.setReturnDesc(desc);
			doc.setReturnValue(value);
			doc.setReturnFail(returns.opFail().toString(returns.failTip()));
			if (SpringContext.isDebug) {
				String modelPath = returns.modelPath();
				if (DataUtil.isValidStr(modelPath)) {
					// 获取配置文件里面的资源
//					logger.debug("class:"+clazz.getSimpleName()+",modelPath:"+modelPath);
					String content = "";
					if(SpringContext.JunitTest){
//						String[] classPath = clazz.getName().split("\\.");
//						//如果是其他依赖的包，则不需要加载
//						if( doc.getUrl().contains(classPath[classPath.length-3])){
//							//如果是junittest,无法获取jar包，则直接从test/resources中读取
//						}
						if( doc.getUrl().contains("/"+SpringContext.JunitTestModule)){
							//如果是junittest,无法获取jar包，则直接从test/resources中读取
							content = ReadFileUtil.getConfigFile(modelPath);
						}
					}
					else{
						content = ReadFileUtil.getJarContent(clazz, modelPath);
					}
					if (DataUtil.isValidStr(content)) {
						doc.setReturnModel(content);
					}
				}
			}
		}

		// session 类型
		SessionType sessionType = m.getAnnotation(SessionType.class);
		if (sessionType != null) {
			SType type = sessionType.value();
			String desc = sessionType.desc();
			if (!DataUtil.isValidStr(desc)) {
				desc = type.toString();
			}
			String typeStr = type.name();
			doc.setSessionDesc(desc);
			doc.setSessionType(typeStr);
			// 如果是无需登录的，则添加进来
			if (type.equals(SType.NOT_COMPELLED) || type.equals(SType.DISPENSABLE)) {
				CommonCache.freePathHolder.add(doc.getUrl());
			}
		}

	}

	/**
	 * 每一个param都应该是new出来的，不能在原有的基础上修改
	 * @param param
	 * @return
	 */
	private static ParamConfig getParam(LanhanParam param) {
		ParamConfig item = new ParamConfig();
		item.setDefautlValue(param.defaultValue());
		item.setParamDesc(arrToStr(param.desc()));
		item.setField(param.field());
		item.setName(param.name());
		item.setParamRegex(param.regex());
		item.setMatchType(param.matchType());
		item.setRequired(param.required() ? 1 : 0);
		item.setParamType(param.type().getName());
		item.setTypeName(param.type().toString());
		item.setMaxLength(param.maxLength());
		item.setMinLength(param.minLength());
		item.setMinValue(param.minValue());
		item.setMaxValue(param.maxValue());
//		System.out.println(param.maxValue());
		String[] subParams = param.subParam();
		if(subParams.length > 0 && !subParams[0].equals("")){
			for(String sub : subParams){
//				System.out.println(sub);
				String[] subArr = sub.split(":");
				ParamItem subItem = new ParamItem();
				if(subArr.length > 1){
					subItem.setField(subArr[0]);
					subItem.setParamType(subArr[1]);
					subItem.setRequired(0);
					subItem.setMaxLength(0);
					subItem.setMinLength(0);
					subItem.setMinValue(-1);
					subItem.setMaxValue(-1);
					if(subArr.length == 3 && subArr[2].equals("true")){
						subItem.setRequired(1);
					}
					item.getSubParams().add(subItem);
				}
			}
		}
		return item;
	}

	private static String arrToStr(String[] arr) {
		if (arr == null || arr.length == 0) {
			return "";
		}
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			if (i == 0) {
				buf.append(arr[i]);
			} else {
				buf.append(",").append(arr[i]);
			}
		}
		return buf.toString();
	}
	
	/**初始化配置文件：注意是有顺序关联的配置数据，否则再初始化bean的时候就自动加载了*/
	public static void initData(){
//		long t1 = System.currentTimeMillis();
		logger.info("开始初始化系统数据缓存……");
		loadInitData();
		loadTask();//必须放到最后，因为可能需要初始化的数据
//		logger.info("加载数据缓存信息消息时间为：" + (System.currentTimeMillis() - t1) + "ms");
		APIDocUtil.beanHolder.clear();
	}

	/**
	 * 初始化数据，从数据库中加载相关数据
	 */
	private static void loadInitData() {
//		List<Object> beans = new ArrayList<Object>(SpringContext.appContext.getBeansOfType(BaseInit.class, false, true).values());
		List<Object> beans = APIDocUtil.beanHolder.get("BaseInit");;
		List<InitBeanData> beanList = new ArrayList<InitBeanData>();
		for(Object bean : beans){
			InitData order = bean.getClass().getAnnotation(InitData.class);
			if (order != null && order.value() > -1) {
				beanList.add(new InitBeanData(order.value(), bean));
			}
			else {
				// 没有，则说明数据加载和顺序无关,则立即执行
				BaseInit init = (BaseInit) bean;
				init.execInit();
			}
		}
		if (beanList.size() != 0) {
			Collections.sort(beanList);// 排序，默认是升序排序
			for (InitBeanData bean : beanList) {
				BaseInit init = (BaseInit) bean.getBean();
				init.execInit();
			}
		}
	}
	
	private static void loadTask() {
//		List<Object> beans = new ArrayList<Object>(SpringContext.appContext.getBeansOfType(BaseSchedule.class, false, true).values());
		List<Object> beans = APIDocUtil.beanHolder.get("BaseSchedule");
		for(Object bean : beans){
			TaskSchedule task = bean.getClass().getAnnotation(TaskSchedule.class);
			BaseSchedule init = (BaseSchedule) bean;
			init.setBeanName(task.value());
			init.setDesc(task.name());
			init.init();
		}
	}
	
	private static Object addAction(Object bean, String beanName){
		Class<?> clazz = bean.getClass();
		//注意在Controller层最好不要应用第三库gson，否则会有问题，处理数据最好都在service层
		Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
		if (methods != null) {
			StringBuilder body = new StringBuilder();// 动态添加方法内容
			body.append("public void action(HttpCtrl httpCtrl, String method){");
			body.append("if(method.equals(\"\")){").append("System.out.println(\"No method invoke……\");").append("}");
			for (Method method : methods) {
				RequestMapping request = method.getAnnotation(RequestMapping.class);
				if (request != null) {
					body.append(" else if(method.equals(\""+method.getName()+"\")){").append(""+method.getName()+"(httpCtrl);").append("}");
				}
			}
			body.append("}");
			//在class中添加一个调用方法，这个方法可以调用controller中的其他方法，这样可以去掉反射
			ClassPool pool = ClassPool.getDefault();
			pool.importPackage(HttpCtrl.class.getName());
			pool.importPackage(BaseController.class.getName());
			//解决加载问题，可以指定一个未加载的ClassLoader 重新classLoader，不然会报错，toClass
			MyClassLoader loader = new MyClassLoader(BaseController.class.getClassLoader());
			String className = clazz.getName();
			pool.insertClassPath(new ClassClassPath(clazz));
			CtClass cls;
			try {
				cls = pool.get(className);
				CtMethod cmd = CtNewMethod.make(body.toString(), cls);//生成方法
				cls.addMethod(cmd);//添加到类字节码
				if(DataUtil.isEmptyStr(beanName)){
					beanName = DataUtil.lowerFirestChar(clazz.getSimpleName());
				}
				SpringContext.removeBean(beanName);//去掉原来的bean，重新加载
				clazz = cls.toClass(loader, clazz.getProtectionDomain());
//				cls.writeFile();//写入
				cls.detach();
				return SpringContext.registerBean(beanName, null,  clazz);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
