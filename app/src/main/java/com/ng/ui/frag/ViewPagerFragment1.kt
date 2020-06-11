package com.ng.ui.frag

import android.widget.TextView
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment






/**
 * 描述:
 * @author Jzn
 * @date 2020-06-11
 */
class ViewPagerFragment1(inpurt: String) : Fragment() {
    private var tv: TextView? = null
    private var text =""

    init {
        text = inpurt
    }




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.ng.ui.R.layout.view_pager_fragment_demo1, container, false)
        tv = v.findViewById(com.ng.ui.R.id.viewPagerText)
        tv!!.text = text
        return v
    }

}