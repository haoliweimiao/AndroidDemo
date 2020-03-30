package com.hlw.demo.activity.opengl;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.widget.Toast;

import com.hlw.demo.R;
import com.hlw.demo.activity.opengl.renderer.FirstOpenGLProjectRenderer;
import com.hlw.demo.base.BaseActivity;
import com.hlw.demo.databinding.ActivityOpenglDemoBinding;
import com.hlw.demo.util.CheckUtils;

public class OpenglDemoActivity extends BaseActivity<ActivityOpenglDemoBinding> {

    private GLSurfaceView mGLSurfaceView;
    private boolean redererSet = false;

    public static void start(Context context) {
        context.startActivity(new Intent(context, OpenglDemoActivity.class));
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_opengl_demo;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mGLSurfaceView = new GLSurfaceView(this);

        boolean isSupportGL2 = CheckUtils.isSupportOpenGl3(this);

        if (isSupportGL2) {
            //Request opengl 2.0 compatible context
            mGLSurfaceView.setEGLContextClientVersion(3);

            mGLSurfaceView.setRenderer(new FirstOpenGLProjectRenderer());
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
