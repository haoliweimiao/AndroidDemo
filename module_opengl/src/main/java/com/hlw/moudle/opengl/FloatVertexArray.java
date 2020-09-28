package com.hlw.moudle.opengl;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * FloatVertexArray
 *
 * @author hlw
 */
public class FloatVertexArray {
    /**
     * float buffer
     */
    private final FloatBuffer mFloatBuffer;
    /**
     * float size
     */
    private static final int BYTES_PER_FLOAT = 4;
    /**
     * vertex size
     */
    private final int VERTEX_SIZE;

    public FloatVertexArray(float[] vertexData) {
        VERTEX_SIZE = vertexData.length * BYTES_PER_FLOAT;

        mFloatBuffer =
                // allocateDirect 分配内存，一个float 32个字节，一个int 8个字节，*4
                ByteBuffer.allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                        //按照本地字节序(大小端)组织内容(当一个值占多个字节时，比如32位整形数，
                        //字节按照从最重要位到最不重要位或者相反顺序排列;可以认为从左到右或者从右到左写一个数类似)
                        .order(ByteOrder.nativeOrder())
                        .asFloatBuffer()
                        .put(vertexData);

    }

    /**
     * getFloatBuffer
     *
     * @return FloatBuffer
     */
    public FloatBuffer getFloatBuffer() {
        mFloatBuffer.position(0);
        return mFloatBuffer;
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
//        LogUtils.i("setVertexAttributePointer dataOffset: " + dataOffset + " attributeLocation: " + attributeLocation +
//                " componentCount: " + componentCount + " stride: " + stride);
        mFloatBuffer.position(dataOffset);
        GLES30.glVertexAttribPointer(attributeLocation, componentCount, GLES30.GL_FLOAT, false, stride, mFloatBuffer);
        GLES30.glEnableVertexAttribArray(attributeLocation);

        mFloatBuffer.position(0);
    }
}
