package com.hlw.demo.activity.opengl.renderer.texture;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.hlw.demo.R;
import com.hlw.demo.activity.opengl.DataBindHelper;
import com.hlw.demo.activity.opengl.FloatVertexArray;
import com.hlw.demo.activity.opengl.IntVertexArray;
import com.hlw.demo.activity.opengl.ShaderHelper;
import com.hlw.demo.activity.opengl.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;

public class ImageTextureRenderer implements GLSurfaceView.Renderer {
    private final String U_TEXTURE = "u_texture";
    private Context context;

    // set up vertex data (and buffer(s)) and configure vertex attributes
    // ------------------------------------------------------------------
    private final float vertices[] = {
            // positions          // colors           // texture coords
            0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, // top right
            0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, // bottom right
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, // bottom left
            -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f  // top left
    };
    private final int indices[] = {
            0, 1, 3, // first triangle
            1, 2, 3  // second triangle
    };

    private final FloatVertexArray vertexData;
    private final IntVertexArray indexData;

    private int[] mVAOId = new int[1];
    private int[] mVBOId = new int[1];
    private int[] mEBOId = new int[1];

    private int mProgramId;
    private int mTextureId;
    private int uTextureLocation;

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

        uTextureLocation = GLES30.glGetUniformLocation(mProgramId, U_TEXTURE);

        GLES30.glGenVertexArrays(1, mVAOId, 0);
        GLES30.glGenBuffers(1, mVBOId, 0);
        GLES30.glGenBuffers(1, mEBOId, 0);

        DataBindHelper.bindArrayData(mVBOId, vertices, vertexData.getFloatBuffer());

        DataBindHelper.bindElementArrayBuffer(mEBOId, indices, indexData.getIntBuffer());

        GLES30.glVertexAttribPointer(1, 8, GL_FLOAT, false, 8 * 4, 0);
        glEnableVertexAttribArray(0);

        // position attribute
        GLES30.glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * 4, 0);
        glEnableVertexAttribArray(0);
        // color attribute
        GLES30.glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * 4, 3);
        glEnableVertexAttribArray(1);
        // texture coord attribute
        GLES30.glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * 4, 6);
        glEnableVertexAttribArray(2);

        mTextureId = TextureHelper.loadTexture(context, R.mipmap.bg_green);

        // Bind the texture
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureId);

        // 告诉shaderProgram sampler2D纹理采集器 使用纹理单元0的纹理对象。
        GLES30.glUniform1i(uTextureLocation, 0);


        // 注意，这是允许的，调用glVertexAttribPointer将VBO注册为顶点属性的绑定顶点缓冲区对象，这样我们就可以安全地解除绑定
//        glBindBuffer(GL_ARRAY_BUFFER, 0);
//
//        GLES30.glBindVertexArray(0);

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

        // Bind the texture
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureId);

        // Set the sampler texture unit to 0
        GLES30.glUniform1i(uTextureLocation, 0);

        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_INT, 0);

    }
}
