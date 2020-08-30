package com.hlw.demo.activity.opengl;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/**
 * IntVertexArray
 *
 * @author hlw
 */
public class IntVertexArray {
    /**
     * IntBuffer
     */
    private final IntBuffer mIntBuffer;
    /**
     * integer size
     */
    private static final int BYTES_PER_INTEGER = 4;
    private final int VERTEX_SIZE;

    public IntVertexArray(int[] vertexData) {
        VERTEX_SIZE = vertexData.length * BYTES_PER_INTEGER;

        mIntBuffer =
                // allocateDirect 分配内存，一个float 32个字节，一个int 8个字节，*4
                ByteBuffer.allocateDirect(vertexData.length * BYTES_PER_INTEGER)
                        //按照本地字节序(大小端)组织内容(当一个值占多个字节时，比如32位整形数，
                        //字节按照从最重要位到最不重要位或者相反顺序排列;可以认为从左到右或者从右到左写一个数类似)
                        .order(ByteOrder.nativeOrder())
                        .asIntBuffer()
                        .put(vertexData);

    }

    /**
     * getIntBuffer
     *
     * @return IntBuffer
     */
    public IntBuffer getIntBuffer() {
        mIntBuffer.position(0);
        return mIntBuffer;
    }

    /**
     * getVertexSize
     *
     * @return VertexSize
     */
    public int getVertexSize() {
        return VERTEX_SIZE;
    }

    /**
     * setVertexAttributePointer
     *
     * @param dataOffset        dataOffset
     * @param attributeLocation attributeLocation
     * @param componentCount    componentCount
     * @param stride            stride
     */
    public void setVertexAttributePointer(int dataOffset, int attributeLocation,
                                          int componentCount, int stride) {
        mIntBuffer.position(dataOffset);
        GLES30.glVertexAttribPointer(attributeLocation, componentCount, GLES30.GL_FLOAT, false, stride, mIntBuffer);
        GLES30.glEnableVertexAttribArray(attributeLocation);

        mIntBuffer.position(0);
    }
}
