package com.ng.ngcommon.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ng.ngcommon.R;


public class ToastUtils {

    public static Toast toast;

    public ToastUtils() {
    }

    public static void showToast(Context context, String text) {
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.layout_toast, null, false);
        TextView message = (TextView) toastRoot.findViewById(R.id.toast_info);
        message.setText(text);
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = new Toast(context);
        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showToastTop(Context context, String text) {
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.layout_toast, null, false);
        TextView message = (TextView) toastRoot.findViewById(R.id.toast_info);
        message.setText(text);
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = new Toast(context);
        toast.setView(toastRoot);
        toast.setGravity(Gravity.TOP, 0, 150);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showToast(Context context, int resID) {
        showToast(context, context.getString(resID));
    }

}
