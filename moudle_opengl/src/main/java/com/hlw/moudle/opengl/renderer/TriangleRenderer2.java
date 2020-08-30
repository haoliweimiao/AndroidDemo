package com.hlw.moudle.opengl.renderer;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.hlw.moudle.opengl.OpenGLConstants;
import com.hlw.moudle.opengl.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TriangleRenderer2 implements GLSurfaceView.Renderer {

    private Context context;

    private float[] VERTICES = new float[]{
            0.0f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
    };


    private FloatBuffer vertexBuffer;
    private int shaderProgram;

    public TriangleRenderer2(Context context) {
        this.context = context;

        vertexBuffer =
                // allocateDirect 分配内存，一个float 32个字节，一个int 8个字节，*4
                ByteBuffer.allocateDirect(VERTICES.length * OpenGLConstants.BYTES_PER_FLOAT)
                        //按照本地字节序(大小端)组织内容(当一个值占多个字节时，比如32位整形数，
                        //字节按照从最重要位到最不重要位或者相反顺序排列;可以认为从左到右或者从右到左写一个数类似)
                        .order(ByteOrder.nativeOrder())
                        .asFloatBuffer()
                        .put(VERTICES);
        vertexBuffer.position(0);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        int vertex = ShaderHelper.compileVertexShaderByAssetsPath(context, "glsl/triangle/vertex_3_0.glsl");
        int fragment = ShaderHelper.compileFragmentShaderByAssetsPath(context, "glsl/triangle/fragment_3_0.glsl");

        shaderProgram = ShaderHelper.linkProgram(vertex, fragment);

        ShaderHelper.validateProgram(shaderProgram);

        GLES30.glUseProgram(shaderProgram);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //clear the rendering surface
        //清空屏幕，擦除所有颜色，使用 glClearColor 填充的颜色当背景
//        GLES30.glClear(GL10.GL_COLOR_BUFFER_BIT);

//        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);


        //绘制圆点
        //准备坐标数据
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);
//        //启用顶点的句柄
        GLES30.glEnableVertexAttribArray(0);

//        //绘制三个点
//        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, 3);

        //绘制直线
//        GLES30.glDrawArrays(GLES30.GL_LINE_STRIP, 0, 2);
//        GLES30.glLineWidth(10);

        //绘制三角形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);

        //禁止顶点数组的句柄
        GLES30.glDisableVertexAttribArray(0);

    }
}
