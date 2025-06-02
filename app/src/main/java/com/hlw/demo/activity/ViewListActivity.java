package com.hlw.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.hlw.demo.R;
import com.hlw.demo.databinding.ActivityViewListBinding;
import com.hlw.demo.ui.BaseActivity;

/**
 * ViewListActivity
 *
 * @author hlw
 */
public class ViewListActivity extends BaseActivity<ActivityViewListBinding> {

    /**
     * start to ViewListActivity
     *
     * @param context context
     */
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
        getBinding().btnDialogLoadingShow.setOnClickListener(this);
        getBinding().btnLoadingViewsShow.setOnClickListener(this);
        getBinding().btnHorizontalViewDemo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dialog_loading_show:
                LoadingDialogShowActivity.start(this);
                break;
            case R.id.btn_loading_views_show:
                LoadingViewsActivity.start(this);
                break;
            case R.id.btn_horizontal_view_demo:
                HorizontalDemoActivity.start(this);
                break;
            default:
                break;
        }
    }
}
