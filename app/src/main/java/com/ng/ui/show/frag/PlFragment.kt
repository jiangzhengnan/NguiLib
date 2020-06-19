package com.ng.ui.show.frag

import android.view.View
import com.ng.nguilib.PolygonLoadView
import com.ng.ui.R
import kotlinx.android.synthetic.main.fragment_pl.*

/**
 * 描述:
 * @author Jzn
 * @date 2020-06-12
 */
class PlFragment : BaseFragment() {
    override fun getLayoutId(): Int =  R.layout.fragment_pl

    override fun initViewsAndEvents(v: View) {

    }



    override fun onStart() {
        super.onStart()
        pl1.post {
            pl1.setModel(pl1.SHOW_MODEL_ROUND)
        }
        pl2.post {
            pl2.setModel(pl1.SHOW_MODEL_TRIANGLE)
        }
        pl3.post {
            pl3.setModel(pl1.SHOW_MODEL_SQUARE)
        }
    }
}