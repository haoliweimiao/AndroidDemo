package com.hlw.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.hlw.demo.R;
import com.hlw.demo.base.BaseActivity;
import com.hlw.demo.databinding.ActivityViewListBinding;

public class ViewListActivity extends BaseActivity<ActivityViewListBinding> {

    public static void start(Context context) {
        Intent intent = new Intent(context, ViewListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_view_list;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        mBinding.btnDialogLoadingShow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dialog_loading_show:
                LoadingDialogShowActivity.start(this);
                break;
            default:
                break;
        }
    }
}
