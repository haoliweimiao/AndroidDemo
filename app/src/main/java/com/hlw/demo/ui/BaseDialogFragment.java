package com.hlw.demo.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.hlw.demo.R;

/**
 * @param <T> R.layout.fragment_*
 * @author von.wu
 * DialogFragment弹窗基类: 默认宽度为屏幕宽度80%，不可点击外围隐藏dialog，无标题栏
 */
public abstract class BaseDialogFragment<T extends ViewDataBinding> extends DialogFragment {

    /**
     * dialog fragment view binding object
     */
    private T mBinding;

    /**
     * dialog确认、取消按钮点击监听
     */
    private BaseFragmentDialogClickListener mBaseClickListener;

    /**
     * dialog dismiss监听
     */
    private BaseFragmentDialogDismissListener mDialogDismissListener;
    /**
     * 是否对用户可见
     */
    private boolean mIsUserCanSee = false;

    /**
     * 加载布局
     */
    protected abstract int initLayout();

    /**
     * 初始化界面
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化监听器
     */
    protected abstract void initListener();

    public void setButtonClickListener(BaseFragmentDialogClickListener listener) {
        this.mBaseClickListener = listener;
    }

    public void setDialogDismissListener(BaseFragmentDialogDismissListener listener) {
        this.mDialogDismissListener = listener;
    }

    /**
     * 设置默认点击外围不隐藏dialog
     */
    @Override
    public boolean isCancelable() {
        return false;
    }

    /**
     * 默认设置非透明背景
     */
    protected boolean isTransparentBackground() {
        return false;
    }

    /**
     * LayoutInfalte.inflater#attachToParent
     */
    protected boolean attachToParent() {
        return false;
    }

    /**
     * dialog 竖屏模式下默认占用的宽度比
     */
    protected float dialogPortWidthScale() {
        return 0.80f;
    }

    /**
     * dialog 横屏模式下默认占用的宽度比
     */
    protected float dialogLandWidthScale() {
        return 0.45f;
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

    protected T getBinding() {
        return mBinding;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme());
        //创建dialog fragment 仅设置这两个属性无效，需在创建DialogFragment时对其进行设置setCancelable->false
        //false：dialog弹出后会点击屏幕或物理返回键，dialog不消失
        dialog.setCancelable(isCancelable());
        //false：dialog弹出后会点击屏幕，dialog不消失；点击物理返回键dialog消失
        dialog.setCanceledOnTouchOutside(isCancelable());
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog_FullScreen_NoDim);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        //将自带的背景设置为透明
        if (isTransparentBackground() && getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        mBinding = DataBindingUtil.inflate(inflater, initLayout(), container, attachToParent());

        initData();

        initView();

        initListener();

        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null && getActivity() != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = (int) ((dm.widthPixels * dialogWidthScale(getActivity()) * 100.f) / 100);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
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


    /**
     * show fragment
     *
     * @param manager FragmentManager
     */
    public void show(FragmentManager manager) {
        show(manager, this.getTag());
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            Fragment prev = manager.findFragmentByTag(tag);
            if (prev != null) {
                manager.beginTransaction().remove(prev).commit();
            }
            super.show(manager, tag);
        } catch (Exception e) {
            //同一实例使用不同的tag会异常,这里捕获一下
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        if (mDialogDismissListener != null) {
            mDialogDismissListener.dialogDismiss();
        }
        //重写dismiss,防止出现以下异常
        //java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
        super.dismissAllowingStateLoss();
    }

    public String getShowTag() {
        return getClass().getSimpleName();
    }

    protected void onUserVisibleChange(boolean isUserCanSee) {

    }
}
