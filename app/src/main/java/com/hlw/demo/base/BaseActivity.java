package com.hlw.demo.base;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hlw.demo.R;

import java.util.List;

public abstract class BaseActivity<ViewBinding extends ViewDataBinding>
        extends AppCompatActivity
        implements View.OnClickListener {

    protected ViewBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, initLayout());

        initData();
        initView();
        initListener();
    }

    protected abstract int initLayout();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initListener();

    @Override
    public void onClick(View v) {

    }
}
