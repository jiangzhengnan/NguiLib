package com.ng.ui.aty

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
            pl.setModel(pl.SHOW_MODEL_SQUARE)
        }
    }

    override fun onPause() {
        super.onPause()
            pl.stopAnimation()
    }

    override fun onResume() {
        super.onResume()
        pl.startAnimation()
    }


}