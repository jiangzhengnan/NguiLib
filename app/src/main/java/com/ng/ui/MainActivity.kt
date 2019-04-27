package com.ng.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.ng.ui.view.CentralTractionButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var ctt: CentralTractionButton
    private lateinit var btn1: Button
    private lateinit var btn2: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewLayoutID())
        initViewsAndEvents()
        LogUtils.d("aaa")
    }

    private fun getContentViewLayoutID(): Int {
        return R.layout.activity_main
    }


    private fun initViewsAndEvents() {
        ctt = findViewById(R.id.ctt_main)
        btn1 = findViewById(R.id.btn1_main)
        btn1.setOnClickListener {
            ctt_main.isChecked = true
            LogUtils.d("选中")
        }

        btn2 = findViewById(R.id.btn2_main)

        btn2.setOnClickListener {
            ctt_main.isChecked = false
            LogUtils.d("取消选中")
        }
    }

}
