package com.hlw.demo.activity.opengl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBufferData;

public class DataBindHelper {

    /**
     * bind array data to OpenGL es GL_ARRAY_BUFFER
     *
     * @param mVBOId       VBO ID
     * @param vertices     vertices
     * @param vertexBuffer cache buffer
     */
    public static void bindArrayData(int[] mVBOId, float[] vertices, FloatBuffer vertexBuffer) {
        vertexBuffer.position(0);
        // glBindBuffer函数把新创建的缓冲绑定到GL_ARRAY_BUFFER目标上：->VBO
        // 从这一刻起，我们使用的任何（在GL_ARRAY_BUFFER目标上的）缓冲调用都会用来配置当前绑定的缓冲(VBO)。
        // 然后我们可以调用glBufferData函数，它会把之前定义的顶点数据复制到缓冲的内存中：
        glBindBuffer(GL_ARRAY_BUFFER, mVBOId[0]);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * 4, vertexBuffer, GL_STATIC_DRAW);
    }

    /**
     * bind element array data to OpenGL es GL_ELEMENT_ARRAY_BUFFER
     *
     * @param mEBOId      EBO ID
     * @param indices     indices
     * @param indexBuffer cache buffer
     */
    public static void bindElementArrayBuffer(int[] mEBOId, int[] indices, IntBuffer indexBuffer) {
        indexBuffer.position(0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mEBOId[0]);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.length * 4, indexBuffer,
                GL_STATIC_DRAW);
    }

}
