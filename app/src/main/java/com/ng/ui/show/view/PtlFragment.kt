package com.ng.ui.show.view

import android.graphics.Color
import android.view.View
import com.ng.ui.R
import kotlinx.android.synthetic.main.fragment_ptl.*

/**
 * 描述:
 * @author Jzn
 * @date 2020-06-12
 */
class PtlFragment : BaseFragment() {

    override fun initViewsAndEvents(v: View) {
        ptl.setColor(Color.parseColor("#ffffff"),
                Color.parseColor("#99A6C7")
        )
    }

    override fun onStart() {
        super.onStart()
        ptl.post {
            ptl.startLoadingAnim()
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_ptl
}