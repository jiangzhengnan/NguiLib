package com.ng.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import butterknife.ButterKnife
import com.ng.ui.aty.CtbActivity
import com.ng.ui.aty.EcgActivity
import com.ng.ui.aty.WvActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewLayoutID())
        ButterKnife.bind(this)
        initViewsAndEvents()
    }

    private fun getContentViewLayoutID(): Int {
        return R.layout.activity_main
    }

    private fun initViewsAndEvents() {
        //cbt
        addIntentBtn(CtbActivity().javaClass,"CentralTractionButton")
        //wv
        addIntentBtn(WvActivity().javaClass,"WaveView")
        //ecg
        addIntentBtn(EcgActivity().javaClass,"EcgView")

    }

    private fun addIntentBtn(target: Class<AppCompatActivity>,txt:String) {
        val button = Button(this)
        button.text = txt
        button.setOnClickListener { startActivity(intent.setClass(this, target)) }
        ll_main.addView(button)
    }

}
