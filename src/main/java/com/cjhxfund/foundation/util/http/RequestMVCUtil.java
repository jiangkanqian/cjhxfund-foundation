package com.cjhxfund.foundation.util.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.cjhxfund.foundation.annotation.data.ParamItem;
import com.cjhxfund.foundation.util.data.DataType;
import com.cjhxfund.foundation.util.data.DataUtil;
import com.cjhxfund.foundation.util.str.EncodeUtil;
import com.cjhxfund.foundation.util.str.JsonUtil;
import com.cjhxfund.foundation.web.model.HttpCtrl;
import com.cjhxfund.foundation.web.model.sys.UserInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * springmvc 请求的工具类，用于处理请求参数
 * 
 * @author xiejs
 * @date 2015年6月27日
 */
public class RequestMVCUtil {

	public static void fillData(HttpCtrl httpCtrl) {
		HttpServletRequest request = httpCtrl.getRequest();
		String requestMethod = request.getMethod();
		getRequestBody(httpCtrl);
		String requestJson = "";
		if (requestMethod.equalsIgnoreCase("get")) {
			requestJson = JsonUtil.toJson(httpCtrl.getQueryMap());
		}
		else {
			String contentType = request.getContentType();
			if (DataUtil.isValidStr(contentType)) {
				String result = "";
				if (contentType.contains("application/json")) {
					result = RequestMVCUtil.requestToObj(request, httpCtrl.getQueryMap());
				}
				else if (contentType.contains("application/x-www-form-urlencoded")) {
					result = RequestMVCUtil.requestFormToObj(request, httpCtrl.getQueryMap());
				}
				else if (contentType.contains("text/xml") || contentType.contains("application/xml")) {
					result = RequestMVCUtil.parseXml(request, httpCtrl.getQueryMapStr());
				}
				if (DataUtil.isValidStr(result)) {
					requestJson = result;
				}
				else {
					requestJson = JsonUtil.toJson(httpCtrl.getQueryMap());
				}
			}
			else {
				requestJson = JsonUtil.toJson(httpCtrl.getQueryMap());
			}
		}
		httpCtrl.setRequestJson(requestJson);
		// else if (requestMethod.equalsIgnoreCase("delete")) {
		// // delete 方式做特殊处理
		// // String requestJson = RequestMVCUtil.requestToArray(request);
		// String requestJson = RequestMVCUtil.requestToObj(request, httpCtrl.getQueryMap());
		// httpCtrl.setRequestJson(requestJson);
		// }

	}

	/**
	 * 将请求参数装换为map对象.
	 * 
	 * @param request
	 * @return Map<String,Object> ：查询条件
	 */
	public static void getRequestBody(HttpCtrl httpCtrl) {
		Map<String, Object> condition = httpCtrl.getQueryMap();
		Map<String, String[]> reqMap = httpCtrl.getRequest().getParameterMap();
		Iterator<Entry<String, String[]>> it = reqMap.entrySet().iterator();
		List<String> paramList = new ArrayList<String>();
		while (it.hasNext()) {
			paramList.add(((Entry<String, String[]>) it.next()).getKey().toString());// 获取参数名
		}
		if (DataUtil.isValidList(paramList)) {
			// String url=request.getRequestURI();
			String value = "";
			for (String name : paramList) {
				value = httpCtrl.getRequest().getParameter(name);// 根据参数名获取参数值
				if (DataUtil.isValidStr(value)) {
					value = EncodeUtil.decodeUTF8(value);// utf-8解码
					if (name.endsWith("Date")) {
						value = value.replaceAll("-", "").replaceAll(" ", "");
						condition.put(name, Integer.valueOf(value));
					}
					else if (name.endsWith("Time")) {
						value = value.replaceAll(":", "").replaceAll(" ", "");
						condition.put(name, Integer.valueOf(value));
					}
					else {
						// 其他都默认为String
						condition.put(name, value.trim());
					}
				}
			}
		}
		// 如果有分页查询
		if (condition.get("page") != null) {
			int rows = (DataType.getAsInt(condition.get("rows")));
			int start = (DataType.getAsInt(condition.get("page")) - 1) * rows;
			condition.put("start", start + 1);
			condition.put("endNum", rows + start);
		}

		// 如果有排序，则处理排序
		sortCondition(condition);

		UserInfo user = httpCtrl.getUserInfo();
		Long userId = 0L;
		if (user != null) {
			userId = user.getUserId();
			// 如果参数中自带了projId则不需要，否则就加上
			if (user.getProjId() != null && condition.get("projId") == null) {
				condition.put("projId", user.getProjId());
			}
		}
		// 操作者的ID自动带上
		condition.put("opId", userId);
	}

	/**
	 * post或者put请求来获取参数，如果前端传的是对象，则数据会是name=value&……的显示方式
	 * @param request
	 * @param condi 数据容器
	 * @return
	 */
	public static String requestFormToObj(HttpServletRequest request, Map<String, Object> condi) {
		String requestBody = "";
		try {
			requestBody = RequestUtil.getBodyString(request.getReader());
			// String encoding = request.getCharacterEncoding();
			if (DataUtil.isValidStr(requestBody)) {
				requestBody = EncodeUtil.decodeUTF8(requestBody);
				String[] paramArr = requestBody.split("&");
				int length = paramArr.length;
				for (int i = 0; i < length; i++) {
					String[] keyV = paramArr[i].split("=");
					if (keyV.length == 2) {
						condi.put(keyV[0], keyV[1]);
					}
				}
				return JsonUtil.toJson(condi);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * post或者put请求来获取参数，并转化为相应的字符串
	 * @param request
	 * @return
	 */
	public static String requestToObj(HttpServletRequest request, Map<String, Object> condi) {
		String requestBody = "";
		// String url = request.getRequestURI();

		try {
			requestBody = RequestUtil.getBodyString(request.getReader());
			// System.out.println("requestBody:"+requestBody);
			if (!DataUtil.isValidStr(requestBody)) {
				return null;
			}
			// requestBody = EncodeUtil.decodeUTF8(requestBody);
			Long userId = DataType.getAsLong(condi.get("opId"));
			if (requestBody.startsWith("[") && requestBody.endsWith("]")) {
				// 如果是字符串是数组形式，则字符串得转化为对象的JsonArray，处理后再转为为String
				return getJsonArray(requestBody, userId);
			}
			else {
				// 如果是字符串不是数组，则字符串得转化为对象的JsonObject，处理后再转为为String
				return getJsonObject(requestBody, userId, condi);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static String parseXml(HttpServletRequest request, Map<String, String> condi) {
		// 读取输入流
		SAXReader reader = new SAXReader();
		try {
			// 从request中取得输入流
			InputStream inputStream = request.getInputStream();
			// 得到xml根元素
			Document document = reader.read(inputStream);
			Element root = document.getRootElement();
			// 得到根元素的所有子节点
			List<Element> elementList = root.elements();
			// 遍历所有子节点
			for (Element e : elementList) {
				condi.put(e.getName(), e.getText());
			}
			// 释放资源
			inputStream.close();
			inputStream = null;
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		return JsonUtil.toJson(condi);
	}

	public static String getJsonObject(String requestJson, Long userId, Map<String, Object> condi) {
		JsonObject jo = new JsonParser().parse(requestJson).getAsJsonObject();

		jo.addProperty("opId", userId);
		// jo.addProperty("userId", userId);
		Set<Entry<String, JsonElement>> Entrys = jo.entrySet();
		Iterator<Entry<String, JsonElement>> jsonIt = Entrys.iterator();
		while (jsonIt.hasNext()) {
			Entry<String, JsonElement> entry = jsonIt.next();
			// System.out.println(entry.getKey()+" : "+ entry.getValue());
			if (entry.getValue() != null && !entry.getValue().toString().toLowerCase().equals("null")) {
				// 只有值在不为null的情况下，才可以有
				if (!entry.getValue().isJsonArray() && !entry.getValue().isJsonObject()) {
					// String elem = EncodeUtil.decodeUTF8(entry.getValue().getAsString().trim());//utf-8解码;
					// 如果在获取requestbody的时候没有encode,那就不要再次decode,实际上，在post和put的时候，传输的是数据流，一般都是系统的默认编码，不需要加工
					String elem = entry.getValue().getAsString().trim();
					if (!DataUtil.isValidStr(elem)) {
						jsonIt.remove();
					}
					else {
						condi.put(entry.getKey(), elem);
					}
				}
				else {
					// 如果是数组或对象，则直接转为字符串
					condi.put(entry.getKey(), entry.getValue().toString());
				}
			}
			else {
				jsonIt.remove();
			}
		}
		return jo.toString();
	}

	public static String getJsonArray(String requestJson, Long userId) {
		JsonArray ja = new JsonParser().parse(requestJson).getAsJsonArray();
		// String now = DateUtil.getNowDateTime();
		for (int i = 0; i < ja.size(); i++) {
			if (ja.get(i).isJsonObject()) {
				JsonObject jo = ja.get(i).getAsJsonObject();
				jo.addProperty("opId", userId);
				// jo.addProperty("userId", userId);
			}
		}
		return ja.toString();
	}

	/**
	 * 获取页面传过来的主键
	 * 
	 * @param request
	 * @param queryMap
	 * @param idList
	 *            主键数据容器
	 */
	public static String requestToArray(HttpServletRequest request) {
		String requestBody = "";
		try {
			requestBody = RequestUtil.getBodyString(request.getReader());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return requestBody;
	}

	/**
	 * 将json字符串转为list 对象
	 * 
	 * @author xiejs
	 * @date 2015年11月21日
	 * @param json
	 * @param data
	 * @param clazz
	 */
	public static <T> void jsonToListObj(String json, List<T> data, Class<T> clazz) {
		JsonArray ja = new JsonParser().parse(json).getAsJsonArray();
		for (int i = 0; i < ja.size(); i++) {
			JsonObject jo = ja.get(i).getAsJsonObject();
			data.add(JsonUtil.jsonToObj(jo.toString(), clazz));
		}
	}

	/**验证该值的有效性*/
	public static String checkVal(String value, ParamItem item) {
		// 先验证是否必传
		if (item.getRequired() == 1 && !DataUtil.isValidStr(value)) {
			return item.getField() + " :required!";
		}

		String paramType = item.getParamType();
		// 如果有值 再验证数据格式
		if (DataUtil.isValidStr(value)) {
			if (item.getMaxLength() > 0 && value.length() > item.getMaxLength()) {
				return item.getField() + ":too long!";

			}
			if (item.getMinLength() > 0 && value.length() < item.getMinLength()) {
				return item.getField() + "(" + value + "):too short!";
			}
			if (paramType.equals("String")) {
				// 如果有正则，则匹配正则
				if (DataUtil.isValidStr(item.getParamRegex())) {
					Pattern pattern = Pattern.compile(item.getParamRegex());
					Matcher matcher = pattern.matcher(value);
					if ((item.getMatchType() == 0 && !matcher.matches()) || (item.getMatchType() == 1 && !matcher.find())) {
						return item.getField() + "(" + value + "):no valid formatter!";
					}
				}
			}
			else if ((paramType.equalsIgnoreCase("Long") || paramType.equalsIgnoreCase("Integer"))) {
				if (!DataUtil.isInt(value)) {
					return item.getField() + "(" + value + "):number required!";
				}
				int minValue = item.getMinValue();
				if( minValue > -1 && Integer.valueOf(value).intValue() < minValue){
					return item.getField() + "(" + value + ") can not be less than "+minValue;
				}
				
				int maxValue = item.getMaxValue();
				if( maxValue > -1 && Integer.valueOf(value).intValue() > maxValue){
					return item.getField() + "(" + value + ") can not be more than "+maxValue;
				}
				
			}
			else if (paramType.equalsIgnoreCase("Array")) {
				if (!value.startsWith("[") || !value.endsWith("]")) {
					// 验证一般的数组格式
					return item.getField() + ": invalid array! value:" + value;
				}
				if (item.getRequired() == 1 && value.length() < 3) {
					// 如果是必传，则不能是空数组
					return item.getField() + ": empty array! value:" + value;
				}
			}
			else if (paramType.equalsIgnoreCase("positiveInt")) {
				if (!DataUtil.isInt(value)) {
					return item.getField() + "(" + value + ") : positive int required!";
				}
				int minValue = item.getMinValue();
				if( minValue > -1 && Integer.valueOf(value).intValue() < minValue){
					return item.getField() + "(" + value + ") can not be less than "+minValue;
				}
				
				int maxValue = item.getMaxValue();
				if( maxValue > -1 && Integer.valueOf(value).intValue() > maxValue){
					return item.getField() + "(" + value + ") can not be more than "+maxValue;
				}
			}
			else if (paramType.equalsIgnoreCase("Float")) {
				if (!DataUtil.isNumeric(value)) {
					return item.getField() + ": float number required!";
				}
			}
			else if (paramType.equalsIgnoreCase("JsonObject")) {
				//如果是json对象，则需要验证对象属性
				List<ParamItem> paramList = item.getSubParams();
				JsonElement je = JsonUtil.getJsonObj(value);
				if(null == je || !je.isJsonObject()){
					return item.getField() + ": is not a JSON Object!";
				}
				JsonObject jo = je.getAsJsonObject();
				String checkVal = null;
				for(ParamItem subParam : paramList){
					if(jo.get(subParam.getField()) == null){
						checkVal = checkVal("", subParam);
					}
					else{
						checkVal = checkVal(jo.get(subParam.getField()).getAsString(), subParam);
					}
					if(DataUtil.isValidStr(checkVal)){
						return checkVal;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 排序处理
	 * @param condition
	 */
	private static void sortCondition(Map<String, Object> condition) {
		if (condition.get("sort") != null) {
			String sort = condition.get("sort").toString();
			if (sort.contains(",")) {
				// 如果有排序多个排序
				String[] sortParam = condition.get("sort").toString().split(",");
				String[] orderParam = condition.get("order").toString().split(",");
				StringBuffer buf = new StringBuffer();
				// buf.append(sortParam[0]).append(" ").append(orderParam[0]);
				Map<String, String> sortMap = new TreeMap<String, String>();
				for (int i = 0, length = sortParam.length; i < length; i++) {
					if (sortParam[i].contains("-")) {
						String[] sortOrder = sortParam[i].split("-");
						if (sortOrder.length > 1) {
							sortMap.put(sortOrder[1], sortOrder[0] + " " + orderParam[i]);
						}
					}
					else {
						sortMap.put("0", sortParam[i] + " " + orderParam[i]);
					}
				}
				int i = 0;
				for (Entry<String, String> entry : sortMap.entrySet()) {
					if (i == 0) {
						buf.append(entry.getValue());
						i++;
					}
					else {
						buf.append(",").append(entry.getValue());
					}
				}
				condition.put("orderBy", buf.toString());
			}
			else {
				if (sort.contains("-")) {
					condition.put("orderBy", sort.split("-")[0] + " " + condition.get("order").toString());
				}
				else {
					condition.put("orderBy", sort + " " + condition.get("order").toString());
				}
			}
			condition.remove("sort");
			condition.remove("order");
		}
	}

}
