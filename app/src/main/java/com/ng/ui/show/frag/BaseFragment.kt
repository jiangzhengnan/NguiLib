package com.ng.ui.show.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ng.ui.R

/**
 * 描述:
 * @author Jzn
 * @date 2020-06-12
 */
abstract class BaseFragment :Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewsAndEvents(view)
    }

    abstract fun initViewsAndEvents(v: View)

    abstract fun getLayoutId(): Int
}