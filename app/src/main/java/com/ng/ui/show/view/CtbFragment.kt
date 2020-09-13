package com.ng.ui.show.view

import android.view.View
import com.ng.ui.R
import kotlinx.android.synthetic.main.fragment_ctb.*

/**
 * 描述:
 * @author Jzn
 * @date 2020-06-12
 */
class CtbFragment : BaseFragment() {
    override fun initViewsAndEvents(v: View) {
        btn1_mcv.setOnClickListener {
            ctt_mcv.isChecked = true
        }
        btn2_mcv.setOnClickListener {
            ctt_mcv.isChecked = false
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_ctb


}