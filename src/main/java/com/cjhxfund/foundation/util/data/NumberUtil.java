package com.cjhxfund.foundation.util.data;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 * 数字处理工具类
 * <p>注意事项：</p>
 * <ul>
 * 	<li>数据库Decimal类型最好对应java的BigDecimal类型</li>
 * 	<li>金额计算，使用整型，把参与值扩大100倍进行运算，运算后再缩小100倍</li>
 * 	<li>不要让类型默认转换，默认转换的过程是：先运算再转换；必须在运算前就进行转换</li>
 * 	<li>基本类型优先考虑</li>
 * 	<li></li>
 * </ul>
 * @author lanhan
 * @date 2015年10月30日
 */
public class NumberUtil {
	
	private NumberUtil(){
		throw new Error("该类不能被实例化！");
	}
	
	/**
	 * double result = MySqrt(3.0, 16); //1.732050807568877
	 * @param A 待开方的数
	 * @param precision 精度,10
	 * @return 开方结果
	 * @throws Exception
	 */
	public static double MySqrt(double A, int precisionNum) throws Exception {
		if (A < 0) {
			throw new Exception("不能为负数");
		}
		if(precisionNum>15)precisionNum=15; //不能超过15位
		double precision = 1/Math.pow(10,precisionNum);
		System.out.println(precision);
		double min = 0, max = A;
		double result = (min + max) / 2;
		while (Math.abs(A - result * result) > precision) {
			if (A - result * result > 0)
				min = result;
			else
				max = result;
			result = (min + max) / 2;
		}
		
		long temp = (long) (result*Math.pow(10,precisionNum));
		result = temp/Math.pow(10,precisionNum);
		return result;
	}
	
	/**
	 * 取出字符串中的数字
	 * @param content  "asd我10是20小30菜";
	 * @return [10,20,30]
	 */
	public static String[] getNumInStr(String content){
		if(DataUtil.isValidStr(content)){
			String repStr = content.replaceAll("[\u4e00-\u9fa5]+|[a-zA-z]+", ",");
			System.out.println(repStr);//,20,30,40,
			String[] strArr = repStr.split(",");
			int total = 0;
			for(int i=0;i<strArr.length;i++){
				if(null!=strArr[i]&&!"".equals(strArr[i])){
					total ++;
				}
			}
			String[] result = new String[total];
			total = 0;
			for(int i=0;i<strArr.length;i++){
				if(null!=strArr[i]&&!"".equals(strArr[i])){
					result[total]=strArr[i];
					total++;
				}
			}
			return result;
		}
		return null;
	}
	
	
	/**
	 * 获取一个一个数据段的数值，from必须小于to 否则将返回0
	 * @author xiejs
	 * @date 2015年7月15日
	 * @param from 开始数值 比如：0
	 * @param to 结束数值 比如：10
	 * @return 
	 * @throws Exception 
	 */
	public static int getBetweenNum(int from,int to){
		if(to==from){
			return to;
		}
		if(from>to){
//			throw new Exception("from:"+from+"-to:"+to+"，but from is lager than to!");
			return 0;
		}
		Random rand = new Random();
		int randNum = rand.nextInt(to-from)+from;
		return randNum;
	}
	
	public static double getRandDouble(int intNum, String point){
		Random rand = new Random();
		double randNum = rand.nextDouble()*intNum;
		return formatDouble(randNum, point);
	}
	
	
	/**
	 * 对小数进行格式化处理的方法.
	 * @param number 要处理的数字
	 * @param format 格式化字符串，例如0.00,标识保留两位小数
	 * @return 格式化后的数字
	 */
	public static double formatDouble( Double number,  String format) {
		if(number!=null){
			DecimalFormat df = new DecimalFormat(format);
			return Double.valueOf(df.format(number));
		}
		return 0.0;
	}
	
	public static String formatDoubleStr( Double number,  String format) {
		if(number!=null){
			DecimalFormat df = new DecimalFormat(format);
			return df.format(number);
		}
		return "";
	}
	
	
	
	/**
	 * 对小数进行格式化处理的方法.
	 * @param number 要处理的数字
	 * @return 格式化后的数字：2.00
	 */
	public static double defaultFormatDouble(final double number) {
		DecimalFormat df = new DecimalFormat("0.00");
		return Double.valueOf(df.format(number));
	}
	
	/**
	 * 两个数相减后，返回保留的数字格式
	 * @author Jason
	 * @date 2016年2月28日
	 * @param num1 减数
	 * @param num2 被减数
	 * @param format 小数点，如：2.00
	 * @return 
	 */
	public static double formatDouble(final double num1, final double num2, String format) {
		NumberFormat df = new DecimalFormat(format);
		return Double.valueOf(df.format(num1 - num2));
	}
	
	/**
	 * 获取数组中最大值
	 * @param data
	 * @return
	 */
	public static int getMax(int[] data){
//		int[] arr = {1,3,7,4,3,8};
//		int max = getMax(arr);
//		System.out.println(max);
//		System.out.println(Arrays.toString(arr));
		Arrays.sort(data.clone());//之所以要克隆，这样避免了改变原有数据的位置
//		Arrays.sort(data);
		return data[data.length - 1];
	}
	
	/**
	 * 获取第二大的数字
	 * @param data
	 * @return
	 */
	public static int getMaxNext(Integer[] data){
		List<Integer> dataList = Arrays.asList(data);//转为列表
		TreeSet<Integer> ts = new TreeSet<Integer>(dataList);//去除重复元素，并升序排序
		//ts.last()是最大值，ts.lower取的是比最大值小的一个
		return ts.lower(ts.last());
	}
	
	/**
	 * 将时间字符串变为可识别的数字，比如：
	 * 2015-05-02 17:59:31——>20150502175931+随机数*100=20150502175976
	 * @return Long 时间数字
	 */
	public static String getOrderNo(){
		String dateStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		return dateStr+(int)(Math.random() * 100);
	}
	
	/**
	 * 将时间字符串变为可识别的数字，比如：
	 * 2015-05-02 17:59:31——>20150502175931+随机数*10的num次方
	 * @return Long 时间数字
	 */
	public static String getOrderNo(int num){
		String dateStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		return dateStr+(int)(Math.random() * Math.pow(10,num));
	}
	
	
	public static void main(String[] args) {
		int[] arr = {1,3,7,4,3,8};
		int max = getMax(arr);
		System.out.println(max);
		System.out.println(Arrays.toString(arr));
		Integer[] arr2 = {1,3,7,4,3,8};
		int second = getMaxNext(arr2);
		System.out.println(Arrays.toString(arr2));
		System.out.println(second);
		List<Integer> dataList = Arrays.asList(arr2);
		dataList.add(9);
	}
	
}
