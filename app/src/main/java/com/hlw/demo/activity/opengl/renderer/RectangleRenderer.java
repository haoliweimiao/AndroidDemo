package com.hlw.demo.activity.opengl.renderer;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.hlw.demo.activity.opengl.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES30.GL_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_FLOAT;
import static android.opengl.GLES30.GL_STATIC_DRAW;
import static android.opengl.GLES30.glBindBuffer;
import static android.opengl.GLES30.glBindVertexArray;
import static android.opengl.GLES30.glBufferData;
import static android.opengl.GLES30.glEnableVertexAttribArray;
import static android.opengl.GLES30.glGenBuffers;
import static android.opengl.GLES30.glGenVertexArrays;

public class RectangleRenderer implements GLSurfaceView.Renderer {

    private Context context;

    private int mProgramId;

    // set up vertex data (and buffer(s)) and configure vertex attributes
    // ------------------------------------------------------------------
    private final float vertices[] = {
            0.5f, 0.5f, 0.0f, // top right
            0.5f, -0.5f, 0.0f, // bottom right
            -0.5f, -0.5f, 0.0f, // bottom left
            -0.5f, 0.5f, 0.0f  // top left
    };
    private final int indices[] = {
            // note that we start from 0!
            0, 1, 3, // first Triangle
            1, 2, 3  // second Triangle
    };

    private int mScreenWidth, mScreenHeight;

    final int VERTEX_POS_SIZE = 3; // x, y and z

    final int VERTEX_POS_INDX = 0;

    final int VERTEX_STRIDE = (4 * (VERTEX_POS_SIZE));

    private FloatBuffer vertexBuffer;
    private IntBuffer indexBuffer;

    /**
     * VertexBufferObject Ids
     */
    private int[] mVBOIds = new int[1];

    /**
     * VertexArrayObject Id
     */
    private int[] mVAOId = new int[1];

    /**
     * ElementBufferObject Id
     */
    private int[] mEBOId = new int[1];

    public RectangleRenderer(Context context) {
        this.context = context;

        vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertices);
        vertexBuffer.position(0);

        indexBuffer = ByteBuffer.allocateDirect(indices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer()
                .put(indices);
        indexBuffer.position(0);

    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        int vertexId = ShaderHelper.compileVertexShaderByAssetsPath(context, "glsl/rectangle/vertex.glsl");
        int fragmentId = ShaderHelper.compileFragmentShaderByAssetsPath(context, "glsl/rectangle/fragment.glsl");
        mProgramId = ShaderHelper.linkProgram(vertexId, fragmentId);
        ShaderHelper.validateProgram(mProgramId);

        glGenVertexArrays(1, mVAOId, 0);
        glGenBuffers(1, mVBOIds, 0);
        glGenBuffers(1, mEBOId, 0);

        // bind the Vertex Array Object first, then bind and set vertex buffer(s), and
        // then configure vertex attributes(s).
        glBindVertexArray(mVAOId[0]);

        // glBindBuffer函数把新创建的缓冲绑定到GL_ARRAY_BUFFER目标上：->VBO
        // 从这一刻起，我们使用的任何（在GL_ARRAY_BUFFER目标上的）缓冲调用都会用来配置当前绑定的缓冲(VBO)。
        // 然后我们可以调用glBufferData函数，它会把之前定义的顶点数据复制到缓冲的内存中：
        glBindBuffer(GL_ARRAY_BUFFER, mVBOIds[0]);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * 4, vertexBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mEBOId[0]);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.length * 4, indexBuffer,
                GL_STATIC_DRAW);

        GLES30.glVertexAttribPointer(VERTEX_POS_INDX, VERTEX_POS_SIZE, GL_FLOAT, false, VERTEX_STRIDE, 0);
        glEnableVertexAttribArray(0);

        // note that this is allowed, the call to glVertexAttribPointer registered VBO
        // as the vertex attribute's bound vertex buffer object so afterwards we can
        // safely unbind
        // 注意，这是允许的，调用glVertexAttribPointer将VBO注册为顶点属性的绑定顶点缓冲区对象，这样我们就可以安全地解除绑定
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // remember: do NOT unbind the EBO while a VAO is active as the bound element
        // buffer object IS stored in the VAO; keep the EBO bound.
        // glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        // You can unbind the VAO afterwards so other VAO calls won't accidentally
        // modify this VAO, but this rarely happens. Modifying other VAOs requires a
        // call to glBindVertexArray anyways so we generally don't unbind VAOs (nor
        // VBOs) when it's not directly necessary.
        // 请记住:当VAO处于活动状态时，不要取消对EBO的绑定，因为绑定的元素缓冲区对象存储在VAO中;
        // 保持EBO绑定。glBindBuffer (GL_ELEMENT_ARRAY_BUFFER 0);
        // 您可以在以后解除VAO的绑定，这样其他的VAO调用就不会意外地修改这个VAO，但是这种情况很少发生。
        // 修改其他的VAOs需要调用 glBindVertexArray，所以当没有直接必要时，我们通常不会取消绑定VAOs(或VBOs)。
        glBindVertexArray(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
        this.mScreenHeight = height;
        this.mScreenWidth = width;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // render
        // ------
        GLES30.glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        // draw our first triangle
        GLES30.glUseProgram(mProgramId);
        GLES30.glBindVertexArray(mVAOId[0]);
        // seeing as we only have a single VAO there's no need to bind it
        // every time, but we'll do so to keep things a bit more organized
        // glDrawArrays(GL_TRIANGLES, 0, 6);
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_INT, 0);
    }
}
