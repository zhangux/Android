package com.qdzl;

import android.app.Application;

import com.qdzl.bean.SysUser;
import com.qdzl.util.ToastUtil;

/**
 * Created by QDZL on 2017/11/29.
 */
public class App extends Application {
    public SysUser user;
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.init(this);
    }
}