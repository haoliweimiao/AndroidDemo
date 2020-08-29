package com.hlw.demo.base;

import android.annotation.SuppressLint;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public abstract class BaseFragmentActivity<ViewBinding extends ViewDataBinding> extends BaseActivity<ViewBinding> {


    protected abstract int initFragmentContentLayout();

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
