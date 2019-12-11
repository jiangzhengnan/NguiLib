package com.ng.ui.aty

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ng.nguilib.CylinderView
import com.ng.ui.R
import kotlinx.android.synthetic.main.activity_cd.*
import java.util.*


/**
 * @ProjectName: NGUI
 * @Package: com.ng.ui.aty
 * @Description:
 * @Author: Pumpkin
 * @CreateDate: 2019/11/24
 */

class CdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewLayoutID())
        initViewsAndEvents()
    }

    private fun getContentViewLayoutID(): Int {
        return R.layout.activity_cd

    }

    private fun initViewsAndEvents() {
        clv.setData(arrayListOf(
                CylinderView.Entry(20f, Color.RED),
                CylinderView.Entry(30f, Color.YELLOW),
                CylinderView.Entry(40f, Color.BLUE),
                CylinderView.Entry(50f, Color.GREEN),
                CylinderView.Entry(60f, Color.GRAY),
                CylinderView.Entry(160f, Color.MAGENTA)
                ))
    }


    /**
     * 获取十六进制的颜色代码.例如  "#5A6677"
     * 分别取R、G、B的随机值，然后加起来即可
     *
     * @return String
     */
    fun getRandColor(): String {
        var R: String
        var G: String
        var B: String
        val random = Random()
        R = Integer.toHexString(random.nextInt(256)).toUpperCase()
        G = Integer.toHexString(random.nextInt(256)).toUpperCase()
        B = Integer.toHexString(random.nextInt(256)).toUpperCase()

        R = if (R.length == 1) "0$R" else R
        G = if (G.length == 1) "0$G" else G
        B = if (B.length == 1) "0$B" else B

        return "#$R$G$B"
    }

}