package com.hlw.demo.activity.opengl;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class FloatVertexArray {
    private final FloatBuffer floatBuffer;
    private static final int BYTES_PER_FLOAT = 4;
    private final int vertexSize;

    public FloatVertexArray(float[] vertexData) {
        vertexSize = vertexData.length * BYTES_PER_FLOAT;

        floatBuffer =
                // allocateDirect 分配内存，一个float 32个字节，一个int 8个字节，*4
                ByteBuffer.allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                        //按照本地字节序(大小端)组织内容(当一个值占多个字节时，比如32位整形数，
                        //字节按照从最重要位到最不重要位或者相反顺序排列;可以认为从左到右或者从右到左写一个数类似)
                        .order(ByteOrder.nativeOrder())
                        .asFloatBuffer()
                        .put(vertexData);

    }

    public FloatBuffer getFloatBuffer() {
        floatBuffer.position(0);
        return floatBuffer;
    }

    public int getVertexSize() {
        return vertexSize;
    }

    public void setVertexAttributePointer(int dataOffset, int attributeLocation,
                                          int componentCount, int stride) {
//        LogUtils.i("setVertexAttributePointer dataOffset: " + dataOffset + " attributeLocation: " + attributeLocation +
//                " componentCount: " + componentCount + " stride: " + stride);
        floatBuffer.position(dataOffset);
        GLES30.glVertexAttribPointer(attributeLocation, componentCount, GLES30.GL_FLOAT, false, stride, floatBuffer);
        GLES30.glEnableVertexAttribArray(attributeLocation);

        floatBuffer.position(0);
    }
}
