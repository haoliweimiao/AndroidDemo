package com.hlw.moudle.opengl.activity;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.text.TextUtils;
import android.widget.Toast;

import com.hlw.moudle.opengl.OpenGLDrawType;
import com.hlw.library.ui.BaseActivity;
import com.hlw.moudle.opengl.R;
import com.hlw.moudle.opengl.databinding.ActivityOpenglDrawDemoBinding;
import com.hlw.moudle.opengl.util.CheckUtils;

public class OpenGLDrawDemoActivity extends BaseActivity<ActivityOpenglDrawDemoBinding> {

    private static final String DRAW_TYPE_STR = "draw_type_str";

    private GLSurfaceView mGLSurfaceView;
    private boolean redererSet = false;

    private String mDrawType;

    public static void start(Context context, String drawType) {
        Intent intent = new Intent(context, OpenGLDrawDemoActivity.class);
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
            Toast.makeText(OpenGLDrawDemoActivity.this, "Draw type is null", Toast.LENGTH_SHORT).show();
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

            getBinding().rlContent.addView(mGLSurfaceView);
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
