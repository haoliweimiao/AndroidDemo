package com.hlw.demo.activity.opengl.renderer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.hlw.demo.activity.opengl.MatrixHelper;
import com.hlw.demo.activity.opengl.ShaderHelper;
import com.hlw.demo.util.AssetsUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author hlw
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    /**
     * 定点坐标数量 2->4 增加 Z W轴，绘制3D图像
     */
    private static final int POSITION_COMPONENT_COUNT = 4;
    private static final int BYTES_PER_FLOAT = 4;

    private final FloatBuffer vertexData;
    protected int mProgram;

//    private static final String U_COLOR = "u_Color";
//    private int uColorLocation;

    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;

    private static final String A_COLOR = "a_Color";
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private int aColorLocation;

    private static final String A_Size = "a_Size";
    private int aSizeLocation;

    private static final String U_Matrix = "u_Matrix";
    private int uMatrixLocation;
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private float[] tableVerticesWithTriangles = {
            //Triangle 1
//            -0.5f, -0.5f,
//            0.5f, 0.5f,
//            -0.5f, 0.5f,

            //Triangle 2
//            -0.5f, -0.5f,
//            0.5f, -0.5f,
//            0.5f, 0.5f,

//            // Order of coordinates: X, Y, R, G, B
//            0f, 0f, 1f, 1f, 1f,
//            -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
//            0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
//            0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
//            -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
//            -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
//
//            // Line 1
//            -0.5f, 0f, 1f, 0f, 0f,
//            0.5f, 0f, 1f, 0f, 0f,
//
//            // Mallets
//            0f, -0.4f, 0f, 0f, 1f,
//            0f, 0.4f, 1f, 0f, 0f

            // Order of coordinates: X, Y, R, G, B
            0f, 0f, 0f, 1.5f, 1f, 1f, 1f,
            -0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,
            0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,
            0.5f, 0.8f, 0f, 2f, 0.7f, 0.7f, 0.7f,
            -0.5f, 0.8f, 0f, 2f, 0.7f, 0.7f, 0.7f,
            -0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,

            // Line 1
            -0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,
            0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,

            // Mallets
            0f, -0.4f, 0f, 1.25f, 0f, 0f, 1f,
            0f, 0.4f, 0f, 1.75f, 1f, 0f, 0f
    };

    private Context mContext;

    public AirHockeyRenderer(Context context) {
        mContext = context;

        vertexData =
                // allocateDirect 分配内存，一个float 32个字节，一个int 8个字节，*4
                ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                        //按照本地字节序(大小端)组织内容(当一个值占多个字节时，比如32位整形数，
                        //字节按照从最重要位到最不重要位或者相反顺序排列;可以认为从左到右或者从右到左写一个数类似)
                        .order(ByteOrder.nativeOrder())
                        .asFloatBuffer();

        //把数据从Dalvik的内存复制到本地内存
        vertexData.put(tableVerticesWithTriangles);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        String vertexShaderSource = AssetsUtil.getFromAssets(mContext, "glsl/simple_vertex_shader.glsl");
        String fragmentShaderSource = AssetsUtil.getFromAssets(mContext, "glsl/simple_fragment_shader.glsl");
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

        mProgram = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        ShaderHelper.validateProgram(mProgram);

        GLES20.glUseProgram(mProgram);

//        uColorLocation = GLES20.glGetUniformLocation(mProgram, U_COLOR);
        aColorLocation = GLES20.glGetAttribLocation(mProgram, A_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(mProgram, A_POSITION);
        aSizeLocation = GLES20.glGetUniformLocation(mProgram, A_Size);
        uMatrixLocation = GLES20.glGetUniformLocation(mProgram, U_Matrix);

        vertexData.position(0);
//        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, vertexData);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, vertexData);
        GLES20.glEnableVertexAttribArray(aColorLocation);


    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

//        final float aspectRadio = width > height ?
//                (float) width / (float) height :
//                (float) height / (float) width;
//
//        if (width > height) {
//            //Landscape
//            Matrix.orthoM(projectionMatrix, 0, -aspectRadio, aspectRadio, -1f, 1f, -1f, 1f);
//        } else {
//            //Portrait or square
//            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRadio, aspectRadio, -1f, 1f);
//        }

        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);
        MatrixHelper.setIdentityM(modelMatrix, 0);
        MatrixHelper.translateM(modelMatrix, 0, 0, 0, -3f);
        MatrixHelper.rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);
        final float[] temp = new float[16];
        MatrixHelper.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, projectionMatrix.length);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        //clear the rendering surface
        //清空屏幕，擦除所有颜色，使用 glClearColor 填充的颜色当背景
        GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT);

        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

        //绘制桌面
        GLES20.glUniform1f(aSizeLocation, 10.0f);
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);

        //绘制线
//        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

        //绘制点
//        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);

        //绘制点
//        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);

//        GLES20.glVertexAttrib4f(aPositionLocation,0.5f,0.5f,0.5f,0);
    }
}
