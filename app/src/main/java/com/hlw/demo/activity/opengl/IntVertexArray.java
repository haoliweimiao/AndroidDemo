package com.hlw.demo.activity.opengl;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class IntVertexArray {
    private final IntBuffer intBuffer;
    private static final int BYTES_PER_FLOAT = 4;

    public IntVertexArray(int[] vertexData) {
        intBuffer =
                // allocateDirect 分配内存，一个float 32个字节，一个int 8个字节，*4
                ByteBuffer.allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                        //按照本地字节序(大小端)组织内容(当一个值占多个字节时，比如32位整形数，
                        //字节按照从最重要位到最不重要位或者相反顺序排列;可以认为从左到右或者从右到左写一个数类似)
                        .order(ByteOrder.nativeOrder())
                        .asIntBuffer()
                        .put(vertexData);

    }

    public IntBuffer getIntBuffer() {
        return intBuffer;
    }

    public void setVertexAttributePointer(int dataOffset, int attributeLocation,
                                          int componentCount, int stride) {
        intBuffer.position(dataOffset);
        GLES30.glVertexAttribPointer(attributeLocation, componentCount, GLES30.GL_FLOAT, false, stride, intBuffer);
        GLES30.glEnableVertexAttribArray(attributeLocation);

        intBuffer.position(0);
    }
}
