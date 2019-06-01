package com.ng.ui.aty

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.ng.ui.LogUtils
import com.ng.ui.R
import com.ng.ui.view.CentralTractionButton


class CtbActivity : AppCompatActivity() {


    private lateinit var ctt: CentralTractionButton
    private lateinit var btn1: Button
    private lateinit var btn2: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewLayoutID())
        initViewsAndEvents()
    }

    private fun getContentViewLayoutID(): Int {
        return R.layout.activity_ctb
    }


    private fun initViewsAndEvents() {
        ctt = findViewById(R.id.ctt_mcv)
        btn1 = findViewById(R.id.btn1_mcv)
        btn1.setOnClickListener {
            ctt.isChecked = true
            LogUtils.d("选中")
        }

        btn2 = findViewById(R.id.btn2_mcv)

        btn2.setOnClickListener {
            ctt.isChecked = false
            LogUtils.d("取消选中")
        }
    }
}
