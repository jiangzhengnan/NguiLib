package com.ng.ui.test

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.ng.ui.R

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
        val imageView = findViewById<View>(R.id.img_test) as ImageView
    }


}
