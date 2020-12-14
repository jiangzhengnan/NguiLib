package com.ng.ui.show.view

import android.view.View
import com.ng.ui.R
import kotlinx.android.synthetic.main.fragment_bd.*

/**
 * 描述:
 * @author Jzn
 * @date 2020-06-16
 */
class BdFragment : BaseFragment() {


    override fun initViewsAndEvents(v: View) {
        test_bar_a.setLastPriceInit(200f, 0)

    }

    override fun getLayoutId(): Int  = R.layout.fragment_bd
}