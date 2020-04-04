package com.hlw.demo.activity.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.hlw.demo.activity.opengl.renderer.ColorTriangleRenderer;
import com.hlw.demo.activity.opengl.renderer.RectangleRenderer;
import com.hlw.demo.activity.opengl.renderer.TriangleRenderer;
import com.hlw.demo.activity.opengl.renderer.TriangleRenderer2;
import com.hlw.demo.activity.opengl.renderer.TwoTriangleRenderer;

public enum OpenGLDrawType {
    //绘制三角形
    TRIANGLE("triangle"),
    TRIANGLE2("triangle2"),
    TWO_TRIANGLE("two_triangle"),
    //绘制彩色三角形
    COLOR_TRIANGLE("color_rectangle"),
    //绘制矩形
    RECTTRIANGLE("rectangle");

    private String value;

    OpenGLDrawType(String value) {
        this.value = value;
    }

    public static GLSurfaceView.Renderer getRenderer(Context context, String drawTypeStr) {
        OpenGLDrawType type = OpenGLDrawType.valueOf(drawTypeStr);

        switch (type) {
            case COLOR_TRIANGLE:
                return new ColorTriangleRenderer(context);
            case TRIANGLE2:
                return new TriangleRenderer2(context);
            case RECTTRIANGLE:
                return new RectangleRenderer(context);
            case TWO_TRIANGLE:
                return new TwoTriangleRenderer(context);
            case TRIANGLE:
            default:
                return new TriangleRenderer(context);
        }
    }
}
