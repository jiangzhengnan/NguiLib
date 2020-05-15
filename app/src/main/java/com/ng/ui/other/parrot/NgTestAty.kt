package com.ng.ui.other.parrot

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ng.ui.R
import com.webull.webulltv.webulldata.parrot.ParrotPillar
import kotlinx.android.synthetic.main.activity_test_parrot.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * 描述:
 * @author Jzn
 * @date 2020-05-07
 */
class NgTestAty : AppCompatActivity() {


    private lateinit var mParrotPillars: ArrayList<ParrotPillar>

    private var citys = arrayOf("California", "Texas", "Florida", "New York", "llinos", "Georgia", "Michigan", "New Jersey", "Pennsylvania", "Virginana",
            "Obhio", "Washigton", "North arolina", "Massajdkasjvsd", "Maryland", "Colorado", "Minnersota", "Arizona", "dsadas", "nangua", "Biejing")

    fun init() {
        Log.d("nangua", "NgTestAty:init")
        mParrotPillars = ArrayList()
        var max = 20
        for (index in 0..max) {
            var random = Random()
            var value = ((index * 10 + random.nextInt(100)).toFloat())
            if (index == 0) {
                value = 400F
            }
            if (value <= 50) {
                value = 50F
            }
            val name = citys[index]
            val temp = ParrotPillar(name, value, 0f, 0)
            mParrotPillars.add(temp)
        }

        ptv_test.setData(mParrotPillars, ParrotView.ANIM_TYPE_COLECT)

        btn_test.setOnClickListener {
            ptv_test.startAnim()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_parrot)
        init()
    }

}