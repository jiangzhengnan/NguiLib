package com.ng.ui.aty
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ng.ui.R

class PlActivity: AppCompatActivity() {


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


}