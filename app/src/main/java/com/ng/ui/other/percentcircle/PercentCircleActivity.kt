package com.ng.ui.other.percentcircle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ng.ui.R
import kotlinx.android.synthetic.main.activity_percent_circle.*

class PercentCircleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_percent_circle)
        percent.initValue(1)
        test.setOnClickListener {
            percent.setValue(50)
        }
    }

}