package com.hlw.moudle.opengl;

import android.content.Context;
import android.opengl.GLES30;

import com.hlw.moudle.opengl.util.AssetsUtil;

public class ShaderProgram {

    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";

    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    protected final int programId;

    public ShaderProgram(String vertexShaderResourceStr,
                         String fragmentShaderResourceStr) {
        programId = ShaderHelper.buildProgram(
                vertexShaderResourceStr,
                fragmentShaderResourceStr);
    }

    public ShaderProgram(Context context, String vertexShaderAssetsPath,
                         String fragmentShaderAssetsPath) {
        programId = ShaderHelper.buildProgram(
                AssetsUtil.getFromAssets(context, vertexShaderAssetsPath),
                AssetsUtil.getFromAssets(context, fragmentShaderAssetsPath));
    }

    public void useProgram() {
        // Set the current OpenGL shader program to this program
        GLES30.glUseProgram(programId);
    }

    public int getShaderProgramId() {
        return programId;
    }

    public void deleteProgram() {
        GLES30.glDeleteProgram(programId);
    }
}
