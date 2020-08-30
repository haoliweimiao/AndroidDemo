package com.hlw.library.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * @param <DialogView> R.layout.dialog_* data binding view
 * @author von
 * @date 2020年08月06日13:54:02
 * Dialog通用基类
 */
public abstract class BaseDialog<DialogView extends ViewDataBinding> extends Dialog {

    /**
     * dialog view binding object
     */
    private DialogView mBinding;

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /**
     * dialog 竖屏模式下默认占用的宽度比
     */
    protected float dialogPortWidthScale() {
        return 0.30f;
    }

    /**
     * dialog 横屏模式下默认占用的宽度比
     */
    protected float dialogLandWidthScale() {
        return 0.65f;
    }

    /**
     * dialog默认宽度(占据屏幕宽度 竖屏80% 横屏65%)
     */
    public float dialogWidthScale(@NonNull Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(displayMetrics);
            float width = displayMetrics.widthPixels;
            float height = displayMetrics.heightPixels;
            return width > height ? dialogLandWidthScale() : dialogPortWidthScale();
        }
        return dialogPortWidthScale();
    }

    protected DialogView getBinding() {
        return mBinding;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), initLayout(), null, false);
        setContentView(mBinding.getRoot());
        if (getWindow() != null) {
            //软键盘不遮挡输入框
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            //设置为透明背景
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        initData();
        initView();
        initListener();
    }

    protected abstract int initLayout();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initListener();
}
