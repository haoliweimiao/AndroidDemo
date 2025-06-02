package com.hlw.demo;


import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Toast;

import com.hlw.demo.activity.ViewListActivity;
import com.hlw.demo.databinding.ActivityMainBinding;
import com.hlw.demo.hook.HookMainActivity;
import com.hlw.demo.util.LogUtils;
import com.hlw.demo.ui.BaseActivity;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * MainActivity
 *
 * @author hlw
 */
public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        AssetManager assetManager = getAssets();
        Typeface typeface = Typeface.createFromAsset(assetManager, "fonts/DIGITAL-Regular.ttf");
        getBinding().tvTest.setTypeface(typeface);
        Toast.makeText(MainActivity.this, getMacAddress("eth0"), Toast.LENGTH_SHORT).show();
    }

    /**
     * Returns MAC address of the given interface name.
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    public static String getMacAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    @Override
    protected void initListener() {
        getBinding().btnView.setOnClickListener(this);
        getBinding().btnHookDemo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_view:
                ViewListActivity.start(this);
                break;
            case R.id.btn_hook_demo:
                HookMainActivity.start(this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        long startActivityTime = ((DemoApplication) getApplication()).getStartApplicationTime();
        LogUtils.i(String.format("start application total time: %s ms", startActivityTime));
    }
}
