package com.hlw.moudle.opengl.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.hlw.moudle.opengl.OpenGLDrawType;
import com.hlw.library.ui.BaseActivity;
import com.hlw.moudle.opengl.OpenGLDemoActivity;
import com.hlw.moudle.opengl.R;
import com.hlw.moudle.opengl.databinding.ActivityOpenglListBinding;

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
//                OpenGLDemoActivity.start(this);
                Intent intent = new Intent("com.hlw.opengl.demo.intent");
                startActivity(intent);
                break;
            case R.id.btn_air_hockey:
                OpenGLAirHockeyActivity.start(this);
                break;
            case R.id.btn_draw_triangle:
                OpenGLDrawDemoActivity.start(this, OpenGLDrawType.TRIANGLE.name());
                break;
            case R.id.btn_draw_triangle2:
                OpenGLDrawDemoActivity.start(this, OpenGLDrawType.TRIANGLE2.name());
                break;
            case R.id.btn_draw_color_triangle:
                OpenGLDrawDemoActivity.start(this, OpenGLDrawType.COLOR_TRIANGLE.name());
                break;
            case R.id.btn_draw_two_triangle:
                OpenGLDrawDemoActivity.start(this, OpenGLDrawType.TWO_TRIANGLE.name());
                break;
            case R.id.btn_draw_rectangle:
                OpenGLDrawDemoActivity.start(this, OpenGLDrawType.RECTANGLE.name());
                break;
            case R.id.btn_draw_3d_box:
                OpenGLDrawDemoActivity.start(this, OpenGLDrawType.OPEN_3D_BOX.name());
                break;
            case R.id.btn_draw_2d_mipmap:
                OpenGLDrawDemoActivity.start(this, OpenGLDrawType.OPEN_MIPMAP_2D.name());
                break;
            case R.id.btn_draw_simple_texture:
                OpenGLDrawDemoActivity.start(this, OpenGLDrawType.OPEN_SIMPLE_TEXTURE.name());
                break;
            case R.id.btn_draw_simple_texture_cubemap:
                OpenGLDrawDemoActivity.start(this, OpenGLDrawType.OPEN_SIMPLE_TEXTURE_CUBE_MAP.name());
                break;
            case R.id.btn_draw_texture_wrap:
                OpenGLDrawDemoActivity.start(this, OpenGLDrawType.OPEN_TEXTURE_WARP.name());
                break;
            case R.id.btn_draw_image_texture:
                OpenGLDrawDemoActivity.start(this, OpenGLDrawType.OPEN_IMAGE_TEXTURE.name());
                break;
            case R.id.btn_draw_smile_box_texture:
                OpenGLDrawDemoActivity.start(this, OpenGLDrawType.SMILE_BOX_TEXTURE.name());
                break;
            default:
                break;
        }
    }
}
