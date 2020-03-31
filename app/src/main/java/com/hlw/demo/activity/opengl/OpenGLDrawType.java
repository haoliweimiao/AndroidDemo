package com.hlw.demo.activity.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.hlw.demo.activity.opengl.renderer.RectangleRenderer;
import com.hlw.demo.activity.opengl.renderer.TriangleRenderer;
import com.hlw.demo.activity.opengl.renderer.TriangleRenderer2;

public enum OpenGLDrawType {
    //绘制三角形
    TRIANGLE("triangle"),
    TRIANGLE2("triangle2"),
    //绘制矩形
    RECTANGLE("rectangle");

    private String value;

    OpenGLDrawType(String value) {
        this.value = value;
    }

    public static GLSurfaceView.Renderer getRenderer(Context context, String drawTypeStr) {
        OpenGLDrawType type = OpenGLDrawType.valueOf(drawTypeStr);

        switch (type) {
            case RECTANGLE:
                return new RectangleRenderer(context);
            case TRIANGLE2:
                return new TriangleRenderer2(context);
            case TRIANGLE:
            default:
                return new TriangleRenderer(context);
        }
    }
}
