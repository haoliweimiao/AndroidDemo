package com.hlw.moudle.opengl;

import android.opengl.Matrix;

public class MatrixHelper {
    public static void perspectiveM(float[] m, float yFovInDegress, float aspect, float n, float f) {
        //计算焦距
        final float angleInRadians = (float) (yFovInDegress * Math.PI / 180.0f);

        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));

        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((f + n) / (f - n));
        m[11] = -1f;

        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f * f * n) / (f - n));
        m[15] = 0f;
    }

    public static void setIdentityM(float[] modelMatrix, int i) {
        Matrix.setIdentityM(modelMatrix, i);
    }

    public static void translateM(float[] modelMatrix, int mOffset,
                                  float x, float y, float z) {
        Matrix.translateM(modelMatrix, mOffset, x, y, z);
    }

    public static void multiplyMM(float[] temp, int resultOffset,
                                  float[] projectionMatrix, int lhsOffset, float[] modelMatrix, int rhsOffset) {
        Matrix.multiplyMM(temp, resultOffset, projectionMatrix, lhsOffset, modelMatrix, rhsOffset);
    }

    public static void rotateM(float[] modelMatrix, int mOffset,
                               float a, float x, float y, float z) {
        Matrix.rotateM(modelMatrix, mOffset, a, x, y,z);
    }
}
