package com.ng.ui.show.view

import android.view.View
import com.ng.ui.R
import kotlinx.android.synthetic.main.fragment_ai.*

/**
 * 描述:
 * @author Jzn
 * @date 2020-06-12
 */
class AiFragment : BaseFragment() {

    override fun initViewsAndEvents(v: View) {
        aiview_left.setModel(aiview_left.SHOW_MODEL_LEFT)
        aiview_left.setOnClickListener {
            aiview_left.startAnim()
        }

        aiview_right.setModel(aiview_right.SHOW_MODEL_RIGHT)
        aiview_right.setOnClickListener {
            aiview_right.startAnim()
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_ai
}