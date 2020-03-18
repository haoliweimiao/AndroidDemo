package com.hlw.demo.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * lazy fragment懒加载类
 */
public abstract class BaseLazyFragment<ViewFragment extends ViewDataBinding> extends Fragment {

    protected ViewFragment mBinding;

    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;
    /**
     * 是否是首次进入获取数据(还未首次加载数据为true)
     */
    protected boolean isFirst = true;
    /**
     * 界面是否已经准备好可以加载创建
     */
    protected boolean isPrepared;
    /**
     * 界面是否已经加载数据
     */
    protected boolean isReady = false;


    //    ①isPrepared参数在系统调用onActivityCreated时设置为true,这时onCreateView方法已调用完毕
    //      (一般我们在这方法里执行findviewbyid等方法),确保 initData()方法不会报空指针异常。
    //    ②isVisible参数在fragment可见时通过系统回调setUserVisibileHint方法设置为true,不可见时为false，这是fragment实现懒加载的关键。
    //    ③isFirst确保ViewPager来回切换时BaseFragment的initData方法不会被重复调用，initData在该Fragment的整个生命周期只调用一次,
    //      第一次调用initData()方法后马上执行 isFirst = false。
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = initLayout();
        if (layoutId == 0) {
            return null;
        }
        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false);
        initView();
        return mBinding.getRoot();
    }


    protected void invalidateOptionsMenu() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.supportInvalidateOptionsMenu();
        }
    }

    @Override
    public void setArguments(Bundle args) {
        if (!isAdded()) {
            super.setArguments(args);
        }
    }


    public abstract int initLayout();

    protected abstract void initView();

    /**
     * 初始化数据
     * 使用懒加载模式，不在此处加载数据（仅在界面被调到前台获取数据）
     */
    protected abstract void initData();

    private void lazyLoad() {
        if (isPrepared && isVisible && isFirst) {
            initData();
            isFirst = false;
            isReady = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }

    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * 不可见
     */
    protected void onInvisible() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
