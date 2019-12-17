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
                CylinderView.Entry(30f,  Color.parseColor("#78909c")),
                CylinderView.Entry(40f,  Color.parseColor("#607d8b")),
                CylinderView.Entry(50f,  Color.parseColor("#546e7a")),
                CylinderView.Entry(60f,  Color.parseColor("#455a64")),
                CylinderView.Entry(70f,  Color.parseColor("#37474f")),
                CylinderView.Entry(110f, Color.parseColor("#263238"))
        ))

        btn1.setOnClickListener {
            clv.startUpAllAnim()
        }
        btn2.setOnClickListener {
            clv.startSingleAnim()
        }
        btn3.setOnClickListener {
            clv.startChangeAnim()
        }
    }


    /**
     * 获取十六进制的颜色代码.例如  "#5A6677"
     * 分别取R、G、B的随机值，然后加起来即可
     *／
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