package com.ng.ui.other.twobtn

import android.content.Context
import android.util.AttributeSet
import android.widget.RadioGroup
import com.ng.ui.R

/**
 * 描述:
 * @author Jzn
 * @date 2020/9/23
 */
class TwoCheckButton : RadioGroup {

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        inflate(context,R.layout.view_two_check_button,this)

    }
}