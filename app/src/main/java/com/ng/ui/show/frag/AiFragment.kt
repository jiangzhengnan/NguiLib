package com.ng.ui.show.frag

import android.view.View
import com.ng.nguilib.ArrowInteractionView
import com.ng.ui.R

/**
 * 描述:
 * @author Jzn
 * @date 2020-06-12
 */
class AiFragment : BaseFragment() {
    private lateinit var aiviewLeft: ArrowInteractionView
    private lateinit var aiviewRight: ArrowInteractionView

    override fun initViewsAndEvents(v: View) {
        aiviewLeft = v.findViewById(R.id.aiview_left)
        aiviewLeft.setModel(aiviewLeft.SHOW_MODEL_LEFT)
        aiviewLeft.setOnClickListener {
            aiviewLeft.startAnim()
        }

        aiviewRight = v.findViewById(R.id.aiview_right)
        aiviewRight.setModel(aiviewRight.SHOW_MODEL_RIGHT)
        aiviewRight.setOnClickListener {
            aiviewRight.startAnim()
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_ai
}