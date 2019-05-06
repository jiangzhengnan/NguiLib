package com.ng.ui.aty

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ng.ui.LogUtils
import com.ng.ui.R

class WvActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewLayoutID())
        initViewsAndEvents()
    }

    private fun getContentViewLayoutID(): Int {
        return R.layout.activity_wv

    }


    private fun initViewsAndEvents() {

        LogUtils.d("aadd")
    }

}