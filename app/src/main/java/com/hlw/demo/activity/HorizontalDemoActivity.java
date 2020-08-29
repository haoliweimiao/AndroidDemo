package com.hlw.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;

import com.hlw.demo.R;
import com.hlw.demo.base.BaseActivity;
import com.hlw.demo.databinding.ActivityHorizontalDemoBinding;

/**
 * HorizontalDemoActivity
 * @author hlw
 */
public class HorizontalDemoActivity extends BaseActivity<ActivityHorizontalDemoBinding> {

    /**
     * start to HorizontalDemoActivity
     * @param context context
     */
    public static void start(Context context) {
        Intent intent = new Intent(context, HorizontalDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_horizontal_demo;
    }

    @Override
    protected void initData() {
        String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"};
        ArrayAdapter<String> numberAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, numbers);
        ArrayAdapter<String> letterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, letters);

        mBinding.llOne.setAdapter(numberAdapter);
        mBinding.llTwo.setAdapter(letterAdapter);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }
}
