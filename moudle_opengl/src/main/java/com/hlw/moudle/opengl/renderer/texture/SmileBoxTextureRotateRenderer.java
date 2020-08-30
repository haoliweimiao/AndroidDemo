package com.hlw.moudle.opengl.renderer.texture;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.hlw.demo.activity.opengl.IntVertexArray;
import com.hlw.moudle.opengl.FloatVertexArray;
import com.hlw.moudle.opengl.R;
import com.hlw.moudle.opengl.ShaderHelper;
import com.hlw.moudle.opengl.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES30.GL_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES30.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_FLOAT;
import static android.opengl.GLES30.GL_STATIC_DRAW;
import static android.opengl.GLES30.GL_TEXTURE0;
import static android.opengl.GLES30.GL_TEXTURE1;
import static android.opengl.GLES30.GL_TEXTURE_2D;
import static android.opengl.GLES30.glActiveTexture;
import static android.opengl.GLES30.glBindBuffer;
import static android.opengl.GLES30.glBindTexture;
import static android.opengl.GLES30.glBindVertexArray;
import static android.opengl.GLES30.glBufferData;
import static android.opengl.GLES30.glClear;
import static android.opengl.GLES30.glClearColor;
import static android.opengl.GLES30.glEnableVertexAttribArray;
import static android.opengl.GLES30.glGenBuffers;
import static android.opengl.GLES30.glGenVertexArrays;
import static android.opengl.GLES30.glVertexAttribPointer;

public class SmileBoxTextureRotateRenderer implements GLSurfaceView.Renderer {

    private final float VERTICES[] = {
            // positions     // texture coords
            0.5f, 0.5f, 0.0f, 1.0f, 1.0f, // top right
            0.5f, -0.5f, 0.0f, 1.0f, 0.0f, // bottom right
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, // bottom left
            -0.5f, 0.5f, 0.0f, 0.0f, 1.0f  // top left
    };
    private final int INDICES[] = {
            0, 1, 3, // first triangle
            1, 2, 3  // second triangle
    };
    private Context context;
    private FloatVertexArray vertexArray;

    private IntVertexArray indexArray;

    private int mProgramId;

    private int mTextureId1, mTextureId2;
    private int uTextureId1, uTextureId2;

    private int[] mVAOId = new int[1], mVBOId = new int[1], mEBOId = new int[1];

    public SmileBoxTextureRotateRenderer(Context context) {
        this.context = context;
        vertexArray = new FloatVertexArray(VERTICES);
        indexArray = new IntVertexArray(INDICES);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        final int vertexId = ShaderHelper.compileVertexShaderByAssetsPath(context, "glsl/texture/smile_box_rotate_vertex.glsl");
        final int fragmentId = ShaderHelper.compileFragmentShaderByAssetsPath(context, "glsl/texture/smile_box_rotate_fragment.glsl");
        mProgramId = ShaderHelper.linkProgram(vertexId, fragmentId);

        GLES30.glEnable(GLES30.GL_BLEND);
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);

        glGenVertexArrays(1, mVAOId, 0);
        glGenBuffers(1, mVBOId, 0);
        glGenBuffers(1, mEBOId, 0);

        glBindVertexArray(mVAOId[0]);

        glBindBuffer(GL_ARRAY_BUFFER, mVBOId[0]);
        glBufferData(GL_ARRAY_BUFFER, vertexArray.getVertexSize(), vertexArray.getFloatBuffer(), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mEBOId[0]);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexArray.getVertexSize(), indexArray.getIntBuffer(), GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * 4, 0 * 4);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * 4, 3 * 4);
        glEnableVertexAttribArray(1);

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
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, mTextureId1);

        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, mTextureId2);

    }
}
