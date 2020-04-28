package com.hlw.demo;


import android.view.View;
import android.widget.Toast;

import com.hlw.demo.activity.ViewListActivity;
import com.hlw.demo.activity.opengl.activity.OpenGLListActivity;
import com.hlw.demo.base.BaseActivity;
import com.hlw.demo.databinding.ActivityMainBinding;
import com.hlw.demo.ndk.NDKTest;
import com.hlw.demo.ndk.TestUtils;

/**
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
        Toast.makeText(MainActivity.this, TestUtils.hello(),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void initListener() {
        mBinding.btnView.setOnClickListener(this);
        mBinding.btnOpenGl.setOnClickListener(this);
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
            default:
                break;
        }
    }
}
