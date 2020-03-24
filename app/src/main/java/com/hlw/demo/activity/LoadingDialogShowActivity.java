package com.hlw.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.hlw.demo.R;
import com.hlw.demo.base.BaseActivity;
import com.hlw.demo.databinding.ActivityLoadingDialogBinding;
import com.hlw.demo.dialog.DownloadAskDialog;
import com.hlw.demo.dialog.DownloadProcessDialog;
import com.hlw.demo.dialog.LoadingDialogFragment;

/**
 * @author hlw
 * @date 2020-03-22 16:22:21
 */
public class LoadingDialogShowActivity extends BaseActivity<ActivityLoadingDialogBinding> {

    public static void start(Context context) {
        Intent intent = new Intent(context, LoadingDialogShowActivity.class);
        context.startActivity(intent);
    }


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
        mBinding.btnShowLoadingDialog.setOnClickListener(this);
        mBinding.btnShowDownloadAskDialog.setOnClickListener(this);
        mBinding.btnShowDownloadProcessDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show_download_ask_dialog: {
                DownloadAskDialog dialog = new DownloadAskDialog(this);
                dialog.show();
            }
            break;
            case R.id.btn_show_loading_dialog: {
                LoadingDialogFragment loadingDialog = LoadingDialogFragment.newInstance();
                loadingDialog.show(getSupportFragmentManager(), loadingDialog.getShowTag());
            }
            case R.id.btn_show_download_process_dialog: {
                DownloadProcessDialog dialog = new DownloadProcessDialog(this);
                dialog.show();
            }
            break;
            default:
                break;
        }
    }
}
