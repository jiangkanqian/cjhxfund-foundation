package com.cjhxfund.foundation.util.data;

import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class DataType {

	public DataType() {
	}

	public static boolean isNeedFullClassName(String type) {
		if (type.equals("String"))
			return false;
		if (type.equals("Short"))
			return false;
		if (type.equals("Integer"))
			return false;
		if (type.equals("Long"))
			return false;
		if (type.equals("Double"))
			return false;
		if (type.equals("Float"))
			return false;
		if (type.equals("Byte"))
			return false;
		if (type.equals("Char"))
			return false;
		if (type.equals("Boolean"))
			return false;
		if (type.equals("Date"))
			return true;
		if (type.equals("Time"))
			return true;
		if (type.equals("DateTime"))
			return true;
		if (type.equals("Object"))
			return false;
		if (type.equals("short"))
			return false;
		if (type.equals("int"))
			return false;
		if (type.equals("long"))
			return false;
		if (type.equals("double"))
			return false;
		if (type.equals("float"))
			return false;
		if (type.equals("byte"))
			return false;
		if (type.equals("char"))
			return false;
		return !type.equals("boolean");
	}

	public int getDatabaseDataType(String type) throws Exception {
		if (type.equalsIgnoreCase("String"))
			return 12;
		if (type.equalsIgnoreCase("Short"))
			return 4;
		if (type.equalsIgnoreCase("Integer"))
			return 4;
		if (type.equalsIgnoreCase("Long"))
			return 4;
		if (type.equalsIgnoreCase("Double"))
			return 8;
		if (type.equalsIgnoreCase("Float"))
			return 6;
		if (type.equalsIgnoreCase("Byte"))
			return 4;
		if (type.equalsIgnoreCase("Char"))
			return 12;
		if (type.equalsIgnoreCase("Boolean"))
			return 4;
		if (type.equalsIgnoreCase("Date"))
			return 91;
		if (type.equalsIgnoreCase("Time"))
			return 92;
		if (type.equalsIgnoreCase("DateTime")) {
			return 93;
		} else {
			String msg ="No type found!";
			throw new Exception(msg);
		}
	}

	/**
	 * 获取java类型名，如果是基础类型，将返回对应的对象类型名称（simpleName）
	 * @param type 类型名（simpleName）
	 * @return
	 */
	public static String getJavaObjectType(String type) {
		if (type.equalsIgnoreCase("String"))
			return "String";
		if (type.equalsIgnoreCase("Short") || type.equalsIgnoreCase("short"))
			return "Short";
		if (type.equalsIgnoreCase("Integer") || type.equalsIgnoreCase("int"))
			return "Integer";
		if (type.equalsIgnoreCase("Long") || type.equalsIgnoreCase("long"))
			return "Long";
		if (type.equalsIgnoreCase("Double") || type.equalsIgnoreCase("double"))
			return "Double";
		if (type.equalsIgnoreCase("Float") || type.equalsIgnoreCase("float"))
			return "Float";
		if (type.equalsIgnoreCase("Byte") || type.equalsIgnoreCase("byte"))
			return "Byte";
		if (type.equalsIgnoreCase("Char") || type.equalsIgnoreCase("char"))
			return "Character";
		if (type.equalsIgnoreCase("Boolean") || type.equalsIgnoreCase("boolean"))
			return "Boolean";
		if (type.equalsIgnoreCase("Date"))
			return "Date";
		if (type.equalsIgnoreCase("Time"))
			return "Time";
		if (type.equalsIgnoreCase("DateTime"))
			return "Timestamp";
		else
			return type;
	}
	
	public static Class loadClass(String name) throws ClassNotFoundException {
		Class result = null;
		try {
			result = Thread.currentThread().getContextClassLoader().loadClass(name);
		} catch (ClassNotFoundException ex) {
		}
		if (result == null)
			result = Class.forName(name);
		return result;
	}

	public static Class getJavaClass(String type) throws ClassNotFoundException {
		int index = type.indexOf("[]");
		if (index < 0)
			return getJavaClassInner(type);
		String arrayString = "[";
		String baseType = type.substring(0, index);
		while ((index = type.indexOf("[]", index + 2)) >= 0)
			arrayString = (new StringBuilder()).append(arrayString).append("[").toString();
		Class baseClass = getJavaClassInner(baseType);
		String baseName;
		try {
			baseName = "";
			if (!baseClass.isPrimitive())
				return loadClass((new StringBuilder()).append(arrayString).append("L").append(baseClass.getName()).append(";").toString());
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
		if (baseClass.equals(Boolean.TYPE))
			baseName = "Z";
		else if (baseClass.equals(Byte.TYPE))
			baseName = "B";
		else if (baseClass.equals(Character.TYPE))
			baseName = "C";
		else if (baseClass.equals(Double.TYPE))
			baseName = "D";
		else if (baseClass.equals(Float.TYPE))
			baseName = "F";
		else if (baseClass.equals(Integer.TYPE))
			baseName = "I";
		else if (baseClass.equals(Long.TYPE))
			baseName = "J";
		else if (baseClass.equals(Short.TYPE))
			baseName = "S";
		return loadClass((new StringBuilder()).append(arrayString).append(baseName).toString());
	}

	protected static Class getJavaClassInner(String type) {
		if (type.equals("String"))
			return String.class;
		if (type.equals("Short"))
			return Short.class;
		if (type.equals("Integer"))
			return Integer.class;
		if (type.equals("Long"))
			return Long.class;
		if (type.equals("Double"))
			return Double.class;
		if (type.equals("Float"))
			return Float.class;
		if (type.equals("Byte"))
			return Byte.class;
		if (type.equals("Char") || type.equals("Character"))
			return Character.class;
		if (type.equals("Boolean"))
			return Boolean.class;
		if (type.equals("Date"))
			return  Date.class;
		if (type.equals("Time"))
			return  Time.class;
		if (type.equals("DateTime"))
			return  Timestamp.class;
		if (type.equals("Object"))
			return Object.class;
		if (type.equals("short"))
			return Short.TYPE;
		if (type.equals("int"))
			return Integer.TYPE;
		if (type.equals("long"))
			return Long.TYPE;
		if (type.equals("double"))
			return Double.TYPE;
		if (type.equals("float"))
			return Float.TYPE;
		if (type.equals("byte"))
			return Byte.TYPE;
		if (type.equals("char"))
			return Character.TYPE;
		if (type.equals("boolean"))
			return Boolean.TYPE;
		try {
			return loadClass(type);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static String getTypeDefine(String type) throws Exception {
		if (type.equalsIgnoreCase("String"))
			return "DataType.DATATYPE_STRING";
		if (type.equalsIgnoreCase("Short"))
			return "DataType.DATATYPE_SHORT";
		if (type.equalsIgnoreCase("Integer"))
			return "DataType.DATATYPE_INTEGER";
		if (type.equalsIgnoreCase("Long"))
			return "DataType.DATATYPE_LONG";
		if (type.equalsIgnoreCase("Double"))
			return "DataType.DATATYPE_DOUBLE";
		if (type.equalsIgnoreCase("Float"))
			return "DataType.DATATYPE_FLOAT";
		if (type.equalsIgnoreCase("Byte"))
			return "DataType.DATATYPE_BYTE";
		if (type.equalsIgnoreCase("Char"))
			return "DataType.DATATYPE_CHAR";
		if (type.equalsIgnoreCase("Boolean"))
			return "DataType.DATATYPE_BOOLEAN";
		if (type.equalsIgnoreCase("Date"))
			return "DataType.DATATYPE_DATE";
		if (type.equalsIgnoreCase("Time"))
			return "DataType.DATATYPE_TIME";
		if (type.equalsIgnoreCase("DateTime"))
			return "DataType.DATATYPE_DATETIME";
		else
			return type;
	}

	public static String getTransFunc(String type) throws Exception {
		if (type.equalsIgnoreCase("String"))
			return "getAsString";
		if (type.equalsIgnoreCase("Short"))
			return "getAsShort";
		if (type.equalsIgnoreCase("Integer"))
			return "getAsInt";
		if (type.equalsIgnoreCase("Long"))
			return "getAsLong";
		if (type.equalsIgnoreCase("Double"))
			return "getAsDouble";
		if (type.equalsIgnoreCase("Float"))
			return "getAsFloat";
		if (type.equalsIgnoreCase("Byte"))
			return "getAsByte";
		if (type.equalsIgnoreCase("Char"))
			return "getAsChar";
		if (type.equalsIgnoreCase("Boolean"))
			return "getAsBoolean";
		if (type.equalsIgnoreCase("Date"))
			return "getAsDate";
		if (type.equalsIgnoreCase("Time"))
			return "getAsTime";
		if (type.equalsIgnoreCase("DateTime"))
			return "getAsDateTime";
		else
			return "getObject";
	}

	public static String getSimpleDataType(String type) throws Exception {
		if (type.equalsIgnoreCase("String"))
			return "String";
		if (type.equalsIgnoreCase("Short"))
			return "short";
		if (type.equalsIgnoreCase("Integer"))
			return "int";
		if (type.equalsIgnoreCase("Long"))
			return "long";
		if (type.equalsIgnoreCase("Double"))
			return "double";
		if (type.equalsIgnoreCase("Float"))
			return "float";
		if (type.equalsIgnoreCase("Byte"))
			return "byte";
		if (type.equalsIgnoreCase("Char"))
			return "char";
		if (type.equalsIgnoreCase("Boolean"))
			return "boolean";
		if (type.equalsIgnoreCase("Date"))
			return "Date";
		if (type.equalsIgnoreCase("Time"))
			return "Time";
		if (type.equalsIgnoreCase("DateTime"))
			return "Timestamp";
		else
			return type;
	}

	public static String getDataTypeBySimple(String type) throws Exception {
		if (type.equalsIgnoreCase("short"))
			return "Short";
		if (type.equalsIgnoreCase("int"))
			return "Integer";
		if (type.equalsIgnoreCase("long"))
			return "Long";
		if (type.equalsIgnoreCase("double"))
			return "Double";
		if (type.equalsIgnoreCase("float"))
			return "Float";
		if (type.equalsIgnoreCase("byte"))
			return "Byte";
		if (type.equalsIgnoreCase("char"))
			return "Char";
		if (type.equalsIgnoreCase("boolean"))
			return "Boolean";
		if (type.equalsIgnoreCase("Date"))
			return "Date";
		if (type.equalsIgnoreCase("Time"))
			return "Time";
		if (type.equalsIgnoreCase("Timestamp"))
			return "DateTime";
		if (type.equalsIgnoreCase("java.sql.Timestamp"))
			return "DateTime";
		if (type.equalsIgnoreCase("java.util.Date"))
			return "Date";
		else
			return type;
	}

	public static boolean isSimpleDataType(String type) {
		if (type.equalsIgnoreCase("String"))
			return false;
		if (type.equalsIgnoreCase("Short"))
			return true;
		if (type.equalsIgnoreCase("short"))
			return true;
		if (type.equalsIgnoreCase("Integer"))
			return true;
		if (type.equalsIgnoreCase("int"))
			return true;
		if (type.equalsIgnoreCase("Long"))
			return true;
		if (type.equalsIgnoreCase("long"))
			return true;
		if (type.equalsIgnoreCase("Double"))
			return true;
		if (type.equalsIgnoreCase("double"))
			return true;
		if (type.equalsIgnoreCase("Float"))
			return true;
		if (type.equalsIgnoreCase("float"))
			return true;
		if (type.equalsIgnoreCase("Byte"))
			return true;
		if (type.equalsIgnoreCase("byte"))
			return true;
		if (type.equalsIgnoreCase("Char"))
			return true;
		if (type.equalsIgnoreCase("char"))
			return true;
		if (type.equalsIgnoreCase("Boolean"))
			return true;
		if (type.equalsIgnoreCase("boolean"))
			return true;
		if (type.equalsIgnoreCase("Date"))
			return false;
		if (type.equalsIgnoreCase("Time"))
			return false;
		return !type.equalsIgnoreCase("DateTime") ? false : false;
	}

	public static Class getSimpleDataType(Class aClass) {
		if (Integer.class.equals(aClass))
			return Integer.TYPE;
		if (Short.class.equals(aClass))
			return Short.TYPE;
		if (Long.class.equals(aClass))
			return Long.TYPE;
		if (Double.class.equals(aClass))
			return Double.TYPE;
		if (Float.class.equals(aClass))
			return Float.TYPE;
		if (Byte.class.equals(aClass))
			return Byte.TYPE;
		if (Character.class.equals(aClass))
			return Character.TYPE;
		if (Boolean.class.equals(aClass))
			return Boolean.TYPE;
		else
			return aClass;
	}

	public static String getNullValueString(String type) {
		if (type.equalsIgnoreCase("String"))
			return "null";
		if (type.equalsIgnoreCase("Short"))
			return "(short)0";
		if (type.equalsIgnoreCase("Integer"))
			return "0";
		if (type.equalsIgnoreCase("Long"))
			return "0";
		if (type.equalsIgnoreCase("Double"))
			return "0";
		if (type.equalsIgnoreCase("Float"))
			return "0";
		if (type.equalsIgnoreCase("Byte"))
			return "((byte)0)";
		if (type.equalsIgnoreCase("Char"))
			return "((char)0)";
		if (type.equalsIgnoreCase("Boolean"))
			return "false";
		if (type.equalsIgnoreCase("Date"))
			return "null";
		if (type.equalsIgnoreCase("Time"))
			return "null";
		if (type.equalsIgnoreCase("DateTime"))
			return "null";
		else
			return "null";
	}

	public static String getNullValueString(Class type) {
		if (type.equals(Short.TYPE))
			return "(short)0";
		if (type.equals(Integer.TYPE))
			return "0";
		if (type.equals(Long.TYPE))
			return "0";
		if (type.equals(Double.TYPE))
			return "0";
		if (type.equals(Float.TYPE))
			return "0";
		if (type.equals(Byte.TYPE))
			return "((byte)0)";
		if (type.equals(Character.TYPE))
			return "((char)0)";
		if (type.equals(Boolean.TYPE)) {
			return "false";
		} else {
			String msg = "No type found!";
			throw new RuntimeException(msg);
		}
	}

	public static String getToSimpleDataTypeFunction(String type) {
		if (type.equalsIgnoreCase("String"))
			return "";
		if (type.equalsIgnoreCase("Short") || type.equalsIgnoreCase("short"))
			return "shortValue";
		if (type.equalsIgnoreCase("Integer") || type.equalsIgnoreCase("int"))
			return "intValue";
		if (type.equalsIgnoreCase("Long") || type.equalsIgnoreCase("long"))
			return "longValue";
		if (type.equalsIgnoreCase("Double") || type.equalsIgnoreCase("double"))
			return "doubleValue";
		if (type.equalsIgnoreCase("Float") || type.equalsIgnoreCase("float"))
			return "floatValue";
		if (type.equalsIgnoreCase("Byte") || type.equalsIgnoreCase("byte"))
			return "byteValue";
		if (type.equalsIgnoreCase("Char") || type.equalsIgnoreCase("char"))
			return "charValue";
		if (type.equalsIgnoreCase("Boolean") || type.equalsIgnoreCase("boolean"))
			return "booleanValue";
		if (type.equalsIgnoreCase("Date"))
			return "";
		if (type.equalsIgnoreCase("Time"))
			return "";
		if (type.equalsIgnoreCase("DateTime"))
			return "";
		else
			return "";
	}

	public static String getToSimpleDataTypeFunction(Class type) {
		if (type.equals(Short.class) || type.equals(Short.TYPE))
			return "shortValue";
		if (type.equals(Integer.class) || type.equals(Integer.TYPE))
			return "intValue";
		if (type.equals(Long.class) || type.equals(Long.TYPE))
			return "longValue";
		if (type.equals(Double.class) || type.equals(Double.TYPE))
			return "doubleValue";
		if (type.equals(Float.class) || type.equals(Float.TYPE))
			return "floatValue";
		if (type.equals(Byte.class) || type.equals(Byte.TYPE))
			return "byteValue";
		if (type.equals(Character.class) || type.equals(Character.TYPE))
			return "charValue";
		if (type.equals(Boolean.class) || type.equals(Boolean.TYPE))
			return "booleanValue";
		else
			return "";
	}

	public static void setPrepareStatementParameter(PreparedStatement stmt, int index, String type, Object value) throws SQLException {
		if (type.equalsIgnoreCase("String")) {
			String content = value.toString();
			if (content.length() > 2000)
				stmt.setCharacterStream(index, new StringReader(content), content.length());
			else
				stmt.setString(index, content);
		} else if (type.equalsIgnoreCase("Short"))
			stmt.setShort(index, Short.parseShort(value.toString()));
		else if (type.equalsIgnoreCase("Integer"))
			stmt.setInt(index, Integer.parseInt(value.toString()));
		else if (type.equalsIgnoreCase("Long"))
			stmt.setLong(index, Long.parseLong(value.toString()));
		else if (type.equalsIgnoreCase("Double"))
			stmt.setDouble(index, Double.parseDouble(value.toString()));
		else if (type.equalsIgnoreCase("Float"))
			stmt.setFloat(index, Float.parseFloat(value.toString()));
		else if (type.equalsIgnoreCase("Byte"))
			stmt.setByte(index, Byte.parseByte(value.toString()));
		else if (type.equalsIgnoreCase("Char"))
			stmt.setString(index, value.toString());
		else if (type.equalsIgnoreCase("Boolean"))
			stmt.setBoolean(index, Boolean.getBoolean(value.toString()));
		else if (type.equalsIgnoreCase("Date")) {
			if (value instanceof Date)
				stmt.setDate(index, (Date) (Date) value);
			else
				stmt.setDate(index, Date.valueOf(value.toString()));
		} else if (type.equalsIgnoreCase("Time")) {
			if (value instanceof Time)
				stmt.setTime(index, (Time) (Time) value);
			else
				stmt.setTime(index, Time.valueOf(value.toString()));
		} else if (type.equalsIgnoreCase("DateTime")) {
			if (value instanceof Timestamp)
				stmt.setTimestamp(index, (Timestamp) (Timestamp) value);
			else if (value instanceof Date)
				stmt.setTimestamp(index, new Timestamp(((Date) value).getTime()));
			else
				stmt.setTimestamp(index, Timestamp.valueOf(value.toString()));
		} else if (value instanceof Character)
			stmt.setString(index, value.toString());
		else
			stmt.setObject(index, value);
	}

//	public static void setPrepareStatementParameter(CachedRowSet stmt, int index, String type, Object value) throws SQLException {
//		if (type.equalsIgnoreCase("String"))
//			stmt.setString(index, value.toString());
//		else if (type.equalsIgnoreCase("Short"))
//			stmt.setInt(index, Short.parseShort(value.toString()));
//		else if (type.equalsIgnoreCase("Integer"))
//			stmt.setInt(index, Integer.parseInt(value.toString()));
//		else if (type.equalsIgnoreCase("Long"))
//			stmt.setLong(index, Long.parseLong(value.toString()));
//		else if (type.equalsIgnoreCase("Double"))
//			stmt.setDouble(index, Double.parseDouble(value.toString()));
//		else if (type.equalsIgnoreCase("Float"))
//			stmt.setFloat(index, Float.parseFloat(value.toString()));
//		else if (type.equalsIgnoreCase("Byte"))
//			stmt.setByte(index, Byte.parseByte(value.toString()));
//		else if (type.equalsIgnoreCase("Char"))
//			stmt.setString(index, value.toString());
//		else if (type.equalsIgnoreCase("Boolean"))
//			stmt.setBoolean(index, Boolean.getBoolean(value.toString()));
//		else if (type.equalsIgnoreCase("Date")) {
//			if (value instanceof Date)
//				stmt.setDate(index, (Date) (Date) value);
//			else
//				stmt.setDate(index, Date.valueOf(value.toString()));
//		} else if (type.equalsIgnoreCase("Time")) {
//			if (value instanceof Time)
//				stmt.setTime(index, (Time) (Time) value);
//			else
//				stmt.setTime(index, Time.valueOf(value.toString()));
//		} else if (type.equalsIgnoreCase("DateTime")) {
//			if (value instanceof Timestamp)
//				stmt.setTimestamp(index, (Timestamp) (Timestamp) value);
//			else if (value instanceof Date)
//				stmt.setTimestamp(index, new Timestamp(((Date) value).getTime()));
//			else
//				stmt.setTimestamp(index, Timestamp.valueOf(value.toString()));
//		} else if (value instanceof Character)
//			stmt.setString(index, value.toString());
//		else
//			stmt.setObject(index, value);
//	}

	/**
	 * 格式化时间或者浮点值
	 * @param value 值对象 
	 * @param type 类型名(simpleName)Date"yyyy-MM-dd"，Time"HH:mm:ss"，DateTime"yyyy-MM-dd HH:mm:ss"
	 * @param precision 如果value是Double或Float类型，则为精度
	 * @return
	 */
	public static String transferToString(Object value, String type, int precision) {
		if (value == null)
			return "";
		String result = "";
		if (type.equalsIgnoreCase("Date")) {
			if ((value instanceof java.util.Date) || (value instanceof Timestamp))
				try {
					SimpleDateFormat DATA_FORMAT_yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
					result = DATA_FORMAT_yyyyMMdd.format(value);
				} catch (Exception e) {
					e.printStackTrace();
					result = "";
				}
			else
				result = value.toString();
		} else if (type.equalsIgnoreCase("Time")) {
			if ((value instanceof java.util.Date) || (value instanceof Time) || (value instanceof Timestamp))
				try {
					SimpleDateFormat DATA_FORMAT_HHmmss = new SimpleDateFormat("HH:mm:ss");
					result = DATA_FORMAT_HHmmss.format(value);
				} catch (Exception e) {
					e.printStackTrace();
					result = "";
				}
			else
				result = value.toString();
		} else if (type.equalsIgnoreCase("DateTime")) {
			if ((value instanceof java.util.Date) || (value instanceof Timestamp))
				try {
					SimpleDateFormat DATA_FORMAT_yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					result = DATA_FORMAT_yyyyMMddHHmmss.format(value);
				} catch (Exception e) {
					e.printStackTrace();
					result = "";
				}
			else
				result = value.toString();
		} else if (type.equalsIgnoreCase("Double") || type.equalsIgnoreCase("Float")) {
			NumberFormat nf = NumberFormat.getInstance();
			if (precision >= 0)
				try {
					nf.setMaximumFractionDigits(precision);
					nf.setGroupingUsed(false);
					result = nf.format(nf.parse(value.toString()).doubleValue());
				} catch (Exception ex) {
					ex.printStackTrace();
					result = value.toString();
				}
			else
				result = value.toString();
		} else {
			result = value.toString();
		}
		return result;
	}

	public static Object transfer(Object value, Class type) {
		if (value == null)
			return null;
		if ((value instanceof String) && value.toString().trim().equals(""))
			if (String.class.equals(type))
				return value;
			else
				return null;
		if (type.equals(Short.class) || type.equals(Short.TYPE))
			if (value instanceof Short)
				return value;
			else
				return new Short((new BigDecimal(value.toString())).shortValue());
		if (type.equals(Integer.class) || type.equals(Integer.TYPE))
			if (value instanceof Integer)
				return value;
			else
				return new Integer((new BigDecimal(value.toString())).intValue());
		if (type.equals(Character.class) || type.equals(Character.TYPE))
			if (value instanceof Character)
				return value;
			else
				return new Character(value.toString().charAt(0));
		if (type.equals(Long.class) || type.equals(Long.TYPE))
			if (value instanceof Long)
				return value;
			else
				return new Long((new BigDecimal(value.toString())).longValue());
		if (type.equals(String.class))
			if (value instanceof String)
				return value;
			else
				return value.toString();
		if (type.equals( Date.class)) {
			if (value instanceof Date)
				return value;
			if (value instanceof java.util.Date)
				return new Date(((java.util.Date) value).getTime());
			try {
				SimpleDateFormat a = new SimpleDateFormat("yyyy-MM-dd");
				return new Date(a.parse(value.toString()).getTime());
			} catch (Exception e) {
				String msg ="This Type cann't transfered";
				throw new RuntimeException(msg);
			}
		}
		if (type.equals( Time.class)) {
			if (value instanceof Time)
				return value;
			if (value instanceof java.util.Date)
				return new Time(((java.util.Date) value).getTime());
			String msg;
			try {
				SimpleDateFormat a = new SimpleDateFormat("HH:mm:ss");
				return new Time(a.parse(value.toString()).getTime());
			} catch (Exception e) {
				msg ="No type can be transformed!";
			}
			throw new RuntimeException(msg);
		}
		if (type.equals( Timestamp.class)) {
			if (value instanceof Timestamp)
				return value;
			if (value instanceof java.util.Date)
				return new Timestamp(((java.util.Date) value).getTime());
			String msg;
			try {
				SimpleDateFormat a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String tmpstr = value.toString();
				if (tmpstr.trim().length() <= 10)
					tmpstr = (new StringBuilder()).append(tmpstr).append(" 00:00:00").toString();
				return new Timestamp(a.parse(tmpstr).getTime());
			} catch (Exception e) {
				msg = "No type can be transformed!";
			}
			throw new RuntimeException(msg);
		}
		if (type.equals(Double.class) || type.equals(Double.TYPE))
			if (value instanceof Double)
				return value;
			else
				return new Double((new BigDecimal(value.toString())).doubleValue());
		if (type.equals(Float.class) || type.equals(Float.TYPE))
			if (value instanceof Float)
				return value;
			else
				return new Float((new BigDecimal(value.toString())).floatValue());
		if (type.equals(Byte.class) || type.equals(Byte.TYPE))
			if (value instanceof Byte)
				return value;
			else
				return new Byte((new BigDecimal(value.toString())).byteValue());
		if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
			if (value instanceof Boolean)
				return value;
			if (value instanceof Number)
				if (((Number) value).doubleValue() > 0.0D)
					return new Boolean(true);
				else
					return new Boolean(false);
			if (value instanceof String) {
				if (((String) value).equalsIgnoreCase("true") || ((String) value).equalsIgnoreCase("y"))
					return new Boolean(true);
				else
					return new Boolean(false);
			} else {
				String msg = "No type can be transformed!";
				throw new RuntimeException(msg);
			}
		} else {
			return value;
		}
	}

	public static String transferToString(Object value, String type) {
		return transferToString(value, type, -1);
	}

	public static Object transfer(Object value, String type) {
		if (value == null)
			return null;
		if ((value instanceof String) && value.toString().trim().equals(""))
			if ("String".equalsIgnoreCase(type))
				return value;
			else
				return null;
		if (type.equalsIgnoreCase("Short") || type.equalsIgnoreCase("short"))
			if (value instanceof Short)
				return value;
			else
				return new Short((new BigDecimal(value.toString())).shortValue());
		if (type.equalsIgnoreCase("Integer") || type.equalsIgnoreCase("int"))
			if (value instanceof Integer)
				return value;
			else
				return new Integer((new BigDecimal(value.toString())).intValue());
		if (type.equalsIgnoreCase("Char") || type.equalsIgnoreCase("char"))
			if (value instanceof Character)
				return value;
			else
				return new Character(value.toString().charAt(0));
		if (type.equalsIgnoreCase("Long") || type.equalsIgnoreCase("long"))
			if (value instanceof Long)
				return value;
			else
				return new Long((new BigDecimal(value.toString())).longValue());
		if (type.equalsIgnoreCase("String"))
			if (value instanceof String)
				return value;
			else
				return value.toString();
		if (type.equalsIgnoreCase("Date")) {
			if (value instanceof Date)
				return value;
			if (value instanceof Timestamp)
				return new Date(((Timestamp) value).getTime());
			try {
				String tmpstr = value.toString().replace('/', '-');
				SimpleDateFormat DATA_FORMAT_yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
				return new Date(DATA_FORMAT_yyyyMMdd.parse(tmpstr).getTime());
			} catch (Exception ex) {
				if (ex instanceof RuntimeException) {
					throw (RuntimeException) ex;
				} else {
					String msg = "No type can be transformed!";
					throw new RuntimeException(msg, ex);
				}
			}
		}
		if (type.equalsIgnoreCase("Time")) {
			if (value instanceof Time)
				return value;
			if (value instanceof Timestamp)
				return new Time(((Timestamp) value).getTime());
			try {
				SimpleDateFormat DATA_FORMAT_HHmmss = new SimpleDateFormat("HH:mm:ss");
				return new Time(DATA_FORMAT_HHmmss.parse(value.toString()).getTime());
			} catch (Exception e) {
				String msg = "No type can be transformed!";
				throw new RuntimeException(msg, e);
			}
		}
		if (type.equalsIgnoreCase("DateTime")) {
			if (value instanceof Timestamp)
				return value;
			if (value instanceof java.util.Date)
				return new Timestamp(((java.util.Date) value).getTime());
			String msg;
			try {
				SimpleDateFormat a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String tmpstr = value.toString();
				if (tmpstr.trim().length() <= 10)
					tmpstr = (new StringBuilder()).append(tmpstr).append(" 00:00:00").toString();
				return new Timestamp(a.parse(tmpstr).getTime());
			} catch (Exception e) {
				msg = "No type can be transformed!";
			}
			throw new RuntimeException(msg);
		}
		if (type.equalsIgnoreCase("Double") || type.equalsIgnoreCase("double"))
			if (value instanceof Double)
				return value;
			else
				return new Double((new BigDecimal(value.toString())).doubleValue());
		if (type.equalsIgnoreCase("Float") || type.equalsIgnoreCase("float"))
			if (value instanceof Float)
				return value;
			else
				return new Float((new BigDecimal(value.toString())).floatValue());
		if (type.equalsIgnoreCase("Byte") || type.equalsIgnoreCase("byte"))
			if (value instanceof Byte)
				return value;
			else
				return new Byte((new BigDecimal(value.toString())).byteValue());
		if (type.equalsIgnoreCase("Boolean") || type.equalsIgnoreCase("boolean")) {
			if (value instanceof Boolean)
				return value;
			if (value instanceof Number)
				if (((Number) value).doubleValue() > 0.0D)
					return new Boolean(true);
				else
					return new Boolean(false);
			if (value instanceof String) {
				if (((String) value).equalsIgnoreCase("true") || ((String) value).equalsIgnoreCase("y"))
					return new Boolean(true);
				else
					return new Boolean(false);
			} else {
				String msg = "No type can be transformed!";
				throw new RuntimeException(msg);
			}
		} else {
			return value;
		}
	}

	public static String getAsString(Object obj) {
		if (obj == null)
			return null;
		else
			return obj.toString();
	}

	public static short getAsShort(Object obj) {
		if (obj == null)
			return 0;
		if (obj instanceof Number)
			return ((Number) obj).shortValue();
		else
			return ((Short) transfer(obj, Short.class)).shortValue();
	}

	public static int getAsInt(Object obj) {
		if (obj == null)
			return 0;
		if (obj instanceof Number)
			return ((Number) obj).intValue();
		else
			return ((Integer) transfer(obj, Integer.class)).intValue();
	}

	public static long getAsLong(Object obj) {
		if (obj == null)
			return 0L;
		if (obj instanceof Number)
			return ((Number) obj).longValue();
		else
			return ((Long) transfer(obj, Long.class)).longValue();
	}

	public static double getAsDouble(Object obj) {
		if (obj == null)
			return 0.0D;
		if (obj instanceof Number)
			return ((Number) obj).doubleValue();
		else
			return ((Double) transfer(obj, Double.class)).doubleValue();
	}

	public static float getAsFloat(Object obj) {
		if (obj == null)
			return 0.0F;
		if (obj instanceof Number)
			return ((Number) obj).floatValue();
		else
			return ((Float) transfer(obj, Float.class)).floatValue();
	}

	public static byte getAsByte(Object obj) {
		if (obj == null)
			return 0;
		if (obj instanceof Number)
			return ((Number) obj).byteValue();
		else
			return ((Byte) transfer(obj, Byte.class)).byteValue();
	}

	public static boolean getAsBoolean(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof Boolean)
			return ((Boolean) obj).booleanValue();
		else
			return ((Boolean) transfer(obj, Boolean.class)).booleanValue();
	}

	public static char getAsChar(Object obj) {
		if (obj == null)
			return '\0';
		if (obj instanceof Character)
			return ((Character) obj).charValue();
		if ((obj instanceof String) && ((String) obj).length() == 1)
			return ((String) obj).charAt(0);
		else
			return ((Character) transfer(obj, Character.class)).charValue();
	}

	public static Date getAsDate(Object obj) {
		if (obj == null)
			return null;
		if (obj instanceof Date)
			return (Date) obj;
		if (obj instanceof Timestamp) {
			return new Date(((Timestamp) obj).getTime());
		} else {
			String msg =  "No type can be transformed!";
			throw new RuntimeException(msg);
		}
	}

	public static Time getAsTime(Object obj) {
		if (obj == null)
			return null;
		if (obj instanceof Time)
			return (Time) obj;
		if (obj instanceof Timestamp) {
			return new Time(((Timestamp) obj).getTime());
		} else {
			String msg =  "No type can be transformed!";
			throw new RuntimeException(msg);
		}
	}

	public static Timestamp getAsTimestamp(Object obj) {
		if (obj == null)
			return null;
		if (obj instanceof Timestamp)
			return (Timestamp) obj;
		if (obj instanceof Date) {
			return new Timestamp(((Date) obj).getTime());
		} else {
			String msg =  "No type can be transformed!";
			throw new RuntimeException(msg);
		}
	}


	public static String getClassName(Class className) {
		String name = className.getName();
		return getClassName(name);
	}

	public static String getClassName(String name) {
		String arrays = "";
		if (name.indexOf("[") >= 0) {
			int point;
			for (point = 0; name.charAt(point) == '['; point++)
				arrays = (new StringBuilder()).append(arrays).append("[]").toString();

			if (name.charAt(point) == 'L')
				name = name.substring(point + 1, name.length() - 1);
			else if (name.charAt(point) == 'Z')
				name = "boolean";
			else if (name.charAt(point) == 'B')
				name = "byte";
			else if (name.charAt(point) == 'C')
				name = "char";
			else if (name.charAt(point) == 'D')
				name = "double";
			else if (name.charAt(point) == 'F')
				name = "float";
			else if (name.charAt(point) == 'I')
				name = "int";
			else if (name.charAt(point) == 'J')
				name = "long";
			else if (name.charAt(point) == 'S')
				name = "short";
		}
		int index = name.lastIndexOf('.');
		if (index > 0 && name.substring(0, index).equals("java.lang"))
			name = name.substring(index + 1);
		name = (new StringBuilder()).append(name).append(arrays).toString();
		return name;
	}

	public static String[] getDataTypeNames() {
		return (new String[] { "String", "Short", "Integer", "Long", "Double", "Float", "Byte", "Char", "Boolean", "Date", "Time", "DateTime", "Object", "short", "int", "long", "long", "float", "byte", "char", "boolean", "UserInfoInterface" });
	}

	public static Class getPrimitiveClass(Class type) {
		if (type.equals(Short.TYPE))
			return Short.class;
		if (type.equals(Integer.TYPE))
			return Integer.class;
		if (type.equals(Long.TYPE))
			return Long.class;
		if (type.equals(Double.TYPE))
			return Double.class;
		if (type.equals(Float.TYPE))
			return Float.class;
		if (type.equals(Byte.TYPE))
			return Byte.class;
		if (type.equals(Character.TYPE))
			return Character.class;
		if (type.equals(Boolean.TYPE))
			return Boolean.class;
		else
			return type;
	}

	public static Class getSimpleClass(Class type) {
		if (type.equals(Short.class))
			return Short.TYPE;
		if (type.equals(Integer.class))
			return Integer.TYPE;
		if (type.equals(Long.class))
			return Long.TYPE;
		if (type.equals(Double.class))
			return Double.TYPE;
		if (type.equals(Float.class))
			return Float.TYPE;
		if (type.equals(Byte.class))
			return Byte.TYPE;
		if (type.equals(Character.class))
			return Character.TYPE;
		if (type.equals(Boolean.class))
			return Boolean.TYPE;
		else
			return type;
	}

	public static String getPrimitiveClass(String type) {
		if (type.equals("short"))
			return Short.class.getName();
		if (type.equals("int"))
			return Integer.class.getName();
		if (type.equals("long"))
			return Long.class.getName();
		if (type.equals("double"))
			return Double.class.getName();
		if (type.equals("float"))
			return Float.class.getName();
		if (type.equals("byte"))
			return Byte.class.getName();
		if (type.equals("char"))
			return Character.class.getName();
		if (type.equals("boolean"))
			return Boolean.class.getName();
		else
			return type;
	}

	public static Method findMethod(Class baseClass, String methodName, Class types[], boolean publicOnly, boolean isStatic) {
		Vector candidates = gatherMethodsRecursive(baseClass, methodName, types.length, publicOnly, isStatic, null);
		Method method = findMostSpecificMethod(types, (Method[]) (Method[]) candidates.toArray(new Method[0]));
		return method;
	}

	static Constructor findConstructor(Class baseClass, Class types[]) {
		Constructor constructors[] = baseClass.getConstructors();
		Class candidateSigs[][] = new Class[constructors.length][];
		List list = new ArrayList();
		for (int i = 0; i < constructors.length; i++)
			if (constructors[i].getParameterTypes().length == types.length)
				list.add(constructors[i].getParameterTypes());

		int match = findMostSpecificSignature(types, (Class[][]) (Class[][]) list.toArray(new Class[0][]));
		return match != -1 ? constructors[match] : null;
	}

	static int findMostSpecificSignature(Class idealMatch[], Class candidates[][]) {
		Class bestMatch[] = null;
		int bestMatchIndex = -1;
		for (int i = candidates.length - 1; i >= 0; i--) {
			Class targetMatch[] = candidates[i];
			if (isSignatureAssignable(idealMatch, targetMatch) && (bestMatch == null || isSignatureAssignable(targetMatch, bestMatch))) {
				bestMatch = targetMatch;
				bestMatchIndex = i;
			}
		}

		if (bestMatch != null)
			return bestMatchIndex;
		else
			return -1;
	}

	static boolean isSignatureAssignable(Class from[], Class to[]) {
		for (int i = 0; i < from.length; i++)
			if (!isAssignable(to[i], from[i]))
				return false;

		return true;
	}

	public static boolean isAssignable(Class dest, Class sour) {
		if (dest == sour)
			return true;
		if (dest == null)
			return false;
		if (sour == null)
			return !dest.isPrimitive();
		if (dest.isPrimitive() && sour.isPrimitive()) {
			if (dest == sour)
				return true;
			if (sour == Byte.TYPE && (dest == Short.TYPE || dest == Integer.TYPE || dest == Long.TYPE || dest == Float.TYPE || dest == Double.TYPE))
				return true;
			if (sour == Short.TYPE && (dest == Integer.TYPE || dest == Long.TYPE || dest == Float.TYPE || dest == Double.TYPE))
				return true;
			if (sour == Character.TYPE && (dest == Integer.TYPE || dest == Long.TYPE || dest == Float.TYPE || dest == Double.TYPE))
				return true;
			if (sour == Integer.TYPE && (dest == Long.TYPE || dest == Float.TYPE || dest == Double.TYPE))
				return true;
			if (sour == Long.TYPE && (dest == Float.TYPE || dest == Double.TYPE))
				return true;
			if (sour == Float.TYPE && dest == Double.TYPE)
				return true;
		} else if (dest.isAssignableFrom(sour))
			return true;
		return false;
	}

	static Method findMostSpecificMethod(Class idealMatch[], Method methods[]) {
		Class candidateSigs[][] = new Class[methods.length][];
		for (int i = 0; i < methods.length; i++)
			candidateSigs[i] = methods[i].getParameterTypes();

		int match = findMostSpecificSignature(idealMatch, candidateSigs);
		return match != -1 ? methods[match] : null;
	}

	private static Vector gatherMethodsRecursive(Class baseClass, String methodName, int numArgs, boolean publicOnly, boolean isStatic, Vector candidates) {
		if (candidates == null)
			candidates = new Vector();
		addCandidates(baseClass.getDeclaredMethods(), methodName, numArgs, publicOnly, isStatic, candidates);
		Class intfs[] = baseClass.getInterfaces();
		for (int i = 0; i < intfs.length; i++)
			gatherMethodsRecursive(intfs[i], methodName, numArgs, publicOnly, isStatic, candidates);

		Class superclass = baseClass.getSuperclass();
		if (superclass != null)
			gatherMethodsRecursive(superclass, methodName, numArgs, publicOnly, isStatic, candidates);
		return candidates;
	}

	private static Vector addCandidates(Method methods[], String methodName, int numArgs, boolean publicOnly, boolean isStatic, Vector candidates) {
		for (int i = 0; i < methods.length; i++) {
			Method m = methods[i];
			if (m.getName().equals(methodName) && m.getParameterTypes().length == numArgs && (!publicOnly || isPublic(m) && (!isStatic || isStatic(m))))
				candidates.add(m);
		}

		return candidates;
	}

	private static boolean isPublic(Class c) {
		return Modifier.isPublic(c.getModifiers());
	}

	private static boolean isPublic(Method m) {
		return Modifier.isPublic(m.getModifiers());
	}

	private static boolean isStatic(Method m) {
		return Modifier.isStatic(m.getModifiers());
	}

	public static void main(String args[]) throws Exception {
		String abc = "2099-1-1 01:30:30";
	}

	public static final String DATATYPE_STRING = "String";
	public static final String DATATYPE_SHORT = "Short";
	public static final String DATATYPE_INTEGER = "Integer";
	public static final String DATATYPE_LONG = "Long";
	public static final String DATATYPE_DOUBLE = "Double";
	public static final String DATATYPE_FLOAT = "Float";
	public static final String DATATYPE_BYTE = "Byte";
	public static final String DATATYPE_CHAR = "Char";
	public static final String DATATYPE_BOOLEAN = "Boolean";
	public static final String DATATYPE_DATE = "Date";
	public static final String DATATYPE_TIME = "Time";
	public static final String DATATYPE_DATETIME = "DateTime";
	public static final String DATATYPE_OBJECT = "Object";
	public static final String DATATYPE_short = "short";
	public static final String DATATYPE_int = "int";
	public static final String DATATYPE_long = "long";
	public static final String DATATYPE_double = "double";
	public static final String DATATYPE_float = "float";
	public static final String DATATYPE_byte = "byte";
	public static final String DATATYPE_char = "char";
	public static final String DATATYPE_boolean = "boolean";
}


