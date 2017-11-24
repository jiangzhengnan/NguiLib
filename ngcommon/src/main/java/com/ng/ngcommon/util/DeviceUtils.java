package com.ng.ngcommon.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.util.UUID;

/**
 * Created by jiangzn on 16/10/6.
 */
public class DeviceUtils {
    public static String getUUID(Context context) {
        DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
        return deviceUuidFactory.getDeviceUuid().toString();
    }


    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        int versioncode;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }


//    //获得独一无二的Psuedo ID
//    public static String getUniquePsuedoID() {
//        String serial = null;
//
//        String m_szDevIDShort = "35" +
//                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
//
//                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
//
//                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
//
//                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
//
//                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
//
//                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
//
//                Build.USER.length() % 10; //13 位
//
//        try {
//            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
//            //API>=9 使用serial号
//            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
//        } catch (Exception exception) {
//            //serial需要一个初始化
//            serial = "serial"; // 随便一个初始化
//        }
//        //使用硬件信息拼凑出来的15位号码
//        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
//    }

}
