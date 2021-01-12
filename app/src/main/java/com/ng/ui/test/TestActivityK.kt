package com.ng.ui.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ng.ui.R
import com.ng.ui.other.dragdialog.DragDialogLayout
import kotlinx.android.synthetic.main.activity_test.*

/**
 * 描述:
 * @author Jzn
 * @date 2020/12/2
 */
class TestActivityK : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        btn_add.setOnClickListener {
            DragDialogLayout(this).show()
        }
    }
}