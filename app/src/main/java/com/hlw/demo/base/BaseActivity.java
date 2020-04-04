package com.hlw.demo.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public abstract class BaseActivity<ViewBinding extends ViewDataBinding>
        extends AppCompatActivity
        implements View.OnClickListener {

    protected ViewBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hiddenNav();

        mBinding = DataBindingUtil.setContentView(this, initLayout());

        initData();
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hiddenNav();
    }

    private void hiddenNav() {
        if (getWindow() != null) {
            View decorView = getWindow().getDecorView();
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
//            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN;

            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    protected abstract int initLayout();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initListener();

    @Override
    public void onClick(View v) {

    }
}
