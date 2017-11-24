package com.ng.ngcommon.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import com.ng.ngcommon.util.LogUtils;
import com.ng.ngcommon.util.PreferencesUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OkHttp封装工具
 * 收到请求后会将cookie通过Des3加密保存到sp里,需要的时候取出解密发送到服务端防止cookie泄露
 * 默认开启5个线程执行任务
 */
public class HttpU {
    public static String MY_COOKIE = "";

    private static HttpU mInstance;
    private ExecutorService executorService = Executors.newFixedThreadPool(5); // 固定五个线程来执行任务
    private final Handler handler;
    private OkHttpClient mOkHttpClient;

    private HttpU() {
        mOkHttpClient = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
    }


    public static HttpU getInstance() {
        if (mInstance == null) {
            synchronized (HttpU.class) {
                if (mInstance == null) {
                    mInstance = new HttpU();
                }
            }
        }
        return mInstance;
    }


    /**
     * 网络请求方法
     *
     * @param context
     * @param url
     * @param params
     * @param tag
     * @param callback
     */
    public void post(final Context context, final String url, Map<String, Object> params, Object tag,
                     final HttpCallback callback) {
        String reqmsg = "";
        //token校验参数
        if (params == null) {
            params = new HashMap<String, Object>();
        }

        FormBody.Builder formBuilder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            Iterator iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                if (entry != null) {
                    if (entry.getKey() != null && entry.getValue() != null) {
                        formBuilder.add(entry.getKey().toString(), entry.getValue().toString());
                    }
                }
            }
        }
        FormBody formBody = formBuilder.build();
        String cookie = MY_COOKIE;
        LogUtils.d("cookie:" + cookie);

        if (cookie != null && !"".equals(cookie)) {
            try {
                cookie = Des3.decode(cookie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Request.Builder requestBuilder = new Request.Builder();
        if (cookie != null) {
            requestBuilder.header(HttpConstants.COOKIE, cookie);
        }
        final Request request = requestBuilder.url(url).post(formBody).tag(tag).build();

        if (HttpConstants.isLog) {
            Log.d("0.0", "请求报文Host：" + url);
            Log.d("0.0", "请求报文cookie：" + cookie);
            Log.d("0.0", "请求报文body：" + params);
        }

        callback.onBefore(request);

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(request, e, context);
                        callback.onAfter();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    final String result = response.body().string();
                    String cookie = response.headers().get("Set-Cookie");
                    if (HttpConstants.isLog) {
                        Log.d("0.0", "返回报文Host：" + url);
                        Log.d("0.0", "返回报文cookie：" + cookie);
                        Log.d("0.0", "返回报文body：" + result);
                    }
                    if (cookie != null && cookie.contains("lbs-lock-tkn")) {
                        try {
                            int start = cookie.indexOf("lbs-lock-tkn=");
                            int end = cookie.indexOf(";");

                            LogUtils.d("保存到本地的cookie:" + cookie.substring(start, end));
                            PreferencesUtil.getInstance(context).putString(context, HttpConstants.COOKIE, cookie.substring(start, end));

                            cookie = Des3.encode(cookie.substring(start, end));
                        } catch (Exception e) {
                            e.printStackTrace();
                            cookie = null;
                        }
                         MY_COOKIE = cookie;
                    }


                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            callback.onResponse(result);
                            callback.onAfter();
                        }
                    });

                } catch (final Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(request, e, context);
                            callback.onAfter();
                        }
                    });
                }
            }
        });
    }


    /**
     * 上传图片
     *
     * @param context
     * @param url
     * @param tag
     * @param callback 回调
     */
    public void uploadImages(final Context context, final String url, Map<String, Object> params,
                             String imgUrl, Object tag, final HttpCallback callback) {

        // mImgUrls为存放图片的url集合
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        //builder.addFormDataPart("method", "upload上传");
        File f = new File(imgUrl);
        if (f.exists()) {
            builder.addFormDataPart("file", f.getName(), RequestBody.create(MediaType.parse
                    ("image/" + f.getName().substring(f.getName().lastIndexOf(".") + 1)), f));
        } else {
            LogUtils.d("文件路径错误!");
            return;
        }
        if (params == null) {
            params = new HashMap<>();
        }
        if (params != null && params.size() > 0) {
            Iterator iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                if (entry != null) {
                    if (entry.getKey() != null && entry.getValue() != null) {
                        //formBuilder.add();
                        builder.addFormDataPart(entry.getKey().toString(), entry.getValue().toString());
                    }
                }
            }
        }
        String cookie = "";
        if (cookie != null && !"".equals(cookie)) {
            try {
                cookie = Des3.decode(cookie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        MultipartBody requestBody = builder.build();
        Request.Builder requestBuilder = new Request.Builder();
        if (cookie != null) {
            requestBuilder.header("Cookie", cookie);
        }
        //构建请求
        final Request request = requestBuilder.url(url)//地址
                .post(requestBody)//添加请求体
                .tag(tag)
                .build();
        callback.onBefore(request);
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(request, e, context);
                        callback.onAfter();
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    final String result = response.body().string();
                    String cookie = response.headers().get("Set-Cookie");
                    if (cookie != null && cookie.contains("ana=")) {
                        try {
                            int start = cookie.indexOf("ana=");
                            int end = cookie.indexOf(";");
                            cookie = Des3.encode(cookie.substring(start, end));
                        } catch (Exception e) {
                            e.printStackTrace();
                            cookie = null;
                        }
                        PreferencesUtil.getInstance(context).putString(context, HttpConstants.COOKIE, cookie);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResponse(result);
                            callback.onAfter();
                        }
                    });

                } catch (final Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(request, e, context);
                            callback.onAfter();
                        }
                    });
                }
            }
        });


    }

    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    //获取arftkn的值+'#'+当前时间毫秒数 后加密
    public static String getrftknAnnA(Context context) {
        String cookie = PreferencesUtil.getInstance(context).getString(context,"Cookie");
        if (cookie != null && cookie.length() > 0) {
            try {
                cookie = Des3.decode(cookie);
                cookie = cookie.substring(cookie.indexOf("=") + 1);
                cookie += "#" + System.currentTimeMillis();
                cookie = Des3.encode(cookie);
            } catch (Exception e) {
                Log.e("0.0", e + "");
            }
        }
        return cookie;
    }
}