package com.hlw.demo;

import android.app.Application;
import android.content.Context;

import com.hlw.demo.util.LogUtils;

public class DemoApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        LogUtils.initLog(BuildConfig.DEBUG);
    }

    private long startApplictionTime;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // attachBaseContext 之后 getApplication 才会有值
//            PreferenceUtils.put(SPFiles.FILE_COMMON, SPKeys.COMMON_APP_START_TIME_LONG, System.currentTimeMillis());
        startApplictionTime = System.currentTimeMillis();
    }

    public static Context getAppContext() {
        return mContext;
    }

    public long getStartApplictionTime() {
        return System.currentTimeMillis() - startApplictionTime;
    }
}
