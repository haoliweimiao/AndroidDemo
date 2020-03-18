package com.hlw.demo.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseDialog<ViewDialog extends ViewDataBinding> extends DialogFragment
        implements DialogInterface.OnKeyListener {

    protected float scale = 0.88f;

    protected ViewDialog mBinding;

    protected boolean isCancelable = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme());
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelable);
        dialog.setOnKeyListener(this);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
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

    protected abstract int initLayout();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initListener();

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = (int) ((dm.widthPixels * scale * 100.f) / 100);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void show(FragmentManager manager) {
        show(manager, String.valueOf(System.currentTimeMillis()));
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            Fragment prev = manager.findFragmentByTag(tag);
            if (prev != null) {
                manager.beginTransaction().remove(prev).commitAllowingStateLoss();
            }
            super.show(manager, tag);
        } catch (Exception e) {
            //同一实例使用不同的tag会异常,这里捕获一下
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismissAllowingStateLoss();
            return true;
        } else {
            //这里注意当不是返回键时需将事件扩散，否则无法处理其他点击事件
            return false;
        }
    }

    @Override
    public void dismiss() {
        //重写dismiss,防止出现以下异常
        //java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
        super.dismissAllowingStateLoss();
    }
}
