package com.ng.ui.aty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ng.ui.R
import kotlinx.android.synthetic.main.activity_wv.*

class WvActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewLayoutID())
        initViewsAndEvents()
    }

    private fun getContentViewLayoutID(): Int {
        return R.layout.activity_wv

    }

    private fun initViewsAndEvents() {
    }

    override fun onStart() {
        super.onStart()
        waveview.post {
            waveview.startAnima()
        }
    }

}