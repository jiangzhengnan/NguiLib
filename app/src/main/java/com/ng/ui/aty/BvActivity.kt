package com.ng.ui.aty

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ng.ui.R

/**
 * @ProjectName: NGUI
 * @Package: com.ng.ui.aty
 * @Description:
 * @Author: Pumpkin
 * @CreateDate: 2019/11/24
 */

class BvActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewLayoutID())
        initViewsAndEvents()
    }

    private fun getContentViewLayoutID(): Int {
        return R.layout.activity_bv

    }

    private fun initViewsAndEvents() {
    }


}