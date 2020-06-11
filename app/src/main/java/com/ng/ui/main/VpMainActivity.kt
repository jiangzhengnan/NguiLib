package com.ng.ui.main

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main_vp.*
import android.view.Menu
import android.view.MenuItem
import com.ng.ui.R
import com.ng.ui.frag.ViewPagerFragment1


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
        itemInfos.add(ItemInfo("A", ViewPagerFragment1("a")))


        val myViewPagerAdapter = MyViewPagerAdapter(supportFragmentManager, itemInfos)

        vp_maina.adapter = myViewPagerAdapter;
        vp_maina.currentItem = 0;
        vp_maina.setOnPageChangeListener(MyOnPageChangeListener())

        //不能在xml文件中设置字体大小和颜色 但是可以在代码中设置
        pts_main.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        pts_main.setTextColor(Color.WHITE)
        pts_main.setTabIndicatorColorResource(R.color.colorAccent)
        pts_main.drawFullUnderline = false


        main_toolbar.title = "NgUiLib"
        setSupportActionBar(main_toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            //设置ActionBar左上角按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_nav)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

//    fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val menuInflater = menuInflater
//        menuInflater.inflate(R.menu.main_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.getItemId()) {
//            android.R.id.home -> if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                drawerLayout.closeDrawers()
//            } else {
//                drawerLayout.openDrawer(GravityCompat.START)
//            }
//            R.id.menu_1 -> Toast.makeText(this@MainActivity, item.getTitle(), Toast.LENGTH_SHORT).show()
//        }
//        return super.onOptionsItemSelected(item)
//    }

}