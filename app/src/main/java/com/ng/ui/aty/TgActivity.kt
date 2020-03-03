package com.ng.ui.aty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ng.ui.R

/**
 * 描述:
 * @author Jzn
 * @date 2020-02-28
 */
class TgActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewLayoutID())
        initViewsAndEvents()
    }

    private fun getContentViewLayoutID(): Int {
        return R.layout.activity_tg

    }

    private fun initViewsAndEvents() {
    }


}