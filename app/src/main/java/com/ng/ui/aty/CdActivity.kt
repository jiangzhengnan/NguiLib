package com.ng.ui.aty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ng.ui.R
import kotlinx.android.synthetic.main.activity_cd.*

/**
 * @ProjectName: NGUI
 * @Package: com.ng.ui.aty
 * @Description:
 * @Author: Pumpkin
 * @CreateDate: 2019/11/24
 */

class CdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewLayoutID())
        initViewsAndEvents()
    }

    private fun getContentViewLayoutID(): Int {
        return R.layout.activity_cd

    }

    private fun initViewsAndEvents() {

        clv.setData(arrayListOf(20f, 40f, 30f, 70f, 60f, 100f, 90f))
    }


}