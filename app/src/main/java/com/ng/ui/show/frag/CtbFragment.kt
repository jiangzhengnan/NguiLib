package com.ng.ui.show.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.ng.ui.R
import kotlinx.android.synthetic.main.fragment_ctb.*

/**
 * 描述:
 * @author Jzn
 * @date 2020-06-12
 */
class CtbFragment : BaseFragment() {
    private lateinit var btn1Mcv: Button
    private lateinit var btn2Mcv: Button
    override fun initViewsAndEvents(v: View) {
        btn1Mcv = v.findViewById(R.id.btn1_mcv)
        btn1Mcv.setOnClickListener {
            ctt_mcv.isChecked = true
        }
        btn2Mcv = v.findViewById(R.id.btn2_mcv)
        btn2Mcv.setOnClickListener {
            ctt_mcv.isChecked = false
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_ctb


}