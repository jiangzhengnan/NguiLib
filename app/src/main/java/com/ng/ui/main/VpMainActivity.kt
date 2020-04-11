package com.ng.ui.main

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.ng.ui.R
import kotlinx.android.synthetic.main.activity_main_vp.*

/**
 * 描述: 新的主页
 * 需要加入drawable
 * @author Jzn
 * @date 2020-04-11
 */
class VpMainActivity : AppCompatActivity() {

    var itemInfos = ArrayList<ItemInfo>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewLayoutID())
        initViewsAndEvents()
    }

    private fun initViewsAndEvents() {

        itemInfos = ItemInfo.getDefaultList();
        var myViewPagerAdapter = MyViewPagerAdapter(this, itemInfos);

        vp_maina.adapter = myViewPagerAdapter;
        vp_maina.currentItem = 0;
        vp_maina.setOnPageChangeListener(MyOnPageChangeListener());
        //不能在xml文件中设置字体大小和颜色 但是可以在代码中设置
        pts_main.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
        pts_main.setTextColor(Color.RED);
    }

    private fun getContentViewLayoutID(): Int {
        return R.layout.activity_main_vp

    }


    class MyOnPageChangeListener : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
        }

    }
}