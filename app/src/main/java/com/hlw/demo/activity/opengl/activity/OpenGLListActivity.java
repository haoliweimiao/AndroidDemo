package com.hlw.demo.activity.opengl.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.hlw.demo.R;
import com.hlw.demo.activity.opengl.OpenGLDrawType;
import com.hlw.demo.activity.opengl.OpenglDemoActivity;
import com.hlw.demo.base.BaseActivity;
import com.hlw.demo.databinding.ActivityOpenglListBinding;

public class OpenGLListActivity extends BaseActivity<ActivityOpenglListBinding> {
    public static void start(Context context) {
        context.startActivity(new Intent(context, OpenGLListActivity.class));
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_opengl_list;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        mBinding.btnDemo1.setOnClickListener(this);
        mBinding.btnAirHockey.setOnClickListener(this);
        mBinding.btnDrawTriangle.setOnClickListener(this);
        mBinding.btnDrawTriangle2.setOnClickListener(this);
        mBinding.btnDrawColorTriangle.setOnClickListener(this);
        mBinding.btnDrawTwoTriangle.setOnClickListener(this);
        mBinding.btnDrawRectangle.setOnClickListener(this);
        mBinding.btnDraw3dBox.setOnClickListener(this);
        mBinding.btnDraw2dMipmap.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_demo_1:
                OpenglDemoActivity.start(this);
                break;
            case R.id.btn_air_hockey:
                OpenglAirHockeyActivity.start(this);
                break;
            case R.id.btn_draw_triangle:
                OpenGLDrawDemoleActivity.start(this, OpenGLDrawType.TRIANGLE.name());
                break;
            case R.id.btn_draw_triangle2:
                OpenGLDrawDemoleActivity.start(this, OpenGLDrawType.TRIANGLE2.name());
                break;
            case R.id.btn_draw_color_triangle:
                OpenGLDrawDemoleActivity.start(this, OpenGLDrawType.COLOR_TRIANGLE.name());
                break;
            case R.id.btn_draw_two_triangle:
                OpenGLDrawDemoleActivity.start(this, OpenGLDrawType.TWO_TRIANGLE.name());
                break;
            case R.id.btn_draw_rectangle:
                OpenGLDrawDemoleActivity.start(this, OpenGLDrawType.RECTTRIANGLE.name());
                break;
            case R.id.btn_draw_3d_box:
                OpenGLDrawDemoleActivity.start(this, OpenGLDrawType.OPEN_3D_BOX.name());
                break;
            case R.id.btn_draw_2d_mipmap:
                OpenGLDrawDemoleActivity.start(this, OpenGLDrawType.OPEN_MIPMAP_2D.name());
                break;
            default:
                break;
        }
    }
}
