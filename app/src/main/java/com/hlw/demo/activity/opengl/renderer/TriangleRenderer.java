package com.hlw.demo.activity.opengl.renderer;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.hlw.demo.activity.opengl.ShaderHelper;
import com.hlw.demo.activity.opengl.VertexArray;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TriangleRenderer implements GLSurfaceView.Renderer {

    private Context context;

    private final float VERTICES[] = {
            // left
            -0.5f, -0.5f, 0.0f, 1.0f,
            // right
            0.0f, -0.5f, 0.0f, 1.0f,
            // top
            0.0f, 0.0f, 0.0f, 1.0f
    };

    private VertexArray vertexArray;
    //vec4(1.0f, 0.5f, 0.2f, 1.0f)

    private final String A_POSITION = "a_Position";
    private int aPositionLocation;
    private final String U_COLOR = "u_Color";
    private int uColorLocation;


    private static int POSITION_COUNT = 4;
    private static int STRIDE = POSITION_COUNT * 4;

    public TriangleRenderer(Context context) {
        this.context = context;

        vertexArray = new VertexArray(VERTICES);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        int vertex = ShaderHelper.compileVertexShaderByAssetsPath(context, "glsl/triangle/vertex.glsl");
        int fragment = ShaderHelper.compileFragmentShaderByAssetsPath(context, "glsl/triangle/fragment.glsl");
        int program = ShaderHelper.linkProgram(vertex, fragment);

        ShaderHelper.validateProgram(program);

        GLES30.glUseProgram(program);

        aPositionLocation = GLES30.glGetAttribLocation(program, A_POSITION);
        uColorLocation = GLES30.glGetUniformLocation(program, U_COLOR);
        GLES30.glUniform4f(uColorLocation, 1.0f, 0.5f, 0.2f, 1.0f);

        vertexArray.setVertexAttributePointer(0, aPositionLocation, POSITION_COUNT, STRIDE);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //clear the rendering surface
        //清空屏幕，擦除所有颜色，使用 glClearColor 填充的颜色当背景
        GLES30.glClear(GL10.GL_COLOR_BUFFER_BIT);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);
    }
}
