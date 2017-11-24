package com.ng.ngcommon.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import butterknife.ButterKnife;


/**
 * Created by jiangzn on 16/10/23.
 */
public abstract class BaseFragment extends Fragment {
    /**
     * 屏幕参数
     */
    protected int mScreenWith = 0;
    protected int mScreenHeight = 0;
    protected float mScreenDensity = 0.0f;

    //在基类中初始化Dialog
    public Dialog mLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(getContentViewLayoutID(),container,false);
        ButterKnife.bind(this, v);
        init(v);

        return v;
    }

    private void init(View view) {
        WindowManager wm = getActivity().getWindowManager();
        mScreenHeight = wm.getDefaultDisplay().getHeight();    //屏幕高度

        initViewsAndEvents(view);
    }

    protected abstract void initViewsAndEvents(View view);

    /**
     * 用于Butterknife 的onclick方法绑定
     *
     * @param view
     */
    protected abstract void onClick(View view);


    protected abstract int getContentViewLayoutID();


    public void onResume() {
        super.onResume();
    }
    public void onPause() {
        super.onPause();
    }


}
