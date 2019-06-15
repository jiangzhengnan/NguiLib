package com.ng.ui.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import com.ng.nguilib.utils.LogUtils
import com.ng.ui.R
import kotlinx.android.synthetic.main.activity_test.*

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
        return R.layout.activity_test
    }

    private fun initViewsAndEvents() {
        fb_test.setOnClickListener {
            LogUtils.d("点击")
        }
    }


}
