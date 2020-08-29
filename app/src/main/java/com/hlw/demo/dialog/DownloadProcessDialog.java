package com.hlw.demo.dialog;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hlw.demo.R;
import com.hlw.demo.base.BaseDialog;
import com.hlw.demo.databinding.DialogDownloadProcessBinding;

public class DownloadProcessDialog extends BaseDialog<DialogDownloadProcessBinding> {

    public DownloadProcessDialog(@NonNull Context context) {
        super(context);
    }

    public DownloadProcessDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DownloadProcessDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected int initLayout() {
        return R.layout.dialog_download_process;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        ValueAnimator animator = ObjectAnimator.ofFloat(0f, 100f);
        animator.setRepeatCount(-1);
        animator.setDuration(10000);
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            mBinding.tvProcess.setText(String.format("%.2f %s", value, "%"));
        });
        animator.start();
    }

    @Override
    protected void initListener() {

    }

}
