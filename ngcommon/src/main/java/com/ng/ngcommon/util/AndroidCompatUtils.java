package com.ng.ngcommon.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by xjm on 2016/10/17.
 */

public class AndroidCompatUtils {
    /**
     * 检测需要的权限，如果没有权限会向用户拿取权限，在Activity需要自己处理返回结果，主要是为了兼容android 6.0
     * activity 当前显示的Activity
     * permission 需要的权限
     * code 用于Activity回调的时候一个特征码，一般在Activity自己设置
     */
    public static boolean checkSelfPermission(Activity activity, String permission, int code){
        boolean bPermission = true;
        if((ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,
                    new String[]{permission},
                    code);
            return false;
        }
        return bPermission;
    }
}
