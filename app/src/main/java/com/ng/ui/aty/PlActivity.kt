package com.ng.ui.aty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ng.ui.R
import kotlinx.android.synthetic.main.activity_pl.*

class PlActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewLayoutID())
        initViewsAndEvents()
    }

    private fun getContentViewLayoutID(): Int {
        return R.layout.activity_pl

    }


    private fun initViewsAndEvents() {
    }

    override fun onStart() {
        super.onStart()
        pl1.post {
            pl1.setModel(pl1.SHOW_MODEL_ROUND)
        }
        pl2.post {
            pl2.setModel(pl1.SHOW_MODEL_TRIANGLE)
        }
        pl3.post {
            pl3.setModel(pl1.SHOW_MODEL_SQUARE)
        }
    }



}