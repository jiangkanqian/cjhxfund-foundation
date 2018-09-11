package com.cjhxfund.foundation.web.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.cjhxfund.foundation.log.constants.TaskLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.util.data.DataUtil;
import com.cjhxfund.foundation.util.str.DateUtil;
import com.cjhxfund.foundation.web.context.SpringContext;
import com.cjhxfund.foundation.web.model.TaskInfo;

/**
 * 定时任务基础类，凡是基础
 * @author xiejiesheng
 * 必须要重写initParams 和initTask两个方法
 */

public class BaseSchedule {

	protected static JLogger logger = TaskLogger.getLogger();

	// 时间间隔
	private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

	protected int hour;
	protected int minute;
	protected int second = 0;
	protected boolean startAuto;
	protected boolean status = true;
	protected int delaySecs;//startAuto=true，时有用，即在启动后多少秒执行，避免那种启动时加载过程，阻塞其他应用启动
	protected TimerTask task;
	protected Timer timer;
	public long execTime = 0L;

	protected String desc;
	
	protected String beanName;
	protected String doTaskIP;//执行任务的IP，如果是多节点部署；
	protected String methodName;

	private Object bean;
	private Method method;
	
	/**取消定时器*/
	public synchronized void cancel() {
		if(status){
			if(task != null){
				task.cancel();
				logger.info("取消任务成功:" + beanName);
			}
			if(timer != null){
				timer.cancel();
				logger.info("取消定时器成功:" + beanName);
			}
			status = false;
		}
	}
	
	/**重置定时器，但是参数不会重置，需要手动修改后重置*/
	public synchronized void reset() {
		if(!status){
			this.timer = new Timer();
			initTask();
			start();
			status = true;
		}
	}

	/**
	 * 系统启动时会执行该方法，初始化相关参数
	 */
	public void init() {
		this.timer = new Timer();
		// 顺序不能乱
		initParams();
		if(DataUtil.isEmptyStr(doTaskIP)){
			this.doTaskIP = SpringContext.getLocalIp();//默认本地IP
		}
		initTask();
		logger.info("开始指定任务执行时间……task:"+beanName+
				", timer:"+beanName+",每天执行时间："+(hour<10?"0"+hour:hour)+":"+(minute<10?"0"+minute:minute)+":"+(second<10?"0"+second:second));
		if (startAuto && doTaskIP.equals(SpringContext.getLocalIp())) {
			if(delaySecs == 0){
				// 如果是表示自动启动，则在启动时会立即执行
				doTask();
			}
			else{
				//延迟时间执行
				new DelayTaskThread().start();
			}
		}
		start();
	}
	
	class DelayTaskThread extends Thread{
		
		@Override
		public void run() {
			long value = delaySecs*1000L;
			try {
				Thread.sleep(value);
				doTask();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.info("延迟任务执行完成……");
		}
	}

	/**
	 * 初始化定时任务参数
	 *  hour 几点
	 *  minute 几分
	 *  second 几秒
	 *  delaySecs 延迟几秒执行 startAuto=true，时有用
	 *  doTaskIP 执行任务的IP，如果是多节点部署；默认本地IP
	 *  startAuto 是否在系统启动时立即执行任务，如果是否，则是在指定的时间执行
	 */
	protected void initParams() {
		this.hour = 0;
		this.minute = 0;
		this.second = 0;
		this.startAuto = false;
		this.doTaskIP = SpringContext.getLocalIp();//默认本地IP
		initConfig(null, null);
	}

	/**
	 * 自定义任务的执行，
	 * @param beanName beanName可以为service的bean
	 * @param methodName methodName为执行方法，执行方法不运行有参数
	 */
	protected void initConfig(String beanName, String methodName) {
		if (DataUtil.isValidStr(beanName) && DataUtil.isValidStr(methodName)) {
			this.beanName = beanName;
			this.methodName = methodName;
			this.bean = SpringContext.getSpringBean(beanName);
			if (bean != null) {
				try {
					method = this.bean.getClass().getMethod(methodName);
				} catch (NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
					logger.error(e);
				}
			}
		}
	}
	
	/**执行具体的任务*/
	public void doTask(){
		
	}

	/**
	 * 编写任务
	 */
	protected void initTask() {
		task = new TimerTask() {
			public void run() {
				if(doTaskIP.equals(SpringContext.getLocalIp())){
					doTask();
					execTime = System.currentTimeMillis();
				}
			}
		};
	}
	
	/**
	 * 重新设置设计
	 * @param hour
	 * @param minute
	 * @param second
	 */
	public void resetTime(int hour, int minute, int second){
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		initTask();
		start();
	}



	/**启动任务，但是这个任务是指定到某个类和某个方法，并且在Spring里面*/
	public void startConfig() {
		logger.info("开始执行任务……");
		if (task != null) {
			task.cancel();
			task = null;
		}
		task = new TimerTask() {
			public void run() {
				try {
					method.invoke(bean);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
					logger.error(e);
				}
			}
		};
		execTime = System.currentTimeMillis();
	}

	private void start() {
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.HOUR_OF_DAY, this.hour);
		calendar.set(Calendar.MINUTE,  this.minute);
		calendar.set(Calendar.SECOND,  this.second);

		Date date = calendar.getTime(); // 第一次执行定时任务的时间

		// 如果第一次执行定时任务的时间 小于 当前的时间
		// 此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。循环执行的周期则以当前时间为准
		if (date.before(new Date())) {
			date = this.addDay(date, 1);
		}
		// 安排指定的任务在指定的时间开始进行重复的固定延迟执行。
		this.timer.schedule(this.task, date, PERIOD_DAY);
	}

	// 增加或减少天数
	private Date addDay(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}

	public boolean isStartAuto() {
		return startAuto;
	}

	public void setStartAuto(boolean startAuto) {
		this.startAuto = startAuto;
	}

	public TimerTask getTask() {
		return task;
	}

	public void setTask(TimerTask task) {
		this.task = task;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	
	public int getDelaySecs() {
		return delaySecs;
	}

	public void setDelaySecs(int delaySecs) {
		this.delaySecs = delaySecs;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	
	
	public String getDoTaskIP() {
		return doTaskIP;
	}

	public void setDoTaskIP(String doTaskIP) {
		this.doTaskIP = doTaskIP;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public TaskInfo toTaskInfo(){
		TaskInfo task = new TaskInfo();
		task.setDesc(desc);
		if(execTime > 0L){
			task.setExecTime(DateUtil.getDateStr(execTime));
		}
		task.setHour(hour);
		task.setMinute(minute);
		task.setSecond(second);
		task.setStatus(status);
		task.setStartAuto(startAuto);
		task.setTaskNo(this.getBeanName());
		task.setDoTaskIP(doTaskIP);
		return task;
	}
	
	@Override
	public String toString() {
		return "{\"hour\":" + hour + ",\"minute\":" + minute + ",\"second\":" + second +
				",\"startAuto\":" + startAuto + ",\"execTime\":\"" + DateUtil.getDateStr(execTime) + 
				 "\",\"methodName\":\"" + methodName + "\""+
				",\"beanName\":\"" + beanName + "\",\"desc\":\"" + desc + "\""+
				",\"status\":" + status +"}";
	}

}
