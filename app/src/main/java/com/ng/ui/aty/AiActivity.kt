package com.ng.ui.aty

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ng.ui.R
import kotlinx.android.synthetic.main.activity_ai.*

class AiActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewLayoutID())
        initViewsAndEvents()

        aiview.setOnClickListener {


        }
    }

    private fun getContentViewLayoutID(): Int {
        return R.layout.activity_ai

    }


    private fun initViewsAndEvents() {
    }

    override fun onStart() {
        super.onStart()
        aiview.post {
            aiview.setModel(aiview.SHOW_MODEL_LEFT)
        }
    }
}