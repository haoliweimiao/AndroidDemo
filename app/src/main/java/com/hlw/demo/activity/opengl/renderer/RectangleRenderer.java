package com.hlw.demo.activity.opengl.renderer;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.hlw.demo.activity.opengl.OpenGLConstants;
import com.hlw.demo.activity.opengl.ShaderHelper;
import com.hlw.demo.util.LogUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES30.GL_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES30.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_STATIC_DRAW;
import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.GL_UNSIGNED_INT;
import static android.opengl.GLES30.glBindBuffer;
import static android.opengl.GLES30.glBindVertexArray;
import static android.opengl.GLES30.glBufferData;
import static android.opengl.GLES30.glClear;
import static android.opengl.GLES30.glClearColor;
import static android.opengl.GLES30.glDrawElements;
import static android.opengl.GLES30.glEnableVertexAttribArray;
import static android.opengl.GLES30.glGenBuffers;
import static android.opengl.GLES30.glGenVertexArrays;

public class RectangleRenderer implements GLSurfaceView.Renderer {

    private Context context;

    // set up vertex data (and buffer(s)) and configure vertex attributes
    // ------------------------------------------------------------------
    private float VERTICES[] = {
            0.5f, 0.5f, 0.0f, 1.0f, // top right
            0.5f, -0.5f, 0.0f, 1.0f,// bottom right
            -0.5f, -0.5f, 0.0f, 1.0f,// bottom left
            -0.5f, 0.5f, 0.0f, 1.0f // top left
    };
    private int INDICES[] = {
            // note that we start from 0!
            0, 1, 3, // first Triangle
            1, 2, 3  // second Triangle
    };

    private int[] VBO = new int[1], VAO = new int[1], EBO = new int[1];

//    private VertexArray vertexArray;
    //vec4(1.0f, 0.5f, 0.2f, 1.0f)

    private FloatBuffer verticeBuffer;
    private IntBuffer indicesBuffer;

    private final String A_POSITION = "a_Position";
    private int aPositionLocation;
    private final String U_COLOR = "u_Color";
    private int uColorLocation;


    private static int POSITION_COUNT = 4;
    private static int STRIDE = POSITION_COUNT * 4;

    public RectangleRenderer(Context context) {
        this.context = context;

        verticeBuffer =
                // allocateDirect 分配内存，一个float 32个字节，一个int 8个字节，*4
                ByteBuffer.allocateDirect(VERTICES.length * OpenGLConstants.BYTES_PER_FLOAT)
                        //按照本地字节序(大小端)组织内容(当一个值占多个字节时，比如32位整形数，
                        //字节按照从最重要位到最不重要位或者相反顺序排列;可以认为从左到右或者从右到左写一个数类似)
                        .order(ByteOrder.nativeOrder())
                        .asFloatBuffer()
                        .put(VERTICES);

        indicesBuffer =
                // allocateDirect 分配内存，一个float 32个字节，一个int 8个字节，*4
                ByteBuffer.allocateDirect(INDICES.length * OpenGLConstants.BYTES_PER_FLOAT)
                        //按照本地字节序(大小端)组织内容(当一个值占多个字节时，比如32位整形数，
                        //字节按照从最重要位到最不重要位或者相反顺序排列;可以认为从左到右或者从右到左写一个数类似)
                        .order(ByteOrder.nativeOrder())
                        .asIntBuffer()
                        .put(INDICES);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        int vertex = ShaderHelper.compileVertexShaderByAssetsPath(context, "glsl/triangle/vertex.glsl");
        int fragment = ShaderHelper.compileFragmentShaderByAssetsPath(context, "glsl/triangle/fragment.glsl");
        int program = ShaderHelper.linkProgram(vertex, fragment);

        ShaderHelper.validateProgram(program);

        GLES30.glUseProgram(program);

        aPositionLocation = GLES30.glGetAttribLocation(program, A_POSITION);
        uColorLocation = GLES30.glGetUniformLocation(program, U_COLOR);
        GLES30.glUniform4f(uColorLocation, 1.0f, 0.5f, 0.2f, 1.0f);
//
//        vertexArray.setVertexAttributePointer(0, aPositionLocation, POSITION_COUNT, STRIDE);

//        VAO = IntBuffer.allocate(1);
//        VBO = IntBuffer.allocate(1);
//        EBO = IntBuffer.allocate(1);

//        vertexArray = new VertexArray(VERTICES);
        glGenVertexArrays(1, IntBuffer.wrap(VAO));
        glGenBuffers(1, IntBuffer.wrap(VBO));
        glGenBuffers(1, IntBuffer.wrap(EBO));

        LogUtils.i(String.format("VAO:%s VBO:%s EBO:%s",VAO[0],VBO[0],EBO[0]));

        // bind the Vertex Array Object first, then bind and set vertex buffer(s), and
        // then configure vertex attributes(s).
        glBindVertexArray(VAO[0]);

        verticeBuffer.position(0);

        glBindBuffer(GL_ARRAY_BUFFER, VBO[0]);
        glBufferData(GL_ARRAY_BUFFER, POSITION_COUNT, verticeBuffer, GL_STATIC_DRAW);

        indicesBuffer.position(0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO[0]);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, 6, indicesBuffer,
                GL_STATIC_DRAW);

        GLES30.glVertexAttribPointer(aPositionLocation, POSITION_COUNT, GLES30.GL_FLOAT, false, STRIDE, verticeBuffer);
        glEnableVertexAttribArray(aPositionLocation);

        // note that this is allowed, the call to glVertexAttribPointer registered VBO
        // as the vertex attribute's bound vertex buffer object so afterwards we can
        // safely unbind
        glBindBuffer(GL_ARRAY_BUFFER, VBO[0]);

        // remember: do NOT unbind the EBO while a VAO is active as the bound element
        // buffer object IS stored in the VAO; keep the EBO bound.
//         glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO[0]);

        // You can unbind the VAO afterwards so other VAO calls won't accidentally
        // modify this VAO, but this rarely happens. Modifying other VAOs requires a
        // call to glBindVertexArray anyways so we generally don't unbind VAOs (nor
        // VBOs) when it's not directly necessary.
        // glBindVertexArray(VAO[0]);

        // uncomment this call to draw in wireframe polygons.
        // glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //clear the rendering surface
        //清空屏幕，擦除所有颜色，使用 glClearColor 填充的颜色当背景
        GLES30.glClear(GL10.GL_COLOR_BUFFER_BIT);
//
//        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);


        // render
        // ------
//        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
//        glClear(GL_COLOR_BUFFER_BIT);

        // draw our first triangle
//        glUseProgram(shaderProgram);
//        glBindVertexArray(
//                VAO[0]); // seeing as we only have a single VAO there's no need to bind it
        // every time, but we'll do so to keep things a bit more organized
        // glDrawArrays(GL_TRIANGLES, 0, 6);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        // glBindVertexArray(0); // no need to unbind it every time

        // glfw: swap buffers and poll IO events (keys pressed/released, mouse moved
        // etc.)
        // -------------------------------------------------------------------------------
        //检查并调用事件，交换缓冲
//        glfwSwapBuffers(window);
//        glfwPollEvents();

    }
}
