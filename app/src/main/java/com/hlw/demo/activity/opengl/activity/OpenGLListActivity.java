package com.hlw.demo.activity.opengl.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.hlw.demo.R;
import com.hlw.demo.activity.opengl.OpenGLDrawType;
import com.hlw.demo.activity.opengl.OpenglDemoActivity;
import com.hlw.demo.databinding.ActivityOpenglListBinding;
import com.hlw.library.ui.BaseActivity;

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
        getBinding().btnDemo1.setOnClickListener(this);
        getBinding().btnAirHockey.setOnClickListener(this);
        getBinding().btnDrawTriangle.setOnClickListener(this);
        getBinding().btnDrawTriangle2.setOnClickListener(this);
        getBinding().btnDrawColorTriangle.setOnClickListener(this);
        getBinding().btnDrawTwoTriangle.setOnClickListener(this);
        getBinding().btnDrawRectangle.setOnClickListener(this);
        getBinding().btnDraw3dBox.setOnClickListener(this);
        getBinding().btnDraw2dMipmap.setOnClickListener(this);
        getBinding().btnDrawSimpleTexture.setOnClickListener(this);
        getBinding().btnDrawSimpleTextureCubemap.setOnClickListener(this);
        getBinding().btnDrawTextureWrap.setOnClickListener(this);
        getBinding().btnDrawImageTexture.setOnClickListener(this);
        getBinding().btnDrawSmileBoxTexture.setOnClickListener(this);
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
                OpenGLDrawDemoleActivity.start(this, OpenGLDrawType.RECTANGLE.name());
                break;
            case R.id.btn_draw_3d_box:
                OpenGLDrawDemoleActivity.start(this, OpenGLDrawType.OPEN_3D_BOX.name());
                break;
            case R.id.btn_draw_2d_mipmap:
                OpenGLDrawDemoleActivity.start(this, OpenGLDrawType.OPEN_MIPMAP_2D.name());
                break;
            case R.id.btn_draw_simple_texture:
                OpenGLDrawDemoleActivity.start(this, OpenGLDrawType.OPEN_SIMPLE_TEXTURE.name());
                break;
            case R.id.btn_draw_simple_texture_cubemap:
                OpenGLDrawDemoleActivity.start(this, OpenGLDrawType.OPEN_SIMPLE_TEXTURE_CUBE_MAP.name());
                break;
            case R.id.btn_draw_texture_wrap:
                OpenGLDrawDemoleActivity.start(this, OpenGLDrawType.OPEN_TEXTURE_WARP.name());
                break;
            case R.id.btn_draw_image_texture:
                OpenGLDrawDemoleActivity.start(this, OpenGLDrawType.OPEN_IMAGE_TEXTURE.name());
                break;
            case R.id.btn_draw_smile_box_texture:
                OpenGLDrawDemoleActivity.start(this, OpenGLDrawType.SMILE_BOX_TEXTURE.name());
                break;
            default:
                break;
        }
    }
}
