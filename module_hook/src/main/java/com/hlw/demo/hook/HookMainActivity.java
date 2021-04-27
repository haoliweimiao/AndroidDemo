package com.hlw.demo.hook;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class HookMainActivity extends AppCompatActivity {

    // 这个方法比onCreate调用早; 在这里Hook比较好.
    @Override
    protected void attachBaseContext(Context newBase) {
        HookHelper.hookActivityManager();
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        Button tv = new Button(this);
        tv.setText("测试界面");

        setContentView(tv);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 测试AMS HOOK (调用其相关方法)
                Uri uri = Uri.parse("http://wwww.baidu.com");
                Intent t = new Intent(Intent.ACTION_VIEW);
                t.setData(uri);
                startActivity(t);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        HashMap<String, UsbDevice> deviceHashMap = ((UsbManager)
                HookMainActivity.this.getSystemService(Context.USB_SERVICE)).getDeviceList();
        for (Map.Entry<String, UsbDevice> entry : deviceHashMap.entrySet()) {
            UsbDevice device = entry.getValue();
            Log.d("usb device", String.format("usb device pid:%s vid:%s name:%s %s", device.getProductId(), device.getVendorId(), device.getDeviceName(), device.getProductName()));
        }
    }
}