package com.hlw.demo;

import android.app.Application;

import com.hlw.demo.util.LogUtils;

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.initLog(BuildConfig.DEBUG);
    }
}
