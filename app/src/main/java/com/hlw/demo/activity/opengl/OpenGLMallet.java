package com.hlw.demo.activity.opengl;

import android.opengl.GLES30;

public class OpenGLMallet {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * OpenGLConstants.BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            // 两个木槌的质点位置 X Y R G B
            0f, -0.4f, 0f, 0f, 0f,
            0f, 0.4f, 0f, 0f, 0f
    };

    private final VertexArray vertexArray;

    public OpenGLMallet() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(ColorShaderProgram shaderProgram) {
        vertexArray.setVertexAttributePointer(
                0,
                shaderProgram.getPositionLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );

        vertexArray.setVertexAttributePointer(
                POSITION_COMPONENT_COUNT,
                shaderProgram.getColorLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE
        );

    }

    public void draw() {
        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, 2);
    }
}
