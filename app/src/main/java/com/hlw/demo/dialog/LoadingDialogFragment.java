package com.hlw.demo.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import com.hlw.demo.R;
import com.hlw.demo.databinding.DialogLoadingBinding;
import com.hlw.demo.ui.BaseDialogFragment;

public class LoadingDialogFragment extends BaseDialogFragment<DialogLoadingBinding> {

    private ValueAnimator animator;

    private boolean mIsUserCanSee;

    public static LoadingDialogFragment newInstance() {
        LoadingDialogFragment dialog = new LoadingDialogFragment();
        Bundle bundle = new Bundle();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected int initLayout() {
        return R.layout.dialog_loading;
    }

    @Override
    protected void initData() {
        animator = ValueAnimator.ofFloat(0, 360);
        animator.setTarget(getBinding().ivLoading);
        //设置动画执行次数
//        animator.setRepeatCount(ValueAnimator.INFINITE);
        //设置动画执行模式
//        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setDuration(1666);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                getBinding().ivLoading.setRotation(value);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mIsUserCanSee) {
                    //部分机型不在post方法内部 animator.start(); 不会生效
                    getBinding().ivLoading.post(() -> {
                        animator.start();
                    });
                }
            }
        });

        if (!animator.isRunning()) {
            animator.start();
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void onUserVisibleChange(boolean isUserCanSee) {
        this.mIsUserCanSee = isUserCanSee;
        if (isUserCanSee) {
            if (animator != null && !animator.isRunning()) {
                animator.start();
            }
        } else {
            if (animator != null) {
                animator.end();
            }
        }
    }

}
