package com.hlw.demo.activity.opengl.renderer;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.hlw.demo.activity.opengl.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES30.glBindVertexArray;

public class ColorTriangleRenderer implements GLSurfaceView.Renderer {

    private Context context;

    /**
     * Handle to a program object
     */
    private int mProgramObject;

    /**
     * Additional member variables
     */
    private int mWidth;
    private int mHeight;
    private FloatBuffer mVertices;
    private ShortBuffer mIndices;

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


    /**
     * 3 vertices, with (x,y,z) ,(r, g, b, a) per-vertex
     */
    private final float[] mVerticesData =
            {
                    0.0f, 0.5f, 0.0f,        // v0
                    1.0f, 0.0f, 0.0f, 1.0f,  // c0
                    -0.5f, -0.5f, 0.0f,        // v1
                    0.0f, 1.0f, 0.0f, 1.0f,  // c1
                    0.5f, -0.5f, 0.0f,        // v2
                    0.0f, 0.0f, 1.0f, 1.0f,  // c2
            };

    private final short[] mIndicesData =
            {
                    0, 1, 2
            };

    final int VERTEX_POS_SIZE = 3; // x, y and z
    final int VERTEX_COLOR_SIZE = 4; // r, g, b, and a

    final int VERTEX_POS_INDX = 0;
    final int VERTEX_COLOR_INDX = 1;

    final int VERTEX_STRIDE = (4 * (VERTEX_POS_SIZE + VERTEX_COLOR_SIZE));

    public ColorTriangleRenderer(Context context) {
        this.context = context;

        mVertices = ByteBuffer.allocateDirect(mVerticesData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertices.put(mVerticesData).position(0);

        mIndices = ByteBuffer.allocateDirect(mIndicesData.length * 2)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        mIndices.put(mIndicesData).position(0);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        int vertex = ShaderHelper.compileVertexShaderByAssetsPath(context, "glsl/triangle/color_triangle_vertex.glsl");
        int fragment = ShaderHelper.compileFragmentShaderByAssetsPath(context, "glsl/triangle/color_triangle_fragment.glsl");

        // Load the shaders and get a linked program object
        mProgramObject = ShaderHelper.linkProgram(vertex, fragment);

        ShaderHelper.validateProgram(mProgramObject);


        // Generate VAO Id
        GLES30.glGenVertexArrays(1, mVAOId, 0);
        // Generate VBO Ids and load the VBOs with data
        GLES30.glGenBuffers(1, mVBOId, 0);
        // Generate EBO Ids and load the EBOs with data
        GLES30.glGenBuffers(1, mEBOId, 0);

        // bind the Vertex Array Object first, then bind and set vertex buffer(s), and
        // then configure vertex attributes(s).
        glBindVertexArray(mVAOId[0]);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBOId[0]);
        mVertices.position(0);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, mVerticesData.length * 4,
                mVertices, GLES30.GL_STATIC_DRAW);

        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mEBOId[0]);

        mIndices.position(0);
        GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, 2 * mIndicesData.length,
                mIndices, GLES30.GL_STATIC_DRAW);


        GLES30.glVertexAttribPointer(VERTEX_POS_INDX, VERTEX_POS_SIZE,
                GLES30.GL_FLOAT, false, VERTEX_STRIDE,
                0);
        GLES30.glEnableVertexAttribArray(VERTEX_POS_INDX);


        GLES30.glVertexAttribPointer(VERTEX_COLOR_INDX, VERTEX_COLOR_SIZE,
                GLES30.GL_FLOAT, false, VERTEX_STRIDE,
                (VERTEX_POS_SIZE * 4));
        GLES30.glEnableVertexAttribArray(VERTEX_COLOR_INDX);

        //注意，这是允许的，调用glVertexAttribPointer将VBO注册为顶点属性的绑定顶点缓冲区对象，这样我们就可以安全地解除绑定
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // Reset to the default VAO
        GLES30.glBindVertexArray(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
        mWidth = width;
        mHeight = height;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Set the viewport
        GLES30.glViewport(0, 0, mWidth, mHeight);

        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

        // Clear the color buffer
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        // Use the program object
        GLES30.glUseProgram(mProgramObject);

        // Bind the VAO
        GLES30.glBindVertexArray(mVAOId[0]);

        // Draw with the VAO settings
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, mIndicesData.length, GLES30.GL_UNSIGNED_SHORT, 0);

        // Return to the default VAO
        GLES30.glBindVertexArray(0);
    }
}
