package com.ng.ui.aty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ng.ui.R
import kotlinx.android.synthetic.main.activity_ai.*

class AiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewLayoutID())
        initViewsAndEvents()

        aiview_left.setModel(aiview_left.SHOW_MODEL_LEFT)
        aiview_left.setOnClickListener {
            aiview_left.startAnim()
        }

        aiview_right.setModel(aiview_right.SHOW_MODEL_RIGHT)
        aiview_right.setOnClickListener {
            aiview_right.startAnim()
        }

    }

    private fun getContentViewLayoutID(): Int {
        return R.layout.activity_ai
    }

    private fun initViewsAndEvents() {
    }

}