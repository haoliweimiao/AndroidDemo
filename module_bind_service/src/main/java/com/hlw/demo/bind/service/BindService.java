package com.hlw.demo.bind.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

public class BindService extends Service {

    private AtomicInteger mConnectCount = new AtomicInteger(0);

    private static IAidlBind.Stub binder = new IAidlBind.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
            Log.i("!!!!!!", "Service execute basicTypes");
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // 首次绑定Service onBind方法一次
        Log.i("!!!!!!", String.format("onBind bind service count:%s", mConnectCount.incrementAndGet()));
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // 所有服务解绑后调用 onUnbind
        Log.i("!!!!!!", String.format("onUnbind bind service count:%s", mConnectCount.decrementAndGet()));
        return super.onUnbind(intent);
    }
}
