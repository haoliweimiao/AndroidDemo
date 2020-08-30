package com.hlw.moudle.opengl;

import android.opengl.GLES30;

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

    private final FloatVertexArray vertexArray;

    public OpenGLTable() {
        vertexArray = new FloatVertexArray(VERTEX_DATA);
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
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, 6);
    }
}
