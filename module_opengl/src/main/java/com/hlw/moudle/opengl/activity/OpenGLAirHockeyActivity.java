package com.hlw.moudle.opengl.activity;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.widget.Toast;

import com.hlw.library.ui.BaseActivity;
import com.hlw.moudle.opengl.R;
import com.hlw.moudle.opengl.databinding.ActivityAirHockeyDemoBinding;
import com.hlw.moudle.opengl.renderer.AirHockeyRenderer2;
import com.hlw.moudle.opengl.util.CheckUtils;

public class OpenGLAirHockeyActivity extends BaseActivity<ActivityAirHockeyDemoBinding> {

    private GLSurfaceView mGLSurfaceView;
    private boolean redererSet = false;

    public static void start(Context context) {
        context.startActivity(new Intent(context, OpenGLAirHockeyActivity.class));
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_air_hockey_demo;
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

//            mGLSurfaceView.setRenderer(new AirHockeyRenderer(this));
            mGLSurfaceView.setRenderer(new AirHockeyRenderer2(this));
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
