package com.hlw.demo;


import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.hlw.demo.activity.ViewListActivity;
import com.hlw.demo.activity.opengl.activity.OpenGLListActivity;
import com.hlw.demo.base.BaseActivity;
import com.hlw.demo.databinding.ActivityMainBinding;
import com.hlw.demo.ndk.TestUtils;
import com.hlw.demo.util.LogUtils;

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
//        AssetManager assetManager = getAssets();
//        Typeface typeface = Typeface.createFromAsset(assetManager, "fonts/DIGITAL-Regular.ttf");
//        mBinding.tvTest.setTypeface(typeface);
//        Toast.makeText(MainActivity.this, NDKTest.hello(),Toast.LENGTH_SHORT).show();
        Toast.makeText(MainActivity.this, TestUtils.hello(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void initListener() {
        mBinding.btnView.setOnClickListener(this);
        mBinding.btnOpenGl.setOnClickListener(this);
        mBinding.btnOpenGlDemo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_view:
                ViewListActivity.start(this);
                break;
            case R.id.btn_open_gl:
                OpenGLListActivity.start(this);
                break;
            case R.id.btn_open_gl_demo: {
                Intent intent = new Intent("opengl.demo.triangle");
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        long startActivityTime = PreferenceUtils.getLong(SPFiles.FILE_COMMON, SPKeys.COMMON_APP_START_TIME_LONG);
        long startActivityTime = ((DemoApplication) getApplication()).getStartApplicationTime();
        LogUtils.i(String.format("start application total time: %s ms", startActivityTime));
    }
}
