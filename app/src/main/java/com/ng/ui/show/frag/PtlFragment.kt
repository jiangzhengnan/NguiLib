package com.ng.ui.show.frag

import android.graphics.Color
import android.view.View
import com.ng.nguilib.PointLoadingView
import com.ng.ui.R

/**
 * 描述:
 * @author Jzn
 * @date 2020-06-12
 */
class PtlFragment : BaseFragment() {
    private lateinit var ptl: PointLoadingView

    override fun initViewsAndEvents(v: View) {
        ptl = v.findViewById(R.id.ptl)
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