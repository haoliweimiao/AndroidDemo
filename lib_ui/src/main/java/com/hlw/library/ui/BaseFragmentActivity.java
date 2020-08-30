package com.hlw.library.ui;

import android.annotation.SuppressLint;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

/**
 * @param <ViewBinding> R.layout.fragment_*
 * @author von
 * FragmentActivity基类
 */
public abstract class BaseFragmentActivity<ViewBinding extends ViewDataBinding> extends BaseActivity<ViewBinding> {

    /**
     * 初始化Fragment
     */
    protected abstract int initFragmentContentLayout();

    /**
     * 切换Fragment
     *
     * @param fragment 需要显示的fragment
     */
    private void switchFragment(Class<? extends Fragment> fragment) {
        String tag = fragment.getSimpleName();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment baseFragment = manager.findFragmentByTag(tag);
        @SuppressLint("RestrictedApi") List<Fragment> list = manager.getFragments();

        for (Fragment f : list) {
            if (f == null) {
                continue;
            }

            if (f == baseFragment) {
                transaction.show(f);
            } else {
                transaction.hide(f);
            }
        }

        if (baseFragment == null) {
            try {
                baseFragment = fragment.newInstance();
                transaction.add(initFragmentContentLayout(), baseFragment, tag);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        transaction.commitAllowingStateLoss();
    }
}
