package com.hlw.library.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


/**
 * lazy fragment懒加载类
 *
 * @param <ViewFragment> layout DataBinding
 * @author von
 */
public abstract class BaseLazyFragment<ViewFragment extends ViewDataBinding> extends Fragment {

    /**
     * fragment view binding object
     */
    private ViewFragment mBinding;

    /**
     * Fragment当前状态是否可见
     */
    private boolean mIsVisible;
    /**
     * 是否是首次进入获取数据(还未首次加载数据为true)
     */
    private boolean mIsFirst = true;
    /**
     * 界面是否已经准备好可以加载创建
     */
    private boolean mIsPrepared;
    /**
     * 界面是否已经加载数据
     */
    private boolean mIsReady = false;


    protected ViewFragment getBinding() {
        return mBinding;
    }

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


    /**
     * init layout
     *
     * @return R.layout.fragment_*
     */
    public abstract int initLayout();

    /**
     * init view
     */
    protected abstract void initView();

    /**
     * 初始化数据
     * 使用懒加载模式，不在此处加载数据（仅在界面被调到前台获取数据）
     */
    protected abstract void initData();

    private void lazyLoad() {
        if (mIsPrepared && mIsVisible && mIsFirst) {
            initData();
            mIsFirst = false;
            mIsReady = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIsPrepared = true;
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
