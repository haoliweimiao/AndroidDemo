package com.hlw.demo.activity.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.hlw.demo.activity.opengl.renderer.ColorTriangleRenderer;
import com.hlw.demo.activity.opengl.renderer.MipMap2DRenderer;
import com.hlw.demo.activity.opengl.renderer.RectangleRenderer;
import com.hlw.demo.activity.opengl.renderer.SimpleTexture2DRenderer;
import com.hlw.demo.activity.opengl.renderer.SimpleTextureCubemapRenderer;
import com.hlw.demo.activity.opengl.renderer.SimpleVertexShaderRenderer;
import com.hlw.demo.activity.opengl.renderer.TextureWrapRenderer;
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
    //绘制3D 立方体
    OPEN_3D_BOX("open_3d_box"),
    //绘制3D 立方体
    OPEN_MIPMAP_2D("open_mipmap_2d"),
    OPEN_SIMPLE_TEXTURE("open_simple_texture"),
    OPEN_SIMPLE_TEXTURE_CUBEMAP("open_simple_texture_cubemap"),
    OPEN_TEXTURE_WARP("open_texture_warp"),
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
            case OPEN_3D_BOX:
                return new SimpleVertexShaderRenderer(context);
            case OPEN_MIPMAP_2D:
                return new MipMap2DRenderer(context);
            case OPEN_SIMPLE_TEXTURE:
                return new SimpleTexture2DRenderer(context);
            case OPEN_SIMPLE_TEXTURE_CUBEMAP:
                return new SimpleTextureCubemapRenderer(context);
            case OPEN_TEXTURE_WARP:
                return new TextureWrapRenderer(context);
            case TRIANGLE:
            default:
                return new TriangleRenderer(context);
        }
    }
}
