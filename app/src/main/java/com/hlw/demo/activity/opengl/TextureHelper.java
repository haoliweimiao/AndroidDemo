package com.hlw.demo.activity.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import com.hlw.demo.util.LogUtils;

import static android.opengl.GLES20.*;

public class TextureHelper {

    private static String TAG = TextureHelper.class.getSimpleName();

    public static int loadTexture(Context context, int resourceId) {
        final int[] textureObjectId = new int[1];
        glGenTextures(1, textureObjectId, 0);

        if (textureObjectId[0] == 0) {
            LogUtils.e(TAG, "Could not generate a new OpenGL texture object.");
            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

        if (bitmap == null) {
            LogUtils.e(TAG, "Resource ID " + resourceId + " could not be decoded.");
            glDeleteTextures(1, textureObjectId, 0);
            return 0;
        }

        glBindTexture(GL_TEXTURE_2D, textureObjectId[0]);
        //缩小情况使用三线性过滤
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR_MIPMAP_LINEAR);
        //放大情况使用 双线性过滤
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
        GLUtils.texImage2D(GL_TEXTURE_2D,0,bitmap,0);
        bitmap.recycle();
        //生成mip贴图
        glGenerateMipmap(GL_TEXTURE_2D);
        //解除纹理绑定
        glBindTexture(GL_TEXTURE_2D,0);
        return textureObjectId[0];
    }

}
