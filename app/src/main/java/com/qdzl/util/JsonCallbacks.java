package com.qdzl.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/7/18.
 */
public abstract class JsonCallbacks<T> implements Callback {
    private Type type;
    private final int SUCCESS = 0;
    private final int FAILURE = 1;
    //在主线程运行
    public Handler handler = new Handler(Looper.getMainLooper());

    public JsonCallbacks() {
        //通过反射获取的泛型类型
        Type tmp = getClass().getGenericSuperclass();
        if (!(tmp instanceof ParameterizedType)) {
            throw new ExceptionInInitializerError("can't get type");
        }
        type = ((ParameterizedType) tmp).getActualTypeArguments()[0];
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.e("aaaa","x:==>network connect error", e);
        handler.post(new MsgRunnable(FAILURE, call, e.hashCode()));
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        int code = response.code();
        try {
            Log.e("aaaa","y:==>response code:" + code);
            if (code == 200) {
                String json = response.body().string().toString();
                Log.e("aaaa","y:==>json:" + json);
                T t = JSON.parseObject(json, type);
                handler.post(new MsgRunnable(SUCCESS, call, t));
            } else {
                handler.post(new MsgRunnable(FAILURE, call, code));
            }
        } catch (IOException e) {
             Log.e("aaaa",e.getMessage(), e);
            handler.post(new MsgRunnable(FAILURE, call, code));
        }
    }

    public void onFailure(Call call, int code) {
        if (code == 404) {
            ToastUtil.show("地址错误！");
        } else if (code == 500) {
            ToastUtil.show("服务器错误！");
        }else {
            ToastUtil.show("请检查网络连接！");
        }
    }

    public abstract void OnSuccess(Call call, T data);

    class MsgRunnable implements Runnable {
        private int what;
        private Call call;
        private T data;
        private int code;

        public MsgRunnable(int what, Call call, T data) {
            this.what = what;
            this.call = call;
            this.data = data;
        }

        public MsgRunnable(int what, Call call, int code) {
            this.what = what;
            this.call = call;
            this.code = code;
        }

        @Override
        public void run() {
            switch (what) {
                case SUCCESS:
                    OnSuccess(call, data);
                    break;
                case FAILURE:
                    onFailure(call, code);
            }
        }
    }
}
