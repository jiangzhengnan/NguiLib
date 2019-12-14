package com.ng.ui.testchumo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ng.ui.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2019-12-13
 */
public class testjava extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(testjava.this, isChristmasTime() + " ", Toast.LENGTH_SHORT).show();
                isChristmasTime();
            }
        });
    }

    /**
     * 判断是否是圣诞节
     * 美东时间2019年12.23 00:00:01-12.29 23:59:59间展示圣诞节主题icon，其他时段不再展示。
     * 切换地区：美国、加拿大、英国、印度、德国；其他地区保持不变
     * <p>
     * 美东时间与北京时间的换算：
     * 美东时间=北京时间-12小时（夏令时）
     * 美东时间=北京时间-13小时（冬令时）纽约时间，晚13个小时
     *
     * @return boolean
     */
    public static boolean isChristmasTime() {
        try {
            //判断是否是圣诞节
            String startTimeStr = "2019-12-23 00:00:01";
            String endTimeStr = "2019-12-29 23:59:59";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            TimeZone usTime = TimeZone.getTimeZone("America/New_York");
            simpleDateFormat.setTimeZone(usTime);

            Date startTimeDate = simpleDateFormat.parse(startTimeStr);
            Date endTimeDate = simpleDateFormat.parse(endTimeStr);


            Date nowTimeDate = Calendar.getInstance(usTime,Locale.US).getTime();

            long nowTime =nowTimeDate.getTime();

            if (startTimeDate.getTime() <= nowTime && nowTime <= endTimeDate.getTime()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


}
