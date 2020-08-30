package com.hlw.demo.dialog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hlw.demo.R;
import com.hlw.library.ui.BaseDialog;

public class DownloadAskDialog extends BaseDialog {

    public DownloadAskDialog(@NonNull Context context) {
        super(context);
    }

    public DownloadAskDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DownloadAskDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected int initLayout() {
        return R.layout.dialog_download_ask;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

}
