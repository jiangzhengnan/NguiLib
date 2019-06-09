package com.ng.ui.aty

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ng.nguilib.java.PolygonLoadViewJ
import com.ng.ui.R

class PlActivity : AppCompatActivity() {

    private lateinit var pl: PolygonLoadViewJ

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewLayoutID())
        initViewsAndEvents()
    }

    private fun getContentViewLayoutID(): Int {
        return R.layout.activity_pl

    }


    private fun initViewsAndEvents() {
        pl = findViewById(R.id.pl)
    }

    override fun onStart() {
        super.onStart()
        pl.post {
            pl.setModel(PolygonLoadViewJ.SHOW_MODEL_ROUND)
        }
    }


}