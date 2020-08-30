package com.hlw.library.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * Activity 基类
 *
 * @param <ViewBinding> layout DataBinding
 * @author von
 */
public abstract class BaseActivity<ViewBinding extends ViewDataBinding>
        extends AppCompatActivity
        implements View.OnClickListener {

    /**
     * view binding object
     */
    private ViewBinding mBinding;

    protected ViewBinding getBinding() {
        return mBinding;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);

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

    /**
     * 初始化布局文件
     */
    protected abstract int initLayout();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 初始化监听器
     */
    protected abstract void initListener();

    @Override
    public void onClick(View v) {

    }

    /**
     * 运行任务在Activity正常时
     */
    protected void runOnSafeMainThread(Runnable runnable) {
        if (!isFinishing()) {
            runOnUiThread(runnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
