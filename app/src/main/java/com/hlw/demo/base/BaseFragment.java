package com.hlw.demo.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Fragment基类
 */
public abstract class BaseFragment<ViewFragment extends ViewDataBinding> extends Fragment {

    /**
     * 是否对用户可见
     */
    protected boolean isUserCanSee = false;

    protected ViewFragment mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, initLayout(), container, false);
        initData();
        initView();
        initListener();
        return mBinding.getRoot();
    }

    protected abstract int initLayout();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initListener();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if ((isVisibleToUser && isResumed())) {
            onResume();
        } else if (!isVisibleToUser) {
            onPause();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            isUserCanSee = true;
            onUserVisibleChange(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getUserVisibleHint()) {
            isUserCanSee = false;
            onUserVisibleChange(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            isUserCanSee = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected void onUserVisibleChange(boolean isUserCanSee) {

    }
}