package com.ng.ui.testchumo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.ng.ui.R
import kotlinx.android.synthetic.main.activity_test.*
import java.lang.Exception
import java.text.SimpleDateFormat


/**
 * @ProjectName: NguiLib
 * @Package: com.ng.ui.test
 * @Description:
 * @Author: Eden
 * @CreateDate: 2019/6/15 11:46
 */
class TestActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewLayoutID())
        ButterKnife.bind(this)
        initViewsAndEvents()


    }

    private fun getContentViewLayoutID(): Int {
        return  R.layout.activity_test
    }

    private fun initViewsAndEvents() {
        test.setOnClickListener {
            Toast.makeText(this, "click: " + getFormatTime(System.currentTimeMillis()), Toast.LENGTH_SHORT).show()
            try {
            } catch (e: Exception) {
                e.printStackTrace();
            }
        }
    }

    fun getFormatTime(time: Long): String {
        val dateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss") //使用了默认的格式创建了一个日期格式化对象。
        val time = dateFormat.format(time) //可以把日期转换转指定格式的字符串
        return time
    }


}
