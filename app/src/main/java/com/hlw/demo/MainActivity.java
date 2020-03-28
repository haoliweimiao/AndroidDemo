package com.hlw.demo;


import android.view.View;

import com.hlw.demo.activity.ViewListActivity;
import com.hlw.demo.activity.opengl.OpenGLListActivity;
import com.hlw.demo.base.BaseActivity;
import com.hlw.demo.databinding.ActivityMainBinding;

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
