package com.hlw.demo;


import android.view.View;

import com.hlw.demo.base.BaseActivity;
import com.hlw.demo.databinding.ActivityMainBinding;

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

    }

    @Override
    protected void initListener() {
        mBinding.btnView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_view:

                break;
            default:
                break;
        }
    }
}
