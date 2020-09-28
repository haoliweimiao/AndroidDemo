package com.hlw.moudle.opengl.renderer;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.hlw.moudle.opengl.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES30.GL_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_STATIC_DRAW;
import static android.opengl.GLES30.glBindBuffer;
import static android.opengl.GLES30.glBindVertexArray;
import static android.opengl.GLES30.glBufferData;
import static android.opengl.GLES30.glGenBuffers;
import static android.opengl.GLES30.*;

public class TwoTriangleRenderer implements GLSurfaceView.Renderer {

    private Context context;

    private final float[] vertexData = new float[]{
            0.0f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            -0.5f, 0.5f, 0.0f,
            0.5f, 0.5f, 0.0f,
            0.5f, 0f, 0.0f,
    };

    private final int[] indexData = new int[]{
            0, 1, 2,
            0, 3, 4
    };

    /**
     * VertexBufferObject Ids
     */
    private int[] mVBOId = new int[1];

    /**
     * VertexArrayObject Id
     */
    private int[] mVAOId = new int[1];

    /**
     * ElementBufferObject Id
     */
    private int[] mEBOId = new int[1];

    private final FloatBuffer vertexBuffer;

    private final IntBuffer indexBuffer;

    public TwoTriangleRenderer(Context context) {
        this.context = context;

        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexBuffer.position(0);

        indexBuffer = ByteBuffer.allocateDirect(indexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer()
                .put(indexData);
        indexBuffer.position(0);
    }

    private int mProgramId;
    private int uColorId;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        int vertexId = ShaderHelper.compileVertexShaderByAssetsPath(context, "glsl/triangle/two_triangle_vertex.glsl");
        int fragmentId = ShaderHelper.compileFragmentShaderByAssetsPath(context, "glsl/triangle/two_triangle_fragment.glsl");
        mProgramId = ShaderHelper.linkProgram(vertexId, fragmentId);
        ShaderHelper.validateProgram(mProgramId);

        glGenVertexArrays(1, mVAOId, 0);
        glGenBuffers(1, mVBOId, 0);
        glGenBuffers(1, mEBOId, 0);

        glBindVertexArray(mVAOId[0]);

        glBindBuffer(GL_ARRAY_BUFFER, mVBOId[0]);
        glBufferData(GL_ARRAY_BUFFER, vertexData.length * 4, vertexBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,mEBOId[0]);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,indexData.length*4,indexBuffer,GL_STATIC_DRAW);

        glVertexAttribPointer(0,3,GL_FLOAT,false,3*4,0);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        GLES30.glUseProgram(mProgramId);

        ShaderHelper.setUniform4v(mProgramId,"f_Color.color",0.3f,0.5f,0.5f,1.0f);

        GLES30.glBindVertexArray(mVAOId[0]);
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_INT, 0);

        GLES30.glBindVertexArray(0);
    }
}
