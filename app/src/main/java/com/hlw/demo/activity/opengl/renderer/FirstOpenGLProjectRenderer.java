package com.hlw.demo.activity.opengl.renderer;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author hlw
 */
public class FirstOpenGLProjectRenderer implements GLSurfaceView.Renderer {
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES30.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        //clear the rendering surface
        //清空屏幕，擦除所有颜色，使用 glClearColor 填充的颜色当背景
        GLES30.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }
}
