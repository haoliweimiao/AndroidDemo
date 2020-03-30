package com.hlw.demo.activity.opengl;

import android.content.Context;
import android.opengl.GLES20;

public class TextureShaderProgram extends ShaderProgram {

    /**
     * Uniform location
     */
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;

    /**
     * Attribute location
     */
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;

    public TextureShaderProgram(Context context) {
        super(context, "glsl/texture_vertex_shader_2.glsl", "glsl/texture_fragment_shader_2.glsl");

        uMatrixLocation = GLES20.glGetUniformLocation(programId, U_MATRIX);
        uTextureUnitLocation = GLES20.glGetUniformLocation(programId, U_TEXTURE_UNIT);

        aPositionLocation = GLES20.glGetAttribLocation(programId, A_POSITION);
        aTextureCoordinatesLocation = GLES20.glGetAttribLocation(programId, A_TEXTURE_COORDINATES);
    }

    public void setUniforms(float[] matrix, int textureId) {
        // 传入变化矩阵到shaderProgram
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        // 激活纹理单元0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // 绑定纹理对象ID
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        // 告诉shaderProgram sampler2D纹理采集器 使用纹理单元0的纹理对象。
        GLES20.glUniform1i(uTextureUnitLocation, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesLocation() {
        return aTextureCoordinatesLocation;
    }
}