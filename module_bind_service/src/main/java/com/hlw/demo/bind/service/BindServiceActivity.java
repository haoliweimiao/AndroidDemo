package com.hlw.demo.bind.service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class BindServiceActivity extends AppCompatActivity {

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent();
        intent.setClassName("com.hlw.demo.bind.service", "com.hlw.demo.bind.service.BindService");
        boolean ret = bindService(intent, mConnection, BIND_AUTO_CREATE);
        Log.i("!!!!!!", String.format("bind service ret:%s", ret));

        findViewById(R.id.btn_next).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), BindServiceActivity.class)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}