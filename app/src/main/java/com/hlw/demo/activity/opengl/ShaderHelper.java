package com.hlw.demo.activity.opengl;

import com.hlw.demo.util.LogUtils;

import static android.opengl.GLES20.*;

public class ShaderHelper {
    private static String TAG = ShaderHelper.class.getSimpleName();

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        final int shaderObjectId = glCreateShader(type);

        if (shaderObjectId == 0) {
            LogUtils.w(TAG, "Could not create new shader. ");
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
            LogUtils.w(TAG, "Compilation of shader failed.");

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

        final int[] linkStatus=new int[1];

        glGetProgramiv(programObjectId,GL_LINK_STATUS,linkStatus,0);

        LogUtils.v(TAG, "Results of linking program:\n" + glGetProgramInfoLog(programObjectId));

        if (linkStatus[0]==0){
            //If it failed, delete the program object.
            glDeleteProgram(programObjectId);
            LogUtils.w(TAG,"Linking of program failed.");
            return 0;
        }

        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId){
        glValidateProgram(programObjectId);

        final int[] validateStatus=new int[1];
        glGetProgramiv(programObjectId,GL_VALIDATE_STATUS,validateStatus,0);

        LogUtils.v(TAG, "Results of validating program:\n" + glGetProgramInfoLog(programObjectId));

        return validateStatus[0]!=0;
    }
}
