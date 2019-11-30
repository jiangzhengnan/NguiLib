package com.ng.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.ng.nguilib.utils.LogUtils
import com.ng.ui.aty.*
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
        LogUtils.d("initViewsAndEvents")

        //cbt
        addIntentBtn(CtbActivity().javaClass,"CentralTractionButton")
        //wv
        addIntentBtn(WvActivity().javaClass,"WaveView")
        //ecg
        addIntentBtn(EcgActivity().javaClass,"EcgView")
        //pl
        addIntentBtn(PlActivity().javaClass,"PolygonLoadView")
        //ai
        addIntentBtn(AiActivity().javaClass,"ArrowInteractionView")
    }

    private fun addIntentBtn(target: Class<AppCompatActivity>, txt:String) {
        LogUtils.d("addIntentBtn")

        val button = Button(this)
        button.text = txt
        button.setOnClickListener {
            LogUtils.d("a:" + target.name)
            var intent = Intent()
            intent.setClass(this, target)
            startActivity(intent) }
        ll_main.addView(button)
    }

}
