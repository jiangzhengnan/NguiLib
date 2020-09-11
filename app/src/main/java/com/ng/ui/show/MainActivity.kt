package com.ng.ui.show

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.ng.nguilib.utils.DensityUtil
import com.ng.ui.R
import com.ng.ui.show.frag.*
import com.ng.ui.show.main.AppUtils
import com.ng.ui.show.main.ItemInfo
import com.ng.ui.show.main.LeftListAdapter
import com.ng.ui.show.main.MyViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main_vp.*


/**
 * todo add layout布局主页
 * 描述: 新的主页
 * @author Jzn
 * @date 2020-04-11
 */
class MainActivity : AppCompatActivity() {

    private fun getContentViewLayoutID(): Int = R.layout.activity_main_vp

    private var isShowView = true

    private var mViewList = ArrayList<ItemInfo>()
    private var myViewPagerAdapter :MyViewPagerAdapter?=null
    private lateinit var mAdapter: LeftListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DensityUtil.setCustomDensity(this, application)
        setContentView(getContentViewLayoutID())
        initViewsAndEvents()
    }

    private fun initViewsAndEvents() {
        initData()

        initView()



        //notify
        myViewPagerAdapter!!.notifyDataSetChanged()
        mAdapter.notifyDataSetChanged()
        vp_maina.currentItem = 0
    }

    private fun initData() {
        //start
        mViewList.add(ItemInfo(getString(R.string.view_1), CtbFragment()))
        mViewList.add(ItemInfo(getString(R.string.view_2), EcgFragment()))
        mViewList.add(ItemInfo(getString(R.string.view_3), PlFragment()))
        mViewList.add(ItemInfo(getString(R.string.view_4), AiFragment()))
        mViewList.add(ItemInfo(getString(R.string.view_5), PtlFragment()))
        mViewList.add(ItemInfo(getString(R.string.view_6), CdFragment()))
        mViewList.add(ItemInfo(getString(R.string.view_7), SvFragment()))
        mViewList.add(ItemInfo(getString(R.string.view_8), TgFragment()))
        mViewList.add(ItemInfo(getString(R.string.view_9), PtFragment()))
    }

    private fun initView() {
        myViewPagerAdapter = MyViewPagerAdapter(supportFragmentManager, mViewList)
        vp_maina.adapter = myViewPagerAdapter;
        vp_maina.setOnPageChangeListener(MyOnPageChangeListener())
        vp_maina.offscreenPageLimit = mViewList.size

        pts_main.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        pts_main.setTextColor(Color.WHITE)
        pts_main.setTabIndicatorColorResource(R.color.colorAccent)
        pts_main.drawFullUnderline = false
        main_toolbar.title = AppUtils.getAppName(this)
        setSupportActionBar(main_toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            //设置ActionBar左上角按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_nav)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        mAdapter = LeftListAdapter(this, mViewList)
        mAdapter.setItemListener(object : LeftListAdapter.OnLeftItemClick {
            override fun onItem(pos: Int) {
                vp_maina.currentItem = pos
                drawer_main.closeDrawers()
            }

        })
        left_rv.layoutManager = LinearLayoutManager(this)
        left_rv.adapter = mAdapter
    }


    class MyOnPageChangeListener : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    //        menu.findItem(R.id.option_menu_previous).setEnabled(true);
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> if (drawer_main.isDrawerOpen(GravityCompat.START)) {
                drawer_main.closeDrawers()
            } else {
                drawer_main.openDrawer(GravityCompat.START)
            }
            R.id.option_menu_change -> {
                isShowView = !isShowView
                if (isShowView) {
                    item.title = getString(R.string.view)
                } else {
                    item.title = getString(R.string.layout)
                }
            }

            //R.id.menu_1 -> Toast.makeText(this@MainActivity, item.getTitle(), Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }


}