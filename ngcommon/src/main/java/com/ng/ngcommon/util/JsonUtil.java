package com.ng.ngcommon.util;

import android.util.Config;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * @author Simon.xin
 * @package cn.eshore.wepi.utils;
 * @date 2013-9-12 上午9:59:08
 * @version 1.0
 * @<b></b>
 */
public class JsonUtil {
	private static final Exception VJSONException = null;

	/**
	 * 将json对象转换成java对象
	 *
	 * @return
	 * @throws JSONException
	 */
	public static String getJson2String(String jsonData, String key) {

		if (null == key)
			return null;

		JSONObject obj;
		try {
			obj = new JSONObject(jsonData.trim());

			String value = obj.get(key).toString();
			if (Config.DEBUG)
				Log.i("getJson2String", value);
			return value;
		} catch (JSONException e) {
			Log.e(JsonUtil.class.toString(), e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 将java对象转换成json对象
	 *
	 * @param obj
	 * @return
	 */
	public static String parseObj2Json(Object obj) {

		if (null == obj)
			return null;

//		Gson gson = new Gson();
//		String objstr = gson.toJson(obj);
		String objstr = JSON.toJSONString(obj);
		if (Config.DEBUG)
			Log.i("parseObj2Json", objstr);
		return objstr;
	}

//	/**
//	 * 将java对象的属性转换成json的key
//	 *
//	 * @param obj
//	 * @return
//	 */
//	public static String parseObj2JsonOnField(Object obj) {
//
//		if (null == obj)
//			return null;
//
//		Gson gson = new GsonBuilder().setFieldNamingPolicy(
//				FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();
//		String objstr = gson.toJson(obj);
//		if (Config.DEBUG)
//			Log.i("parseObj2JsonOnField", objstr);
//		return objstr;
//	}

	/**
	 * 将json对象转换成java对象
	 *
	 * @param <T>
	 * @param jsonData
	 * @param c
	 * @return
	 */
	public static <T> Object parseJsonObj(String jsonData, Class<T> c)
			throws Exception {

		if (null == jsonData)
			return null;




//		Gson gson = new Gson();

		Object obj = null;
		try {
//			obj = gson.fromJson(jsonData.trim(), c);
			obj = JSON.parseObject(jsonData, c);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(JsonUtil.class.toString(), "fromJson Exception...");
			Log.e(JsonUtil.class.toString(), "Exception:" + e.getMessage());
			throw new Exception();
		}
		if (Config.DEBUG)
			Log.i("parseJson2Obj", obj.toString());
		return obj;
	}
	/**
	 * 将数组对象转换成json
	 * @throws JSONException
	 */
	public static  String  toJsonString(Object obj)
			throws Exception {
		try {
			return JSON.toJSONString(obj,true);
		}catch (Exception e) {
			e.printStackTrace();
			Log.e(JsonUtil.class.toString(), "fromJson Exception...");
			Log.e(JsonUtil.class.toString(), "Exception:" + e.getMessage());
			throw new Exception();
		}
	}	
		
	
	
	/**
	 * 将json对象转换成数组对象
	 * 
	 * @param <T>
	 * @param jsonData
	 * @param c
	 * @return
	 * @throws JSONException
	 */
	public static <T> List<T> parseJsonList(String jsonData, Class<T> c)
			 {

		if (null == jsonData || "".equals(jsonData))
			return null;

		List<T> list = JSON.parseArray(jsonData, c);

		return list;
	}

	public static String combineJson(JSONObject requestHead,
			JSONObject requestBody) {

		JSONArray requestHeads = new JSONArray();
		requestHeads.put(requestHead);
		JSONArray requestBodys = new JSONArray();
		requestBodys.put(requestBody);

		StringBuffer mMessge = new StringBuffer();
		mMessge.append(requestHeads.toString() + "\t" + requestBodys.toString());
		return mMessge.toString();
	}

	/**
	 * 
	 * @param json
	 * @return
	 */
	public static String[] seporateJson(String json) {
		if (json != null && !json.equals("")) {
			String[] respStr = json.split("\t");
			if (respStr != null && respStr.length == 2) {
				return respStr;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
}