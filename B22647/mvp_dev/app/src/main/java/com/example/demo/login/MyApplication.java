package com.example.demo.login;


import android.support.multidex.MultiDexApplication;
import android.content.Context;

import cn.bmob.v3.Bmob;


public class MyApplication extends MultiDexApplication {

    public static MyApplication application;
    private static Context context;

    /**
     * 获取上下文
     * @return
     */
    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        context = getApplicationContext();
        //第一：默认初始化
        Bmob.initialize(this, "3490ca681f3116a5cb23733b41435fe4");
    }
}
