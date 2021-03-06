package com.hlw.moudle.opengl;

import android.content.Context;
import android.opengl.GLES30;


import com.hlw.moudle.opengl.util.AssetsUtil;
import com.hlw.moudle.opengl.util.LogUtils;

import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES30.GL_COMPILE_STATUS;
import static android.opengl.GLES30.GL_FRAGMENT_SHADER;
import static android.opengl.GLES30.GL_LINK_STATUS;
import static android.opengl.GLES30.GL_VALIDATE_STATUS;
import static android.opengl.GLES30.GL_VERTEX_SHADER;
import static android.opengl.GLES30.glAttachShader;
import static android.opengl.GLES30.glCompileShader;
import static android.opengl.GLES30.glCreateProgram;
import static android.opengl.GLES30.glCreateShader;
import static android.opengl.GLES30.glDeleteProgram;
import static android.opengl.GLES30.glDeleteShader;
import static android.opengl.GLES30.glGetProgramInfoLog;
import static android.opengl.GLES30.glGetProgramiv;
import static android.opengl.GLES30.glGetShaderInfoLog;
import static android.opengl.GLES30.glGetShaderiv;
import static android.opengl.GLES30.glLinkProgram;
import static android.opengl.GLES30.glShaderSource;
import static android.opengl.GLES30.glValidateProgram;

public class ShaderHelper {
    private static String TAG = ShaderHelper.class.getSimpleName();

    public static int compileVertexShaderByAssetsPath(Context context, String shaderCodePath) {
        String shaderCode = AssetsUtil.getFromAssets(context, shaderCodePath);
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShaderByAssetsPath(Context context, String shaderCodePath) {
        String shaderCode = AssetsUtil.getFromAssets(context, shaderCodePath);
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        final int shaderObjectId = glCreateShader(type);

        if (shaderObjectId == 0) {
            LogUtils.e(TAG, "Could not create new shader. ");
            return 0;
        }

        glShaderSource(shaderObjectId, shaderCode);
        glCompileShader(shaderObjectId);
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);
        LogUtils.v(TAG, "Results of compiling source:\n" + shaderCode + "\n" + glGetShaderInfoLog(shaderObjectId));

        if (compileStatus[0] == 0) {
            //If it failed, delete the shader object
            glDeleteShader(shaderObjectId);
            LogUtils.e(TAG, "Compilation of shader failed.");
            return 0;
        }

        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programObjectId = glCreateProgram();
        if (programObjectId == 0) {
            LogUtils.w(TAG, "Could not create new program");
            return 0;
        }

        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);

        glLinkProgram(programObjectId);

        final int[] linkStatus = new int[1];

        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);

        LogUtils.v(TAG, "Results of linking program:\n" + glGetProgramInfoLog(programObjectId));

        if (linkStatus[0] == 0) {
            //If it failed, delete the program object.
            glDeleteProgram(programObjectId);
            LogUtils.e(TAG, "Linking of program failed.");
            return 0;
        }

        glDeleteShader(vertexShaderId);
        glDeleteShader(fragmentShaderId);

        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);

        LogUtils.v(TAG, "Results of validating program:\n" + glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;
    }

    public static int buildProgram(String vertexShaderSource, String fragmentShaderSource) {
        int program;

        //Compile the shaders
        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);
        program = linkProgram(vertexShader, fragmentShader);

        validateProgram(program);

        return program;
    }

    public static void setUniform4v(int programId, String name, float x, float y, float z, float w) {
        int id = glGetUniformLocation(programId, name);
        glUniform4f(id, x, y, z, w);
    }
}
