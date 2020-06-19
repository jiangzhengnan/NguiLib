package com.ng.ui.show.frag

import android.graphics.Color
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.ng.nguilib.ParrotViewNew
import com.ng.nguilib.utils.ParrotPillarNew
import com.ng.ui.R
import kotlinx.android.synthetic.main.fragment_pt.*
import java.util.*

/**
 * 描述:ParrotViewNew
 * @author Jzn
 * @date 2020-06-16
 */
class PtFragment : BaseFragment() {
    private val citys = arrayOf("California", "Texas", "Florida", "New York", "llinos", "Georgia", "Michigan", "New Jersey", "Pennsylvania", "Virginana", "Obhio", "U.S. Virgin Islands", "North Arolina", "South Carolina", "Maryland", "Colorado", "Minnersota", "Arizona", "Northern Marianas", "tokey")

    private val value1 = floatArrayOf(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f, 13f, 14f, 15f, 16f, 17f, 18f, 19f, 20f)

    private var mParrotPillars1: ArrayList<ParrotPillarNew>? = null
    private var mParrotPillars2: ArrayList<ParrotPillarNew>? = null

    private fun getFakeData() {
        mParrotPillars1 = ArrayList()
        mParrotPillars2 = ArrayList()

        for (i in citys.indices) {
            mParrotPillars1!!.add(ParrotPillarNew(citys[i], value1[i]))
            mParrotPillars2!!.add(ParrotPillarNew(citys[i], value1[i]))

        }

    }


    override fun initViewsAndEvents(v: View) {
        getFakeData()


        ptv2_new.setColor(Color.parseColor("#cc8400"), Color.parseColor("#ffff00"))

        ptv1_new.setData(mParrotPillars1,ParrotViewNew.ANIM_TYPE_NORMAL)
        ptv2_new.setData(mParrotPillars2,ParrotViewNew.ANIM_TYPE_COLECT)

        val btn : AppCompatButton = v.findViewById(R.id.btn_pt)
        btn.setOnClickListener {
            ptv1_new.startAnim()
            ptv2_new.startAnim()

        }
     }

    override fun getLayoutId(): Int  = R.layout.fragment_pt
}