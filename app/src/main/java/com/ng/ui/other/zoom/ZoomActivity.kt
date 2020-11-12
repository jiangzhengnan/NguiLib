package com.ng.ui.other.zoom

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.ng.nguilib.layout.ZoomLayout3
import com.ng.ui.R
import kotlinx.android.synthetic.main.activity_zoom.*

/**
 * 描述:
 * @author Jzn
 * @date 2020/9/9
 */
class ZoomActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoom)

        root_layout.addListener(object:ZoomLayout3.OnZoomListener{
            override fun setState(isZoomming: Boolean) {
            }

        })
    }

}