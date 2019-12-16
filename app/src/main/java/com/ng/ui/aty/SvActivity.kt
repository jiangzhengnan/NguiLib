package com.ng.ui.aty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ng.ui.R
import kotlinx.android.synthetic.main.activity_sv.*

/**
 * 描述:
 * @author Jzn
 * @date 2019-12-14
 */
class SvActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewLayoutID())
        initViewsAndEvents()
    }

    private fun getContentViewLayoutID(): Int {
        return R.layout.activity_sv
    }

    private fun initViewsAndEvents() {

        button3.setOnClickListener {
            mysc.startAnim()
        }
        button4.setOnClickListener {
            mysc.stopAnim()
        }
    }


}