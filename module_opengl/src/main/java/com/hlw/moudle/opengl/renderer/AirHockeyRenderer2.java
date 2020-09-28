package com.hlw.moudle.opengl.renderer;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.hlw.moudle.opengl.ColorShaderProgram;
import com.hlw.moudle.opengl.MatrixHelper;
import com.hlw.moudle.opengl.OpenGLMallet;
import com.hlw.moudle.opengl.OpenGLTable;
import com.hlw.moudle.opengl.R;
import com.hlw.moudle.opengl.TextureHelper;
import com.hlw.moudle.opengl.TextureShaderProgram;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES30.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES30.glClear;
import static android.opengl.GLES30.glClearColor;

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
        GLES30.glViewport(0, 0, width, height);

        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);
        MatrixHelper.setIdentityM(modelMatrix, 0);
        MatrixHelper.translateM(modelMatrix, 0, 0, 0, -3f);
        MatrixHelper.rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);
        final float[] temp = new float[16];
        MatrixHelper.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, projectionMatrix.length);
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
