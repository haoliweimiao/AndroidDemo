package com.hlw.demo.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;


/**
 * Fragment基类
 *
 * @param <ViewFragment> R.layout.fragment_*
 * @author von
 */
public abstract class BaseFragment<ViewFragment extends ViewDataBinding> extends Fragment {

    /**
     * 是否对用户可见
     */
    private boolean mIsUserCanSee = false;

    /**
     * fragment view binding object
     */
    private ViewFragment mBinding;

    protected ViewFragment getBinding() {
        return mBinding;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, initLayout(), container, false);
        initData();
        initView();
        initListener();
        return mBinding.getRoot();
    }

    /**
     * 初始化布局
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            onResume();
        } else if (!isVisibleToUser) {
            onPause();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            mIsUserCanSee = true;
            onUserVisibleChange(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getUserVisibleHint()) {
            mIsUserCanSee = false;
            onUserVisibleChange(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            mIsUserCanSee = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * Callback
     * 界面用户是否可见
     *
     * @param isUserCanSee true：可见
     */
    protected void onUserVisibleChange(boolean isUserCanSee) {

    }
}