package com.ng.ui.aty

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ng.ui.R
import kotlinx.android.synthetic.main.activity_ptl.*

/**
 * 描述:
 * @author Jzn
 * @date 2019-12-10
 */
class PtlActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewLayoutID())
        initViewsAndEvents()

        ptl.setColor(Color.parseColor("#ffffff"),
                Color.parseColor("#99A6C7")
        )
    }

    private fun getContentViewLayoutID(): Int {
        return R.layout.activity_ptl

    }

    private fun initViewsAndEvents() {
    }

    override fun onStart() {
        super.onStart()
        ptl.post {
            ptl.startLoadingAnim()
        }
    }


}