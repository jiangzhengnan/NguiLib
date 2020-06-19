package com.ng.ui.show.frag

import android.view.View
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSeekBar
import com.ng.ui.R
import com.ng.ui.other.soundwaveview.SoundView
import kotlinx.android.synthetic.main.fragment_sv.*

/**
 * 描述:
 * @author Jzn
 * @date 2020-06-12
 */
class SvFragment : BaseFragment() {

    override fun initViewsAndEvents(v: View) {
        button3.setOnClickListener {
            mysc.startWaveAnim()
        }
        button4.setOnClickListener {
            mysc.stopWaveAnim()
        }

        yinliang.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mysc.setVolume(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }


    override fun onStart() {
        super.onStart()
        mysc.post {
            //   mysc.startBallAnim()
            mysc.setVolume(80)
            mysc.startWaveAnim()

        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_sv
}