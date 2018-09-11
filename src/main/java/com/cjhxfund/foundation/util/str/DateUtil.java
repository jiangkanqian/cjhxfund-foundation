package com.cjhxfund.foundation.util.str;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

import com.cjhxfund.foundation.util.data.DataUtil;

public class DateUtil {

	private DateUtil() {
		throw new Error("该类不能被实例化！");
	}

	private static final String defaultDatePattern = "yyyy-MM-dd";
	private static final String defaultTimePattern = "HH:mm:ss";
	private static final String defaultDTPattern = "yyyy-MM-dd HH:mm:ss";
	private static final String orderPattern = "yyyyMMddHHmmss";
	private static final String defaultDHMPattern = "yyyy-MM-dd HH:mm";
	private static final String defaultYMPattern = "yyyy-MM";

	/**
	 * 获取当前时间：yyyyMMddHHmmss
	 * 
	 * @return yyyyMMddHHmmss
	 */
	public static String getOrderDateTime() {
		Date now = new Date();
		return format(now, orderPattern);
	}

	/**
	 * 获取当前时间：HH:mm:ss
	 * 
	 * @return HH:mm:ss
	 */
	public static String getNowTime() {
		Date now = new Date();
		return format(now, defaultTimePattern);
	}

	/**
	 * 获取日期数字：yyyyMMdd
	 * 
	 * @return
	 */
	public static int getDateNum() {
		Date now = new Date();
		return Integer.valueOf(format(now, "yyyyMMdd"));
	}
	
	public static int getDateNum(String date) {
		if(DataUtil.isValidStr(date)){
			date = date.replaceAll("-", "");
			return Integer.valueOf(date);
		}
		return 0;
	}

	/**
	 * 获取时间数字：HHmmss
	 * 
	 * @return
	 */
	public static int getTimeNum() {
		Date now = new Date();
		return Integer.valueOf(format(now, "HHmmss"));
	}

	/**
	 * 获取当前时间：自定义格式，如：HHmmss
	 * 
	 * @return HHmmss
	 */
	public static String getNowTime(String pattern) {
		Date now = new Date();
		return format(now, pattern);
	}

	/**
	 * 获取当前日期时间：yyyy-MM-dd HH:mm:ss
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getNowDateTime() {
		Date now = new Date();
		return format(now, defaultDTPattern);
	}

	/**
	 * 获取当前日期时间，但是只精确到分：yyyy-MM-dd HH:mm
	 * 
	 * @return ：yyyy-MM-dd HH:mm
	 */
	public static String getDateWithDMH() {
		Date now = new Date();
		return format(now, defaultDHMPattern);
	}

	/**
	 * 返回时间差的秒数
	 * 
	 * @param time1
	 *            19:23:12
	 * @param time2
	 *            20:23:12
	 * @return 返回秒数 3600
	 */
	public static int getSecondsLag(String time1, String time2) {
		int lag = 0;
		String[] tArr1 = time1.trim().split(":");
		String[] tArr2 = time2.trim().split(":");
		int hour1 = Integer.parseInt(tArr1[0]);
		int hour2 = Integer.parseInt(tArr2[0]);
		int minute1 = Integer.parseInt(tArr1[1]);
		int minute2 = Integer.parseInt(tArr2[1]);
		int second1 = Integer.parseInt(tArr1[2]);
		int second2 = Integer.parseInt(tArr2[2]);
		lag = (hour2 - hour1) * 3600 + (minute2 - minute1) * 60
				+ (second2 - second1);
		return lag;

	}

	/**
	 * 返回当前日期字符串：yyyy-MM-dd
	 */
	public static String getToday() {
		Date today = new Date();
		return format(today);
	}

	/**
	 * 获取距离今天的前面后者后面 num 天 返回当前日期字符串：yyyy-MM-dd param num
	 */
	public static String getAroundToday(int num) {
		Calendar numday = Calendar.getInstance();
		numday.add(Calendar.DATE, num);
		return format(numday.getTime());
	}

	/**
	 * 转成这种格式的字符串：yyyy-MM，值为当前年月
	 */
	public static String getThisYearMonth() {
		Date today = new Date();
		return format(today, defaultYMPattern);
	}

	/**
	 * 转成这种格式的字符串：yyyy-MM-dd
	 */
	public static String format(Date date) {
		return date == null ? " " : format(date, defaultDatePattern);
	}

	/**
	 * 转成这种格式的字符串:HH:mm:ss
	 */
	public static String formatTime(Date date) {
		return date == null ? " " : format(date, defaultTimePattern);
	}

	/**
	 * 转成这种格式的字符串：yyyy-MM-dd HH:mm:ss
	 */
	public static String formatDateTime(Date date) {
		return date == null ? " " : format(date, defaultDTPattern);
	}

	/**
	 * 使用参数Format格式化Date成字符串
	 */
	public static String format(Date date, String pattern) {
		return date == null ? " " : new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 使用参数Format格式化Date成字符串
	 */
	public static String formatNow(String pattern) {
		return new SimpleDateFormat(pattern).format(new Date());
	}

	/**
	 * 使用参数Format格式化Date成字符串
	 */
	public static String format(String date, String pattern) {
		return date == null ? " " : new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 使用预设格式将字符串转为Date:yyyy-MM-dd
	 */
	public static Date parse(String strDate) {
		strDate = strDate.trim();
		return StringUtils.isBlank(strDate) ? null : parse(strDate,
				defaultDatePattern);
	}
	
	public static Date parse(Integer date) {
		String strDate = formatDateNum(date);
		return StringUtils.isBlank(strDate) ? null : parse(strDate,
				defaultDatePattern);
	}

	/**
	 * 使用参数Format将字符串转为Date
	 */
	public static Date parse(String strDate, String pattern) {
		pattern = pattern.trim();
		try {
			return StringUtils.isBlank(strDate.trim()) ? null
					: new SimpleDateFormat(pattern).parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 在日期上增加数个整月
	 */
	public static Date addMonth(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n);
		return cal.getTime();
	}

	/**
	 * 在日期上增加天数
	 */
	public static Date addDay(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, n);
		return cal.getTime();
	}

	/**
	 * 计算和此刻相差的时间单位 String today="2014-12-03 18:05:00"; getBetweenTime(today));
	 * return map 相差多少天多少小时多少分钟多少秒
	 */
	public static Map<String, Long> getBetweenTime(String start) {
		Map<String, Long> lagTime = new HashMap<String, Long>();
		String pattern = "yyyy-MM-dd HH:mm:ss";
		if (!start.contains(":")) {// 如果传过来的是日期型，即没有时间的
			pattern = "yyyy-MM-dd";
		}
		Date begin = DateUtil.parse(start, pattern);
		long between = (System.currentTimeMillis() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
		long day = between / (24 * 3600);
		long hour = between % (24 * 3600) / 3600;
		long minute = between % 3600 / 60;
		long second = between % 60;
		lagTime.put("between", between);// 相差的秒数
		lagTime.put("year", day / 365);// 相差的年数
		lagTime.put("month", day / 30);// 相差的月数
		lagTime.put("day", day);// 相差的天数
		lagTime.put("hour", day * 24 + hour);// 相差的小时数
		lagTime.put("minute", (day * 24 + hour) * 60 + minute);// 相差的分钟

		// 相差多少年多少月多少天多少时
		lagTime.put("lagYear", day / 365);
		lagTime.put("lagMonth", day / 30 - (day / 365 * 12));
		lagTime.put("lagDay", day - (day / 30) * 30);
		lagTime.put("lagHour", hour);
		lagTime.put("lagMinute", minute);
		lagTime.put("lagSecond", second);
		return lagTime;

		// System.out.println( "相差" + day1 + "天" + hour1 + "小时" + minute1+ "分" +
		// second1 + "秒");
	}

	public static Long getLagSecond(String start) {
		long between = (System.currentTimeMillis() - getDateLong(start)) / 1000;// 除以1000是为了转换成秒
		return between;
	}

	public static Long getLagDay(String start) {
		long between = (System.currentTimeMillis() - getDateLong(start)) / 1000;// 除以1000是为了转换成秒
		return between / (24 * 3600);
	}

	public static Integer getLastDayOfMonth(Integer year, Integer month, Integer day) {
		Calendar cal = Calendar.getInstance();  
		cal.set(year, month-1, day);//2011-03-20 12:20:20
		cal.add(Calendar.MONTH, -1);//取前一个月的同一天 
		Date date = cal.getTime();
		return Integer.valueOf(format(date, "yyyyMMdd"));
	}
	
	public static String getLastDayOfMonth(String year, String month) {
		Calendar cal = Calendar.getInstance();
		// 年
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		// 月，因为Calendar里的月是从0开始，所以要-1
		// cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
		// 日，设为一号
		cal.set(Calendar.DATE, 1);
		// 月份加一，得到下个月的一号
		cal.add(Calendar.MONTH, 1);
		// 下一个月减一为本月最后一天
		cal.add(Calendar.DATE, -1);
		return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));// 获得月末是几号
	}

	public static Date getDate(String year, String month, String day)
			throws ParseException {
		String result = year + "- "
				+ (month.length() == 1 ? ("0" + month) : month) + "- "
				+ (day.length() == 1 ? ("0" + day) : day);
		return parse(result);
	}

	/**
	 * 将日期的字符串，变成系统日期的long数据，类似：System.currentTimeMillis()
	 * 
	 * @param dateStr
	 *            时间字符串，默认格式为2016-06-06 10:20:20或者2016-06-06
	 */
	public static Long getDateLong(String dateStr) {
		if (!dateStr.contains(":")) {// 如果传过来的是日期型，即没有时间的
			return DateUtil.parse(dateStr, defaultDatePattern).getTime();
		}
		return DateUtil.parse(dateStr, defaultDTPattern).getTime();
	}

	/**
	 * 将日期的字符串，变成系统日期的long数据，类似：System.currentTimeMillis()
	 * 
	 * @param dateStr
	 *            时间字符串
	 * @param dateStr
	 *            该时间字符串的格式
	 */
	public static Long getDateLong(String dateStr, String pattern) {
		return DateUtil.parse(dateStr, pattern).getTime();
	}

	/**
	 * 将类似System.currentTimeMillis()的长整数变为可识别的日期格式
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String getDateStr(Long dateTime) {
		if (dateTime == null) {
			return "";
		}
		return format(new Date(dateTime), defaultDTPattern);
	}

	/**
	 * 将类似System.currentTimeMillis()的长整数变为可识别的日期格式
	 * 
	 * @param dateTime
	 * @param pattern
	 *            "yyyy-MM-dd" ,如果没有 则为默认的"yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String getDateStr(Long dateTime, String pattern) {
		if (dateTime == null) {
			return "";
		}
		if (DataUtil.isValidStr(pattern)) {
			return format(new Date(dateTime), pattern);
		} else {
			return format(new Date(dateTime), defaultDTPattern);
		}
	}

	/**
	 * 把日期改为日期时间格式，并为改日的开始时间，即加上" 00:00:00"
	 * 
	 * @author xiejs
	 * @date 2015年7月21日
	 * @param startDate
	 * @return
	 */
	public static String formatStartDate(String startDate) {
		return startDate + " 00:00:00";
	}

	/**
	 * 把日期改为日期时间格式，并为改日的最后时间,即加上" 23:59:59"
	 * 
	 * @author xiejs
	 * @date 2015年7月21日
	 * @param endDate
	 * @return
	 */
	public static String formatEndDate(String endDate) {
		return endDate + " 23:59:59";
	}

	/**
	 * 获取今天所在的这一年的第几周
	 */
	public static int getThisWeekNo() {
		return Calendar.getInstance(TimeZone.getTimeZone("Asia/Beijing")).get(
				Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 获取每天的时间点，一般用于定时
	 * 
	 * @author xiejs
	 * @date 2015年9月10日
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	 public static Date getTimer(int hour,int minute, int second){
	        Calendar calendar = Calendar.getInstance();
	        calendar.set(Calendar.HOUR_OF_DAY, hour);
	        calendar.set(Calendar.MINUTE, minute);
	        calendar.set(Calendar.SECOND, second);
	        Date time = calendar.getTime();
	        return time;
	   }
	 
	 /**
	  * 
	  * @author xiejs
	  * @date 2015年9月28日
	  * @param flag : year,month,day,hour,minute,second
	  * @return 返回当前年月日，时分秒的数字
	  */
	 public static int getThisTimeNum(String flag){
		 Calendar calendar = Calendar.getInstance();
		 if(flag.equals("year")){
			 return calendar.get(Calendar.YEAR);
		 }
		 else if(flag.equals("month")){
			 return calendar.get(Calendar.MONTH)+1;
		 }
		 else if(flag.equals("day")){
			 return calendar.get(Calendar.DAY_OF_MONTH);
		 }
		 else if(flag.equals("hour")){
			 return calendar.get(Calendar.HOUR_OF_DAY);
		 }
		 else if(flag.equals("minute")){
			 return calendar.get(Calendar.MINUTE);
		 }
		 else if(flag.equals("second")){
			 return calendar.get(Calendar.SECOND);
		 }
		 return 0;
	 }
	
	 /**
	  * 将数字日期yyyyMMdd格式转为yyyy-MM-dd
	  * @param date
	  * @return
	  */
	 public static String formatDateNum(int date){
		 String dateStr = date+"";
		 return dateStr.substring(0, 4)+"-"+dateStr.substring(4, 6)+"-"+dateStr.substring(6, 8);
	 }
	 
	 public static String getDateStr(Integer dateTime, String pattern){
		if(dateTime.intValue() > 0){
			String dateStr = dateTime.toString();
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, Integer.valueOf(dateStr.substring(0, 4)));
			calendar.set(Calendar.MONTH, Integer.valueOf(dateStr.substring(4, 6))-1);
			calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dateStr.substring(6, 8)));
			Date time = calendar.getTime();
			return format(time, pattern);
		}
		return null;
	 }
	
	
	public static void main(String[] args) {
//		 System.out.println(getThisWeekNo());
//		 System.out.println(formatNow("yyyy"));
//		Calendar.getInstance().DAY_OF_WEEK
//		 String updateTime = "2015-08-17 22:32:29";
//		 Map<String,Long> times = DateUtil.getBetweenTime(updateTime);
//		 System.out.println(new Date().getYear());
//		 System.out.println(getThisTimeNum("year"));
//		 System.out.println(getThisTimeNum("month"));
//		 System.out.println(getThisTimeNum("day"));
//		 System.out.println(getThisTimeNum("hour"));
//		 System.out.println(getThisTimeNum("minute"));
//		 System.out.println(getThisTimeNum("second"));
//		String date = DateUtil.getDateNum()+"";
		String result = getDateStr(DateUtil.getDateNum(), "yyyy/M/dd");
		 System.out.println(result);
	}

	/**
	 * 获取指定日期是周几
	 * 
	 * @param dt
	 * @return
	 */
	public static int getWeekOfDate(Date dt) {
		// String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"
		// };
		int[] weekDaysNum = { 7, 1, 2, 3, 4, 5, 6 };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDaysNum[w];
	}

	/**
	 * 获取指定日期是当月第几天
	 * 
	 * @param dt
	 * @return
	 */
	public static int getMonthDay(Date dt) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取当前季度开始时间
	 * 
	 * @return current quarter
	 */
	public static Date getThisQuarter() {
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.set(Calendar.MONTH,
				((int) startCalendar.get(Calendar.MONTH) / 3) * 3);
		startCalendar.set(Calendar.DAY_OF_MONTH, 1);
		return startCalendar.getTime();
	}

	/**
	 * 获取前/后半年的开始时间
	 * 
	 * @return
	 */
	public static Date getHalfYearStartTime() {
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 6) {
				c.set(Calendar.MONTH, 0);
			} else if (currentMonth >= 7 && currentMonth <= 12) {
				c.set(Calendar.MONTH, 6);
			}
			c.set(Calendar.DATE, 1);
			now = DateUtil.parse(DateUtil.format(c.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}
}
