package com.ng.ui.show.layout

import android.view.View
import com.ng.nguilib.layout.FloatingCardLayout
import com.ng.ui.R
import com.ng.ui.show.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_floating_card.*

/**
 *    @author : jiangzhengnan.jzn@alibaba-inc.com
 *    @creation   : 2022/01/31
 *    @description   :
 *    漂浮卡片布局
 *    待完成
 */
class FloatingCardFragment : BaseFragment() {
    override fun initViewsAndEvents(v: View) {
        val dataList = arrayListOf<FloatingCardLayout.FloatingCardItem>()
        dataList.add(FloatingCardLayout.FloatingCardItem(R.drawable.bank_card_test_1))
        dataList.add(FloatingCardLayout.FloatingCardItem(R.drawable.bank_card_test_2))
        dataList.add(FloatingCardLayout.FloatingCardItem(R.drawable.bank_card_test_3))
        dataList.add(FloatingCardLayout.FloatingCardItem(R.drawable.bank_card_test_4))

        floatingCardLayout.setData(dataList)

        btn_up.setOnClickListener {
            floatingCardLayout.slideUp()
        }
        btn_down.setOnClickListener {
            floatingCardLayout.slideDown()
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_floating_card
}