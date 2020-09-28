package com.hlw.moudle.opengl;

import android.content.Context;
import android.opengl.GLES30;

import com.hlw.moudle.opengl.ShaderProgram;

/**
 * OpenGL ES ColorShaderProgram
 *
 * @author hlw
 */
public class ColorShaderProgram extends ShaderProgram {

    /**
     * Uniform location
     */
    private final int uMatrixLocation;

    /**
     * Color Attribute location
     */
    private final int aColorLocation;

    /**
     * Position Attribute location
     */
    private final int aPositionLocation;

    public ColorShaderProgram(Context context) {
        super(context, "glsl/simple_vertex_shader_2.glsl", "glsl/simple_fragment_shader_2.glsl");

        uMatrixLocation = GLES30.glGetUniformLocation(programId, U_MATRIX);

        aColorLocation = GLES30.glGetAttribLocation(programId, A_COLOR);
        aPositionLocation = GLES30.glGetAttribLocation(programId, A_POSITION);
    }

    /**
     * setUniforms
     *
     * @param matrix matrix
     */
    public void setUniforms(float[] matrix) {
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }

    /**
     * get color location
     *
     * @return color location
     */
    public int getColorLocation() {
        return aColorLocation;
    }

    /**
     * get position location
     *
     * @return position location
     */
    public int getPositionLocation() {
        return aPositionLocation;
    }

    /**
     * get matrix location
     *
     * @return matrix location
     */
    public int getMatrixLocation() {
        return uMatrixLocation;
    }

}