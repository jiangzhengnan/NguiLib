package com.ng.ui

import android.view.View
import android.widget.Button
import com.ng.ngcommon.bean.MessageEvent
import com.ng.ngcommon.ui.activity.BaseActivity
import com.ng.ngcommon.util.LogUtils
import com.ng.ui.view.CentralTractionButton

class MainActivity : BaseActivity() {
    override fun onClick(view: View?) {
    }

    private lateinit var ctt_main: CentralTractionButton
    private lateinit var btn1_main: Button
    private lateinit var btn2_main: Button

    override fun getContentViewLayoutID(): Int {
        return R.layout.activity_main
    }


    override fun initViewsAndEvents() {
        ctt_main = findViewById(R.id.ctt_main) as CentralTractionButton
        btn1_main = findViewById(R.id.btn1_main) as Button
        btn1_main.setOnClickListener({
            ctt_main.isChecked = true
            LogUtils.d("选中")
        })

        btn2_main = findViewById(R.id.btn2_main) as Button
        btn2_main.setOnClickListener({
            ctt_main.isChecked = false
            LogUtils.d("取消选中")
        })
    }

    override fun getLoadingTargetView(): View? {
        return null
    }

    override fun onGetEvent(event: MessageEvent) {

    }
}
