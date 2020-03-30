package com.hlw.demo.activity.opengl;

import android.content.Context;
import android.opengl.GLES30;

public class ColorShaderProgram extends ShaderProgram {

    /**
     * Uniform location
     */
    private final int uMatrixLocation;

    /**
     * Attribute location
     */
    private final int aColorLocation;

    private final int aPositionLocation;

    public ColorShaderProgram(Context context) {
        super(context, "glsl/simple_vertex_shader_2.glsl", "glsl/simple_fragment_shader_2.glsl");

        uMatrixLocation = GLES30.glGetUniformLocation(programId, U_MATRIX);

        aColorLocation = GLES30.glGetAttribLocation(programId, A_COLOR);
        aPositionLocation = GLES30.glGetAttribLocation(programId, A_POSITION);
    }

    public void setUniforms(float[] matrix) {
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }


    public int getColorLocation() {
        return aColorLocation;
    }

    public int getPositionLocation() {
        return aPositionLocation;
    }

    public int getMatrixLocation() {
        return uMatrixLocation;
    }

}