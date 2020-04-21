package com.hlw.demo.activity.opengl.renderer.texture;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.hlw.demo.R;
import com.hlw.demo.activity.opengl.FloatVertexArray;
import com.hlw.demo.activity.opengl.IntVertexArray;
import com.hlw.demo.activity.opengl.ShaderHelper;
import com.hlw.demo.activity.opengl.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SmileBoxTextureRenderer implements GLSurfaceView.Renderer {

    private Context context;

    private final float VERTICES[] = {
            // positions          // colors           // texture coords
            0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, // top right
            0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, // bottom right
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, // bottom left
            -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f  // top left
    };

    private final int INDICES[] = {
            0, 1, 3, // first triangle
            1, 2, 3  // second triangle
    };

    private FloatVertexArray vertexArray;

    private IntVertexArray indexArray;

    private int mProgramId;

    private int mTextureId1, mTextureId2;
    private int uTextureId1, uTextureId2;

    private int[] mVAOId = new int[1], mVBOId = new int[1], mEBOId = new int[1];

    public SmileBoxTextureRenderer(Context context) {
        this.context = context;
        vertexArray = new FloatVertexArray(VERTICES);
        indexArray = new IntVertexArray(INDICES);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        final int vertexId = ShaderHelper.compileVertexShaderByAssetsPath(context, "glsl/texture/smile_box_vertex.glsl");
        final int fragmentId = ShaderHelper.compileFragmentShaderByAssetsPath(context, "glsl/texture/smile_box_fragment.glsl");
        mProgramId = ShaderHelper.linkProgram(vertexId, fragmentId);

//        GLES30.glEnable(GLES30.GL_BLEND);
//        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);
//        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_DST_ALPHA);

        GLES30.glGenVertexArrays(1, mVAOId, 0);
        GLES30.glGenBuffers(1, mVBOId, 0);
        GLES30.glGenBuffers(1, mEBOId, 0);

        GLES30.glBindVertexArray(mVAOId[0]);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBOId[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertexArray.getVertexSize(), vertexArray.getFloatBuffer(), GLES30.GL_STATIC_DRAW);

        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mEBOId[0]);
        GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, indexArray.getVertexSize(), indexArray.getIntBuffer(), GLES30.GL_STATIC_DRAW);

        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 8 * 4, 0 * 4);
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glVertexAttribPointer(1, 3, GLES30.GL_FLOAT, false, 8 * 4, 3 * 4);
        GLES30.glEnableVertexAttribArray(1);
        GLES30.glVertexAttribPointer(2, 2, GLES30.GL_FLOAT, false, 8 * 4, 6 * 4);
        GLES30.glEnableVertexAttribArray(2);

        mTextureId1 = TextureHelper.loadTexture(context, R.mipmap.container);
        mTextureId2 = TextureHelper.loadTexture(context, R.mipmap.awesomeface);

        uTextureId1 = GLES30.glGetUniformLocation(mProgramId, "uTexture1");
        uTextureId2 = GLES30.glGetUniformLocation(mProgramId, "uTexture2");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        // bind textures on corresponding texture units
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureId1);
        GLES30.glUniform1i(uTextureId1, 0);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureId2);
        GLES30.glUniform1i(uTextureId1, 1);

        GLES30.glUseProgram(mProgramId);
        GLES30.glBindVertexArray(mVAOId[0]);
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_INT, 0);
    }
}
