package com.cjhxfund.foundation.util.thread;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定制每天执行的任务
 * @author lanhan
 * @date 2015年12月9日
 */
public class TimerUtil {

	// 时间间隔
	private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

	public TimerUtil(){};
	
	/**
	 * 定制每天执行的任务
	 * @param hour 几点
	 * @param minute 几分
	 * @param second 几秒
	 * @param task 要执行的任务
	 */
	public TimerUtil(int hour, int minute, int second, TimerTask task) {
		start(hour, minute, second, task);
	}
	
	public void start(int hour, int minute, int second, TimerTask task){
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);

		Date date = calendar.getTime(); // 第一次执行定时任务的时间

		// 如果第一次执行定时任务的时间 小于 当前的时间
		// 此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。循环执行的周期则以当前时间为准
		if (date.before(new Date())) {
			date = this.addDay(date, 1);
		}
		Timer timer = new Timer();
		// 安排指定的任务在指定的时间开始进行重复的固定延迟执行。
		timer.schedule(task, date, PERIOD_DAY);
	}

	// 增加或减少天数
	public Date addDay(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}
	
	

}
