package com.ng.ui.other.zoom

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ng.ui.R
import kotlinx.android.synthetic.main.activity_zoom.*

/**
 * 描述:
 * @author Jzn
 * @date 2020/9/9
 */
class ZoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoom)
        right.setOnClickListener {
            Toast.makeText(this, "right", Toast.LENGTH_SHORT).show()
        }
        right.setOnTouchListener { v, event ->
            Toast.makeText(this, "right", Toast.LENGTH_SHORT).show()
            true
        }
    }

}