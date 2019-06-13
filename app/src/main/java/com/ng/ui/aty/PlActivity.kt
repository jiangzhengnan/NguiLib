package com.ng.ui.aty

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ng.nguilib.PolygonLoadView
import com.ng.nguilib.java.PolygonLoadViewJ
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
        pl.post {
            pl.setModel(pl.SHOW_MODEL_ROUND)
        }
    }


}