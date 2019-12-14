package com.ng.ui.aty

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.ng.ui.R
import kotlinx.android.synthetic.main.activity_sv.*
import kotlinx.android.synthetic.main.activity_wv.*
import java.util.*

/**
 * 描述:
 * @author Jzn
 * @date 2019-12-14
 */
class SvActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewLayoutID())
        initViewsAndEvents()
    }

    private fun getContentViewLayoutID(): Int {
        return R.layout.activity_sv
    }

    private fun initViewsAndEvents() {
        button1.setOnTouchListener { v, event ->
            if(event.getAction() == MotionEvent.ACTION_DOWN) {

                if (hesv.playStatus) {
                    hesv.cancel()
                    button1.text = "START"
                } else {
                    button1.text = "stop"
                    hesv.start()
                    timeTask = object : TimerTask() {
                        override fun run() {
                            val msg = Message()
                            msg.what = 1
                            handler.sendMessage(msg)
                        }
                    }
                    timeTimer.schedule(timeTask, 100L, 100L)
                }
            }
            false
        }
    }

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val db = (Math.random() * 100).toInt()
            hesv.setVolume(db)
        }
    }
    private var timeTask: TimerTask? = null

    private val timeTimer = Timer(true)

    override fun onStart() {
        super.onStart()
    }

}