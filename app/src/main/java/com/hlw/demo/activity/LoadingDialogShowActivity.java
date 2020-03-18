package com.hlw.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.hlw.demo.R;
import com.hlw.demo.base.BaseActivity;
import com.hlw.demo.databinding.ActivityLoadingDialogBinding;
import com.hlw.demo.dialog.LoadingDialog;

public class LoadingDialogShowActivity extends BaseActivity<ActivityLoadingDialogBinding> {

    public static void start(Context context) {
        Intent intent = new Intent(context, LoadingDialogShowActivity.class);
        context.startActivity(intent);
    }


    private LoadingDialog loadingDialog;

    @Override
    protected int initLayout() {
        return R.layout.activity_loading_dialog;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        mBinding.btnHidden.setOnClickListener(this);
        mBinding.btnShow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_hidden: {
                if (loadingDialog == null) {
                    break;
                }
                loadingDialog.dismiss();
            }
            break;
            case R.id.btn_show: {
                if (loadingDialog == null) {
                    loadingDialog = LoadingDialog.newInstance();
                }
                loadingDialog.show(getSupportFragmentManager(), loadingDialog.getShowTag());
            }
            break;
            default:
                break;
        }
    }
}
