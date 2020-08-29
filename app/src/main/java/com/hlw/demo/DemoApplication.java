package com.hlw.demo;

import android.app.Application;
import android.content.Context;

import com.hlw.demo.util.LogUtils;

/**
 * Demo Application
 *
 * @author hlw
 */
public class DemoApplication extends Application {
    /**
     * application context
     */
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        LogUtils.initLog(BuildConfig.DEBUG);
    }

    /**
     * start application time
     */
    private long mStartApplicationTime;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // attachBaseContext 之后 getApplication 才会有值
//            PreferenceUtils.put(SPFiles.FILE_COMMON, SPKeys.COMMON_APP_START_TIME_LONG, System.currentTimeMillis());
        mStartApplicationTime = System.currentTimeMillis();
    }

    /**
     * get application context
     *
     * @return application context
     */
    public static Context getAppContext() {
        return mContext;
    }

    /**
     * get application start time
     *
     * @return application start time
     */
    public long getStartApplicationTime() {
        return System.currentTimeMillis() - mStartApplicationTime;
    }
}
