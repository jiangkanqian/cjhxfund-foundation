package com.cjhxfund.foundation.util.str;

import java.util.HashMap;
import java.util.Map;

public class BeanUtil {

	/**
	 * 数据库字段类型与JAVA类型转换
	 * @param type 数据库中的字段类型
	 * @param dataType 数据类型，1：MySQL，2：Oracle，3：SQLserver
	 * @param length 字段长度，Oracle中的数据类型，只有一个number，要根据number长度来确定Java类型
	 * @return
	 */
	public static String typeTrans(String type, String dataType, int length) {
		if(dataType.equalsIgnoreCase("mysql")){
			return getMysqlType(type);
		}
		else if(dataType.equalsIgnoreCase("sqlserver")){
			return getSqlserverType(type, length);
		}
		else{
			return getOracleType(type, length);
		}
	}
	
	private static String getSqlserverType(String type, int length) {
		type = type.toLowerCase(); 
		if(type.contains("\\(")){
			type = type.trim().split("\\(")[0].trim();
		}
		if (type.contains("nume")) {
			//说明没有小数
			if(length > 0 ){
				if(length <= 10){
					return "Integer";
				}
				else{
					return "Long";
				}
			}
			else{
				return "Double";
			}
		}
		else if (type.contains("bit")) {
			return "Boolean";
		} else if (type.contains("bigint")) {
			return "Long";
		} else if (type.contains("int") || type.contains("smallint")
				|| type.equalsIgnoreCase("mediumint") || type.contains("tinyint")) {
			return "Integer";
		} else if (type.contains("double") ||type.contains("float")|| type.contains("decimal")) {
			return "Double";
		}else if (type.contains("varchar") || type.contains("text")
				|| type.equalsIgnoreCase("date") || type.equalsIgnoreCase("time")
				|| type.equalsIgnoreCase("timestamp") || type.contains("datetime")
				|| type.contains("binary") || type.contains("char")) {
			return "String";
		}
		else if (type.contains("binary") || type.contains("blob")) {
			return "Byte[]";
		} else {
			return "String";
		}
	}

	private static String getOracleType(String type, int length) {
		type = type.toLowerCase();
		if (type.equalsIgnoreCase("number")) {
			//说明没有小数
			if(length>0){
				if(length <= 10){
					return "Integer";
				}
				else{
					return "Long";
				}
			}
			else{
				return "Double";
			}
		}
		else if (type.equalsIgnoreCase("Integer")) {
			return "Integer";
		}
		else if (type.equalsIgnoreCase("varchar2") || type.equalsIgnoreCase("text")
				|| type.equalsIgnoreCase("date") 
				|| type.equalsIgnoreCase("timestamp")
				|| type.equalsIgnoreCase("char")) {
			return "String";
		}
		else if (type.contains("blob")) {
			return "Byte[]";
		} else {
			return "String";
		}
	}

	private static String getMysqlType(String type){
		if(type.contains("\\(")){
			type = type.trim().split("\\(")[0].trim();
		}
		if (type.equalsIgnoreCase("bit")) {
			return "Boolean";
		} else if (type.equalsIgnoreCase("bigint")) {
			return "Long";
		} else if (type.equalsIgnoreCase("int") || type.equalsIgnoreCase("smallint")
				|| type.equalsIgnoreCase("mediumint") || type.equalsIgnoreCase("tinyint")) {
			return "Integer";
		} else if (type.equalsIgnoreCase("double") || type.equalsIgnoreCase("decimal")) {
			return "Double";
		} else if (type.equalsIgnoreCase("float")) {
			return "Float";
		} else if (type.equalsIgnoreCase("varchar") || type.equalsIgnoreCase("text")
				|| type.equalsIgnoreCase("date") || type.equalsIgnoreCase("time")
				|| type.equalsIgnoreCase("timestamp") || type.equalsIgnoreCase("datetime")
				|| type.equalsIgnoreCase("enum") || type.equalsIgnoreCase("char")) {
			return "String";
		}
		else if (type.contains("binary") || type.contains("blob")) {
			return "Byte[]";
		} else {
			return "String";
		}
		
	}
	
	

	/**
	 * 格式化字符串：首字母大写
	 * @param src
	 * @return
	 */
	public static String upperFirestChar(String src) {
		return src.substring(0, 1).toUpperCase().concat(src.substring(1));
	}

	/**
	 *  格式化字符串：首字母小写
	 * @param src
	 * @return
	 */
	public static String lowerFirestChar(String src) {
		return src.substring(0, 1).toLowerCase().concat(src.substring(1));
	}

	/**
	 * 去掉表名称的前缀，如：tb_,tbl_, t_等；
	 * @param tableName
	 * @return
	 */
	public static String filterTableName(String tableName) {
		String temp = tableName;
		if (temp.startsWith("tb_")) {
			tableName = tableName.substring(3);
		} else if (temp.startsWith("tbl_")) {
			tableName = tableName.substring(4);
		} else if (temp.startsWith("t_")) {
			tableName = tableName.substring(2);
		}
		return tableName;
	}
	
	/**
	 * 将表名称转化为Java类名称
	 * @param tableName
	 * @return
	 */
	public static String getClassName(String tableName){
		String temp = filterTableName(tableName);
		temp = BeanUtil.upperFirestChar(BeanUtil.deleteUnderline(temp));
		return temp;
	}
	
	/**
	 * 将表名称转化为Java类对象名称：即类对象的首字母小写
	 * @param tableName
	 * @return
	 */
	public static String getClassObjName(String tableName){
		String temp = filterTableName(tableName);
		temp = BeanUtil.lowerFirestChar(BeanUtil.deleteUnderline(temp));
		return temp;
	}

	/**
	 * 去掉名字中的下划线
	 * @param str
	 * @return
	 */
	public static String deleteUnderline(String str) {
		String[] colNames = str.split("_");
		StringBuffer colName = new StringBuffer(colNames[0]);
		if (colNames.length > 1) {
			for (int i = 1; i < colNames.length; i++) {
				colName.append(upperFirestChar(colNames[i]));
			}
		}
		return colName.toString();
	}

	/**
	 * 根据外键字段的备注转换成相应的Map
	 * @param comment
	 * @return
	 */
	public static Map<String, String> getLinkTabComment(String comment) {
		// List<Map<String,String>> comts=new ArrayList<Map<String,String>>();
		comment = comment.trim();
		Map<String, String> map = new HashMap<String, String>();
		int flag = comment.indexOf("(");
		int index1 = 0, index2 = 0;
		index1 = comment.indexOf("(", index1 + 1);
		index2 = comment.indexOf(")", index2 + 1);
		String[] comments = comment.split(",");
//		char ch = comments[1].charAt(0);// 逗号后面如果是字母则说明是中间表
		// 如果有()且还有中文备注则说明对应关系是多对一
		if (flag >= 0 && comment.getBytes().length != comment.length()) {
			map.put("m2o", comment.substring(0, index1));// 关联表
			map.put("for_cname", comment.substring(index1 + 1, index2));// 关联字段
		}
		// 如果没有()且首字母为英文，后面还有中文备注则说明对应关系是一对多
		else if (flag <= 0 && !comment.contains("list")) {
			map.put("o2m", comments[0]);
			map.put("for_cname", "");
		}
		// 如果是list_user开头，则说明对应关系是多对多的关系
		else if (flag <= 0 && comment.contains("list")) {
			index1 = comment.indexOf("<", index1 + 1);
			index2 = comment.indexOf(">", index2 + 1);
			map.put("m2m", comment.substring(index1 + 1, index2));
			map.put("for_cname", "");
		} else {
			map.put("for_linkTab", comments[1]);// 如果后面是关联表，证明是多对多的关系，此表为中间表
		}

		// comts.add(map);
		return map;

	}

	public static Map<String, String> getTypeAndField(String comment) {
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> comt = getLinkTabComment(comment);
		String m2o = comt.get("m2o");
		String o2m = comt.get("o2m");
		String m2m = comt.get("m2m");
		String midTable = comt.get("for_linkTab");
		String op = "add";
		if (null != m2o) {
			map.put("type", upperFirestChar(deleteUnderline(m2o)));
			map.put("field", lowerFirestChar(deleteUnderline(m2o)));
			op = "update";
		}
		if (null != o2m) {
			map.put("type", "List<" + upperFirestChar(deleteUnderline(o2m) + ">"));
			map.put("field", lowerFirestChar(deleteUnderline(o2m) + "s"));
		}
		if (null != m2m) {
			map.put("type", "List<" + upperFirestChar(deleteUnderline(m2m) + ">"));
			map.put("field", lowerFirestChar(deleteUnderline(m2m) + "s"));
		}
		if (null != midTable) {
			map.put("type", upperFirestChar(deleteUnderline(midTable)));
			map.put("field", lowerFirestChar(deleteUnderline(midTable)));
			op = "update";
		}
		map.put("op", op);
		return map;
	}

	// 判断表字段是否和外键关联，在备注表面，以table_name,column_name的形式
	public static boolean isFK(String word) {
		// if(word.equals("")||word==null){
		// return false;
		// }
		// else{
		// word=word.trim();
		// }
		// char ch=word.charAt(0);
		// //如果首字母是英文字母但word中又包含汉字，就说明是外键关联一对多
		// if(((ch>='a'&&ch<='z') || (ch>='A'&&ch<='Z'))&&word.getBytes().length!=word.length()){
		// return true;
		// }
		// else if(!((ch>='a'&&ch<='z') || (ch>='A'&&ch<='Z'))||word.getBytes().length!=word.length()){
		// //如果首字母不是英文字母或者word中包含汉字，就说明不是外键关联
		// return false;
		// }
		// else {
		// //如果首字母是英文，而且没有中文，则说明是对多关系，此表为中间表
		// return true;
		// }

		boolean isFK = false;// 是否关联其他表
		if (word.equals("") || word == null) {
			isFK = false;
		} else {
			word = word.trim();
		}
		char ch = word.charAt(0);
		// 如果首字母是英文字母但word中又包含汉字，就说明是外键关联一对多，或者多对一
		if (((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
				&& word.getBytes().length != word.length()) {
			if (word.contains("(") || word.contains("<")) {
				isFK = true;
			} else {
				isFK = true;
			}

		} else if (!((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
				|| word.getBytes().length != word.length()) {
			// 如果首字母不是英文字母或者word中包含汉字，就说明不是外键关联
			isFK = false;
		} else {
			// 如果首字母是英文，而且没有中文，则说明是对多关系，此表为中间表
			isFK = true;
		}

		return isFK;
	}

	public static void main(String[] args) {
		String text = "t_listt_date";
		String result = filterTableName(text);
		 System.out.println(result);
	}

}
