package com.hlw.moudle.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.hlw.moudle.opengl.renderer.ColorTriangleRenderer;
import com.hlw.moudle.opengl.renderer.RectangleRenderer;
import com.hlw.moudle.opengl.renderer.SimpleVertexShaderRenderer;
import com.hlw.moudle.opengl.renderer.TriangleRenderer;
import com.hlw.moudle.opengl.renderer.TriangleRenderer2;
import com.hlw.moudle.opengl.renderer.TwoTriangleRenderer;
import com.hlw.moudle.opengl.renderer.texture.ImageTextureRenderer;
import com.hlw.moudle.opengl.renderer.texture.MipMap2DRenderer;
import com.hlw.moudle.opengl.renderer.texture.SimpleTexture2DRenderer;
import com.hlw.moudle.opengl.renderer.texture.SimpleTextureCubemapRenderer;
import com.hlw.moudle.opengl.renderer.texture.SmileBoxTextureRenderer;
import com.hlw.moudle.opengl.renderer.texture.TextureWrapRenderer;

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
    OPEN_SIMPLE_TEXTURE_CUBE_MAP("open_simple_texture_cube_map"),
    OPEN_TEXTURE_WARP("open_texture_warp"),
    OPEN_IMAGE_TEXTURE("open_image_texture"),
    //绘制矩形
    RECTANGLE("rectangle"),
    //绘制笑脸纹理
    SMILE_BOX_TEXTURE("smile_box_texture");

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
            case RECTANGLE:
                return new RectangleRenderer(context);
            case TWO_TRIANGLE:
                return new TwoTriangleRenderer(context);
            case OPEN_3D_BOX:
                return new SimpleVertexShaderRenderer(context);
            case OPEN_MIPMAP_2D:
                return new MipMap2DRenderer(context);
            case OPEN_SIMPLE_TEXTURE:
                return new SimpleTexture2DRenderer(context);
            case OPEN_SIMPLE_TEXTURE_CUBE_MAP:
                return new SimpleTextureCubemapRenderer(context);
            case OPEN_TEXTURE_WARP:
                return new TextureWrapRenderer(context);
            case OPEN_IMAGE_TEXTURE:
                return new ImageTextureRenderer(context);
//                return new TextureRenderer(context);
            case SMILE_BOX_TEXTURE:
                return new SmileBoxTextureRenderer(context);
            case TRIANGLE:
            default:
                return new TriangleRenderer(context);
        }
    }
}
