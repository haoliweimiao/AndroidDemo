package com.hlw.demo.activity.opengl.activity;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.text.TextUtils;
import android.widget.Toast;

import com.hlw.demo.R;
import com.hlw.demo.activity.opengl.OpenGLDrawType;
import com.hlw.demo.base.BaseActivity;
import com.hlw.demo.databinding.ActivityOpenglDrawDemoBinding;
import com.hlw.demo.util.CheckUtils;

public class OpenGLDrawDemoleActivity extends BaseActivity<ActivityOpenglDrawDemoBinding> {

    private static final String DRAW_TYPE_STR = "draw_type_str";

    private GLSurfaceView mGLSurfaceView;
    private boolean redererSet = false;

    private String mDrawType;

    public static void start(Context context, String drawType) {
        Intent intent = new Intent(context, OpenGLDrawDemoleActivity.class);
        intent.putExtra(DRAW_TYPE_STR, drawType);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_opengl_draw_demo;
    }

    @Override
    protected void initData() {
        mDrawType = getIntent().getStringExtra(DRAW_TYPE_STR);
        if (TextUtils.isEmpty(mDrawType)) {
            Toast.makeText(OpenGLDrawDemoleActivity.this, "Draw type is null", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void initView() {
        mGLSurfaceView = new GLSurfaceView(this);

        boolean isSupportGL2 = CheckUtils.isSupportOpenGl3(this);

        if (isSupportGL2) {
            //Request opengl 2.0 compatible context
            mGLSurfaceView.setEGLContextClientVersion(3);

            mGLSurfaceView.setRenderer(OpenGLDrawType.getRenderer(this, mDrawType));
            redererSet = true;

            mBinding.rlContent.addView(mGLSurfaceView);
        } else {
            Toast.makeText(this, "This device cannot support opengl es 2.0", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (redererSet) {
            mGLSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (redererSet) {
            mGLSurfaceView.onResume();
        }
    }
}
