package com.hlw.demo.activity.opengl.renderer;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.hlw.demo.R;
import com.hlw.demo.activity.opengl.ColorShaderProgram;
import com.hlw.demo.activity.opengl.OpenGLMallet;
import com.hlw.demo.activity.opengl.OpenGLTable;
import com.hlw.demo.activity.opengl.ShaderHelper;
import com.hlw.demo.activity.opengl.TextureHelper;
import com.hlw.demo.activity.opengl.TextureShaderProgram;
import com.hlw.demo.util.LogUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;

public class AirHockeyRenderer2 implements GLSurfaceView.Renderer {

    private final Context context;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private OpenGLTable table;
    private OpenGLMallet mallet;

    private TextureShaderProgram textureShaderProgram;
    private ColorShaderProgram colorShaderProgram;

    private int texture;


    public AirHockeyRenderer2(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        table = new OpenGLTable();
        mallet = new OpenGLMallet();

        textureShaderProgram = new TextureShaderProgram(context);
        colorShaderProgram = new ColorShaderProgram(context);

        texture = TextureHelper.loadTexture(context, R.mipmap.bg_green);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

        //draw table
        textureShaderProgram.useProgram();
        textureShaderProgram.setUniforms(projectionMatrix, texture);
        table.bindData(textureShaderProgram);
        table.draw();

        //draw the mallets
        colorShaderProgram.useProgram();
        colorShaderProgram.setUniforms(projectionMatrix);
        mallet.bindData(colorShaderProgram);
        mallet.draw();
    }
}
