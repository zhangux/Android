package com.qdzl.util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.qdzl.App;
import com.qdzl.activity.LoginActivity;
import com.qdzl.activity.MainActivity;
import com.qdzl.bean.Result;
import com.qdzl.bean.SysUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2016/7/18.
 */
public class HttpUtils {
    private static OkHttpClient client = new OkHttpClient();

    public static Call get(String url, Callback callback) {
        return post(url, null, callback);
    }

    public static Call post(String url, Map<String, String> map, Callback callback) {
        //将map封装成RequestBody
        RequestBody body = getFormBody(map);
        //创建请求构建器
        Request.Builder builder = new Request.Builder()
                .url(url);
        //如果在body则添加post
        if (body != null) {
            builder.post(body);
        }
        //创建
        Call call = client.newCall(builder.build());
        call.enqueue(callback);
        return call;
    }

    private static FormBody getFormBody(Map<String, String> map) {
        if (map != null && !map.isEmpty()) {
            FormBody.Builder builder = new FormBody.Builder();
            Set<Map.Entry<String, String>> set = map.entrySet();
            for (Map.Entry<String, String> entry : set) {
                builder.add(entry.getKey(), entry.getValue());
            }
            return builder.build();
        }
        return null;
    }

    public static void httpLogin(final Activity activity, final String account, final String pwd) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("account", account);
        params.put("password", pwd);
        HttpUtils.post("http://192.168.3.3:8080/Maven_Project/user/login.shtml", params, new JsonCallbacks<Result<SysUser>>() {
            @Override
            public void OnSuccess(Call call, Result<SysUser> data) {
                if (data.state) {
                    ToastUtil.show(data.msg);
                    ((App) activity.getApplication()).user = data.data;
                    activity.sendBroadcast(new Intent("LOGIN_STATE"));
                    Log.e("bbbb","------httpLogin--------"+((App) activity.getApplication()).user.getNick());
                    SharedUtil.saveUserAccount(activity, account, pwd);
                    if (activity instanceof LoginActivity) {
                        ((LoginActivity) activity).btnLogin.setText("跳转中...");
                        MainActivity.startActivity(activity);
                        activity.finish();
                    }
                } else {
                    ToastUtil.show(data.msg);
                    if (activity instanceof MainActivity) {
                        LoginActivity.startActivity(activity);
                    }
                    if (activity instanceof LoginActivity) {
                        ((LoginActivity) activity).btnLogin.setText("登 录");
                    }
                }
            }

            @Override
            public void onFailure(Call call, int code) {
                super.onFailure(call, code);
                ((LoginActivity) activity).btnLogin.setText("登 录");
//                if (code == 404) {
//                    txtTime.setText("地址错误!");
//                } else if (code == 500) {
//                    txtTime.setText("服务器错误!");
//                } else {
//                    txtTime.setText("请检查网络连接!");
//                }
            }
        });
    }
}
