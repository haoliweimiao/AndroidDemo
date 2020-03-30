package com.hlw.demo.activity.opengl;

import android.content.Context;
import android.opengl.GLES30;

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

        uMatrixLocation = GLES30.glGetUniformLocation(programId, U_MATRIX);
        uTextureUnitLocation = GLES30.glGetUniformLocation(programId, U_TEXTURE_UNIT);

        aPositionLocation = GLES30.glGetAttribLocation(programId, A_POSITION);
        aTextureCoordinatesLocation = GLES30.glGetAttribLocation(programId, A_TEXTURE_COORDINATES);
    }

    public void setUniforms(float[] matrix, int textureId) {
        // 传入变化矩阵到shaderProgram
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        // 激活纹理单元0
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        // 绑定纹理对象ID
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
        // 告诉shaderProgram sampler2D纹理采集器 使用纹理单元0的纹理对象。
        GLES30.glUniform1i(uTextureUnitLocation, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesLocation() {
        return aTextureCoordinatesLocation;
    }
}