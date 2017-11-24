package com.ng.ngcommon.util;

import android.util.Config;
import android.util.Log;


import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jiangzn on 16/10/8.
 */
public class JsonParseUtil {
    /**
     * 解析返回信息是否成功
     */
    public static boolean isSuccessResponse(String jsonStr)
            throws Exception {
        boolean isSuccess = false;
        JSONObject jsonObj = new JSONObject(jsonStr);
        boolean suc = jsonObj.getBoolean("suc");
        if (suc) {
            isSuccess = true;
        }
        return isSuccess;
    }
    public static boolean getBooleanValue(String jsonStr, String key)  {
        boolean result = false;
        try {
            JSONObject jsonObject  = new JSONObject(jsonStr);
            result = jsonObject.getBoolean(key);
        } catch (JSONException e) {
            e.printStackTrace();
            result = false;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
    public static int getIntValue(String jsonStr, String key) {
        int result = 0;
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            result= jsonObject.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
            result =  0;
        }catch (Exception e) {
            e.printStackTrace();
            result = 0;
        }
        return result;
    }
    /**
     * 解析服务器返回的message字段信息
     *
     * @throws Exception
     */
    public static String getStringValue(String jsonStr, String key)  {
        String value = null;
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            value = jsonObj.getString(key);
        } catch (JSONException e) {
            //e.printStackTrace();
            value = null;
        }catch (Exception e) {
            e.printStackTrace();
            value = null;
        }
        return value;
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
        String objstr = JSON.toJSONString(obj);
        if (Config.DEBUG)
            Log.i("parseObj2Json", objstr);
        return objstr;
    }

    /**
     * 将json对象转换成java对象
     *
     * @param <T>
     * @param jsonData
     * @param c
     * @return
     */
    public static <T> Object parseJsonObj(String jsonData, Class<T> c)
             {

        if (null == jsonData)
            return null;

        Object obj = null;
        try {
            obj = JSON.parseObject(jsonData, c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Config.DEBUG)
            Log.i("parseJson2Obj", obj.toString());
        return obj;
    }
}
