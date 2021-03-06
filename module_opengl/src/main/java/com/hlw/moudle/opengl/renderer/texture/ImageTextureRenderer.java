package com.hlw.moudle.opengl.renderer.texture;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.hlw.demo.activity.opengl.IntVertexArray;
import com.hlw.moudle.opengl.DataBindHelper;
import com.hlw.moudle.opengl.FloatVertexArray;
import com.hlw.moudle.opengl.R;
import com.hlw.moudle.opengl.ShaderHelper;
import com.hlw.moudle.opengl.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;

public class ImageTextureRenderer implements GLSurfaceView.Renderer {
    // set up vertex data (and buffer(s)) and configure vertex attributes
    // ------------------------------------------------------------------
    private final float[] vertices = {
            // positions          // colors           // textureId coords
            0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, // top right
            0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, // bottom right
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, // bottom left
            -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f  // top left
    };
    private final int[] indices = {
            0, 1, 3, // first triangle
            1, 2, 3  // second triangle
    };
    private final FloatVertexArray vertexData;
    private final IntVertexArray indexData;
    //    private final String U_TEXTURE = "u_texture";
    private Context context;
    private int[] mVAOId = new int[1];
    private int[] mVBOId = new int[1];
    private int[] mEBOId = new int[1];

    private int mProgramId;
    private int mTextureId;
//    private int uTextureLocation;

    public ImageTextureRenderer(Context context) {
        this.context = context;

        vertexData = new FloatVertexArray(vertices);

        indexData = new IntVertexArray(indices);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        int vertexId = ShaderHelper.compileVertexShaderByAssetsPath(context, "glsl/texture/vertex_image_texture.glsl");
        int fragmentId = ShaderHelper.compileFragmentShaderByAssetsPath(context, "glsl/texture/fragment_image_texture.glsl");
        mProgramId = ShaderHelper.linkProgram(vertexId, fragmentId);
        ShaderHelper.validateProgram(mProgramId);

//        uTextureLocation = GLES30.glGetUniformLocation(mProgramId, U_TEXTURE);

        GLES30.glGenVertexArrays(1, mVAOId, 0);
        GLES30.glGenBuffers(1, mVBOId, 0);
        GLES30.glGenBuffers(1, mEBOId, 0);

        GLES30.glBindVertexArray(mVAOId[0]);

        DataBindHelper.bindArrayData(mVBOId, vertices, vertexData.getFloatBuffer());

        DataBindHelper.bindElementArrayBuffer(mEBOId, indices, indexData.getIntBuffer());

        // position attribute
        GLES30.glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * 4, 0 * 4);
        glEnableVertexAttribArray(0);
        // color attribute
        GLES30.glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * 4, 3 * 4);
        glEnableVertexAttribArray(1);
        // textureId coord attribute
        GLES30.glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * 4, 6 * 4);
        glEnableVertexAttribArray(2);

        mTextureId = TextureHelper.loadTexture(context, R.mipmap.container);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // render
        // ------
        GLES30.glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        // bind Texture
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureId);

        // render container
        GLES30.glUseProgram(mProgramId);

        GLES30.glBindVertexArray(mVAOId[0]);


        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_INT, 0);

    }
}
