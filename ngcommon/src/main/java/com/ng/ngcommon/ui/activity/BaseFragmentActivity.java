package com.ng.ngcommon.ui.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.dataeye.sdk.api.app.DCAgent;
import com.ng.ngcommon.bean.MessageEvent;
import com.ng.ngcommon.ui.BaseAppManager;
import com.ng.ngcommon.util.LogUtils;
import com.ng.ngcommon.widget.loading.VaryViewHelperController;
import com.ng.ngcommon.widget.net.NetChangeObserver;
import com.ng.ngcommon.widget.net.NetStateReceiver;
import com.ng.ngcommon.widget.net.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;

/**
 * Created by jiangzn on 16/11/23.
 */

public abstract class BaseFragmentActivity extends FragmentActivity {
    /**
     * 屏幕参数
     */
    protected int mScreenWidth = 0;
    protected int mScreenHeight = 0;
    protected float mScreenDensity = 0.0f;

    /**
     * 上下文
     */
    protected Context mContext = null;

    /**
     * 联网状态
     */
    protected NetChangeObserver mNetChangeObserver = null;

    /**
     * 加载视图控制器
     * loading view controller
     */
    protected VaryViewHelperController mVaryViewHelperController = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置沉浸式标题栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }

        BaseAppManager.getInstance().addActivity(this);

        init();
    }

    private void init() {
        BaseAppManager.getInstance().addActivity(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mScreenDensity = displayMetrics.density;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;

        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }

        mNetChangeObserver = new NetChangeObserver() {
            @Override
            public void onNetConnected(NetUtils.NetType type) {
                super.onNetConnected(type);
                if (isNetWorkConnected == false) {
                    onNetworkConnected(type);
                } else {
                    if (!NetUtils.isNetworkConnected(BaseFragmentActivity.this)) {
                        judgeNetwork();
                    }
                }
            }

            @Override
            public void onNetDisConnect() {
                super.onNetDisConnect();
                if (isNetWorkConnected == true) {
                    onNetworkDisConnected();
                } else {
                    if (NetUtils.isNetworkConnected(BaseFragmentActivity.this)) {
                        judgeNetwork();
                    }
                }
            }
        };

        NetStateReceiver.registerObserver(mNetChangeObserver);

        //判断网络情况
        judgeNetwork();
    }

    private void judgeNetwork() {
        if (NetUtils.isNetworkConnected(this)) {
            isNetWorkConnected = true;
            //LogUtils.d("now_isNetWorkConnected:" + isNetWorkConnected);
            initViewsAndEvents();
        } else {
            isNetWorkConnected = false;
            //LogUtils.d("now_isNetWorkConnected:" + isNetWorkConnected);
            onNetworkDisConnected();
        }
    }

    /**
     * network connected
     */
    protected void onNetworkConnected(NetUtils.NetType type) {
        LogUtils.d("onNetworkConnected");
        if (getRootView() != null) {
            toggleNetworkError(false, null);
            toggleShowLoading(false, null);
            initViewsAndEvents();
        }
    }

    private static boolean isNetWorkConnected = false;


    /**
     * network disconnected
     */
    protected void onNetworkDisConnected() {
        LogUtils.d("onNetworkDisConnected");
        if (getRootView() != null) {
            getRootView().setVisibility(View.VISIBLE);
            toggleNetworkError(true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    judgeNetwork();
                }
            });
        }
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        if (null != getLoadingTargetView()) {
            mVaryViewHelperController = new VaryViewHelperController(getLoadingTargetView());
        } else if (null != getRootView()) {
            mVaryViewHelperController = new VaryViewHelperController(getRootView());
        }
    }

    /**
     * 得到视图高度
     *
     * @param v
     * @return
     */
    protected int getViewHeight(View v) {
        int intw = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int inth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(intw, inth);
        return v.getMeasuredHeight();
    }


    /**
     * bind layout resource file
     *
     * @return id of layout resource
     */
    protected abstract int getContentViewLayoutID();


    /**
     * 用于Butterknife 的onclick方法绑定
     *
     * @param view
     */
    protected abstract void onClick(View view);

    /**
     * init all views and add events
     */
    protected abstract void initViewsAndEvents();

    protected abstract View getLoadingTargetView();



    /**
     * toggle show loading
     *
     * @param toggle
     */
    protected void toggleShowLoading(boolean toggle, String msg) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showLoading(msg);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show empty
     *
     * @param toggle
     */
    protected void toggleShowEmpty(boolean toggle, String msg, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showEmpty(msg, onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show error
     *
     * @param toggle
     */
    protected void toggleShowError(boolean toggle, String msg, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showError(msg, onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show network error
     *
     * @param toggle
     */
    protected void toggleNetworkError(boolean toggle, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showNetworkError(onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    protected View getRootView() {
        View group = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        //LogUtil.d(this,"----"+group.getClass().getSimpleName());
        return group;
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard() {
        if (getWindow().getAttributes(). softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN ) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE );
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS );
        }
    }
    public void onResume() {
        super.onResume();
        DCAgent.resume(this);
    }
    public void onPause() {
        super.onPause();
        DCAgent.pause(this);

    }

    @Override
    protected void onStart() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onStart();
     }

    @Subscribe
    public void onMessageEvent(MessageEvent event) {
        LogUtils.d("BaseFragmentAty收到了");
        onGetEvent(event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        NetStateReceiver.removeRegisterObserver(mNetChangeObserver);
        EventBus.getDefault().unregister(this);
    }

    protected abstract void onGetEvent(MessageEvent event);



}

