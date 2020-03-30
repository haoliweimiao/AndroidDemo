package com.hlw.demo.activity.opengl;

import android.opengl.GLES20;

public class OpenGLTable {
    private int POSITION_COMPONENT_COUNT = 2;
    private int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * OpenGLConstants.BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            //Order of coordinates: X Y S T
            // 裁剪高度依据宽高比调整T
            // Triangle Fan
            0f, 0f, 0.5f, 0.5f,
            -0.5f, 0.8f, 0f, 0.9f,
            0.5f, -0.8f, 1f, 0.9f,
            0.5f, 0.8f, 1f, 0.1f,
            -0.5f, 0.8f, 0f, 0.1f,
            -0.5f, -0.8f, 0f, 0.9f
    };

    private final VertexArray vertexArray;

    public OpenGLTable() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(TextureShaderProgram shaderProgram) {
        vertexArray.setVertexAttributePointer(
                0,
                shaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );
        vertexArray.setVertexAttributePointer(
                POSITION_COMPONENT_COUNT,
                shaderProgram.getTextureCoordinatesLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE
        );
    }

    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
    }
}
