package com.ng.ui.study.rotate

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.ng.ui.R
import kotlinx.android.synthetic.main.activity_test_rotate_xyz.*
import kotlinx.android.synthetic.main.activity_test_rotate_xyz.view.*

/**
 * 描述:
 * https://www.jianshu.com/p/34e0fe5f9e31
 * https://hencoder.com/ui-1-4/
 * @author Jzn
 * @date 2020-06-04
 */
class RotateXyzTestActivity : AppCompatActivity() {


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_rotate_xyz)

        updateTv()

        sb_x.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                rts_test.setRX(progress)
                updateTv()
            }
        })
        sb_y.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                rts_test.setRY(progress)
                updateTv()

            }
        })

        sb_z.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                rts_test.setRZ(progress)
                updateTv()

            }
        })

        sb_r.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                rts_test.setRR(progress)
                updateTv()

                //rts_test.setRQ(progress)

            }
        })
        sb_q.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                rts_test.setRQ(progress)
                updateTv()

            }
        })

        btn_test.setOnClickListener {
            sb_x.progress = 0
            sb_y.progress = 0
            sb_z.progress = 0
            sb_r.progress = 0
            sb_q.progress = 0

        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateTv() {
        tv_show.text = "camera三轴参数\n" +
                "x:" + rts_test.getmX() + "\n" +
                "y:" + rts_test.getmY() + "\n" +
                "z:" + rts_test.getmZ() + "\n" +
                "\n旋转角度\n" +
                "r:" + rts_test.getmR() + "\n" +
                "\n裁切角度\n" +
                "q:" + rts_test.getmQ() + "\n"

    }

}