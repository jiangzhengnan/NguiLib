package com.ng.ui.aty

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.ng.nguilib.utils.LogUtils
import com.ng.ui.R
import kotlinx.android.synthetic.main.activity_sv.*

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

        button3.setOnClickListener {
            mysc.startAnim()
        }
        button4.setOnClickListener {
            mysc.stopAnim()
        }

        yinliang.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mysc.setVolume(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        });
    }

    override fun onStart() {
        super.onStart()
        mysc.post {
            mysc.startAnim()
        }
    }


}